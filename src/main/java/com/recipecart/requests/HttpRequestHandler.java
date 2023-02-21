/* (C)2023 */
package com.recipecart.requests;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.recipecart.entities.Recipe;
import com.recipecart.execution.EntityCommander;
import com.recipecart.usecases.*;
import com.recipecart.utils.Utils;
import java.util.Map;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;

/**
 * This class takes requests from the front-end to do some use case, tells the Business Logic Layer
 * to perform that use case, then gives the front-end an output response.
 */
public class HttpRequestHandler {
    private static final String APPLICATION_JSON = "application/json";
    private static final String UNAUTHORIZED = "User is not properly authorized to do this task";
    private static final Map<String, Integer> messageToStatusCode =
            Map.of(
                    UNAUTHORIZED,
                    401,
                    Command.NOT_OK_ERROR,
                    500,
                    EntityCommand.NOT_OK_BAD_STORAGE,
                    500,
                    SearchRecipesCommand.NOT_OK_BAD_SEARCH_TERMS,
                    400,
                    SearchRecipesCommand.OK_MATCHES_FOUND,
                    200,
                    SearchRecipesCommand.OK_NO_MATCHES_FOUND,
                    200,
                    CreateRecipeCommand.OK_RECIPE_CREATED_WITH_GIVEN_NAME,
                    201,
                    CreateRecipeCommand.OK_RECIPE_CREATED_NAME_ASSIGNED,
                    201);

    private final @NotNull EntityCommander commander;
    private final @NotNull JwtValidator loginChecker;
    private final int listenPort;

    private final Gson gson = new Gson();

    /**
     * Creates a handler that sends its commands to the given EntityCommander, validates logins with
     * the given LoginValidator, and listens in on the given port.
     *
     * @param commander where commands are executed
     * @param loginChecker where logins are verified
     * @param listenPort the port for this handler to listen on
     */
    public HttpRequestHandler(
            @NotNull EntityCommander commander,
            @NotNull JwtValidator loginChecker,
            int listenPort) {
        this.commander = commander;
        this.loginChecker = loginChecker;
        this.listenPort = listenPort;
    }

    /** Gets this handler to start taking requests from the front-end. */
    public void startHandler() {
        port(listenPort);
        get("/search/recipes", APPLICATION_JSON, this::handleSearchRecipesRequest, gson::toJson);
        post("/recipes/create", APPLICATION_JSON, this::handleCreateRecipeRequest, gson::toJson);
    }

    private boolean isAuthorized(RequestBodies.WithLoginRequired requestBodyDetails) {
        return true;
        // use the line of code below once checkValidity is implemented
        // return loginChecker.checkValidity(requestBodyDetails.getEncryptedJwtToken());
    }

    private String handleUnauthorized(Response response) {
        response.status(messageToStatusCode.get(UNAUTHORIZED));
        response.type(APPLICATION_JSON);
        return UNAUTHORIZED;
    }

    private String handleCommand(EntityCommand command, Response response) {
        commander.execute(command);
        String message = command.getExecutionMessage();
        response.status(messageToStatusCode.get(message));
        response.type(APPLICATION_JSON);
        return message;
    }

    private <T> T getRequestBodyDetails(Request request, Class<T> classOfT) {
        return gson.fromJson(request.body(), classOfT);
    }

    private Set<String> getQueryArgumentWords(Request request, String queryParam) {
        String rawSearchTerms = request.queryParams(queryParam);
        return Utils.allowNull(rawSearchTerms, (str) -> Set.of(str.split("\\s+")));
    }

    private Object handleSearchRecipesRequest(Request request, Response response) {
        Set<String> searchTerms = getQueryArgumentWords(request, "terms");

        SearchRecipesCommand searchRecipesCommand = new SearchRecipesCommand(searchTerms);
        String message = handleCommand(searchRecipesCommand, response);

        return new ResponseBodies.RecipeSearch(message, searchRecipesCommand.getMatchingRecipes());
    }

    private Object handleCreateRecipeRequest(Request request, Response response) {
        RequestBodies.RecipeCreation bodyDetails =
                getRequestBodyDetails(request, RequestBodies.RecipeCreation.class);

        if (isAuthorized(bodyDetails)) {
            CreateRecipeCommand createRecipeCommand =
                    new CreateRecipeCommand(bodyDetails.getRecipeForm());
            String executionMessage = handleCommand(createRecipeCommand, response);

            Recipe createdRecipe = createRecipeCommand.getCreatedRecipe();
            return new ResponseBodies.RecipeCreation(
                    executionMessage, Utils.allowNull(createdRecipe, Recipe::getName));
        } else {
            return new RequestBodies.RecipeCreation(handleUnauthorized(response), null);
        }
    }
}
