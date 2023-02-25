/* (C)2023 */
package com.recipecart.requests;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.execution.EntityCommander;
import com.recipecart.usecases.*;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.UserForm;
import com.recipecart.utils.Utils;
import java.util.*;
import java.util.stream.Collectors;
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
    private static final Map<String, Integer> messageToStatusCode = initializeMessageToStatusCode();

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
        post("/create/recipe", APPLICATION_JSON, this::handleCreateRecipeRequest, gson::toJson);
        post("/create/user", APPLICATION_JSON, this::handleCreateUserRequest, gson::toJson);
        post(
                "/create/ingredient",
                APPLICATION_JSON,
                this::handleCreateIngredientRequest,
                gson::toJson);
        post("/create/tag", APPLICATION_JSON, this::handleCreateTagRequest, gson::toJson);
        get("/tags/:tag", APPLICATION_JSON, this::handleGetTagRequest, gson::toJson);
        get(
                "/ingredients/:ingredient",
                APPLICATION_JSON,
                this::handleGetIngredientRequest,
                gson::toJson);
        get("/recipes/:recipe", APPLICATION_JSON, this::handleGetRecipeRequest, gson::toJson);
        get("/users/:user", APPLICATION_JSON, this::handleGetUserRequest, gson::toJson);
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

        if (searchRecipesCommand.getMatchingRecipes() != null) {
            List<RecipeForm> matches = new ArrayList<>();
            searchRecipesCommand
                    .getMatchingRecipes()
                    .forEach((match) -> matches.add(new RecipeForm(match)));
            return new ResponseBodies.RecipeSearch(message, matches);
        } else {
            return new ResponseBodies.RecipeSearch(message, null);
        }
    }

    private Object handleCreateRecipeRequest(Request request, Response response) {
        RequestBodies.RecipeCreation bodyDetails =
                getRequestBodyDetails(request, RequestBodies.RecipeCreation.class);

        if (isAuthorized(bodyDetails)) {
            CreateRecipeCommand command = new CreateRecipeCommand(bodyDetails.getRecipeForm());
            String executionMessage = handleCommand(command, response);

            Recipe createdRecipe = command.getCreatedRecipe();
            return new ResponseBodies.RecipeCreation(
                    executionMessage,
                    Utils.allowNull(createdRecipe, Recipe::getName),
                    Utils.allowNull(
                            command.getCreatedTags(),
                            (t) -> t.stream().map(Tag::toString).collect(Collectors.toSet())));
        } else {
            return new RequestBodies.RecipeCreation(handleUnauthorized(response), null);
        }
    }

    private Object handleCreateUserRequest(Request request, Response response) {
        RequestBodies.UserCreation bodyDetails =
                getRequestBodyDetails(request, RequestBodies.UserCreation.class);

        CreateUserCommand command =
                new CreateUserCommand(bodyDetails.getUsername(), bodyDetails.getEmailAddress());
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.WithMessage(executionMessage);
    }

    private Object handleCreateIngredientRequest(Request request, Response response) {
        RequestBodies.IngredientCreation bodyDetails =
                getRequestBodyDetails(request, RequestBodies.IngredientCreation.class);

        CreateIngredientCommand command =
                new CreateIngredientCommand(
                        bodyDetails.getName(), bodyDetails.getUnits(), bodyDetails.getImageUri());
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.WithMessage(executionMessage);
    }

    private Object handleCreateTagRequest(Request request, Response response) {
        RequestBodies.TagCreation bodyDetails =
                getRequestBodyDetails(request, RequestBodies.TagCreation.class);

        CreateTagCommand command = new CreateTagCommand(bodyDetails.getName());
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.WithMessage(executionMessage);
    }

    private Object handleGetTagRequest(Request request, Response response) {
        String tagName = request.params(":tag");

        GetTagCommand command = new GetTagCommand(tagName);
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.TagRetrieval(executionMessage, command.getRetrievedEntity());
    }

    private Object handleGetIngredientRequest(Request request, Response response) {
        String ingredientName = request.params(":ingredient");

        GetIngredientCommand command = new GetIngredientCommand(ingredientName);
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.IngredientRetrieval(
                executionMessage, command.getRetrievedEntity());
    }

    private Object handleGetRecipeRequest(Request request, Response response) {
        String recipeName = request.params(":recipe");

        GetRecipeCommand command = new GetRecipeCommand(recipeName);
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.RecipeRetrieval(
                executionMessage, Utils.allowNull(command.getRetrievedEntity(), RecipeForm::new));
    }

    private Object handleGetUserRequest(Request request, Response response) {
        String username = request.params(":user");

        GetUserCommand command = new GetUserCommand(username);
        String executionMessage = handleCommand(command, response);

        return new ResponseBodies.UserRetrieval(
                executionMessage, Utils.allowNull(command.getRetrievedEntity(), UserForm::new));
    }

    private static Map<String, Integer> initializeMessageToStatusCode() {
        Map<String, Integer> map = new HashMap<>();

        map.put(UNAUTHORIZED, 401);
        map.put(Command.NOT_OK_ERROR, 500);
        map.put(EntityCommand.NOT_OK_BAD_STORAGE, 500);

        map.put(SearchRecipesCommand.NOT_OK_BAD_SEARCH_TERMS, 400);
        map.put(SearchRecipesCommand.OK_MATCHES_FOUND, 200);
        map.put(SearchRecipesCommand.OK_NO_MATCHES_FOUND, 200);

        map.put(CreateRecipeCommand.OK_RECIPE_CREATED_WITH_GIVEN_NAME, 201);
        map.put(CreateRecipeCommand.OK_RECIPE_CREATED_NAME_ASSIGNED, 201);
        map.put(CreateRecipeCommand.NOT_OK_INVALID_RECIPE, 400);
        map.put(CreateRecipeCommand.NOT_OK_RECIPE_RESOURCES_NOT_FOUND, 404);
        map.put(CreateRecipeCommand.NOT_OK_RECIPE_NAME_TAKEN, 400);

        map.put(CreateTagCommand.OK_TAG_CREATED, 201);
        map.put(CreateTagCommand.NOT_OK_INVALID_TAG, 400);
        map.put(CreateTagCommand.NOT_OK_TAG_NAME_TAKEN, 400);

        map.put(CreateIngredientCommand.OK_INGREDIENT_CREATED, 201);
        map.put(CreateIngredientCommand.NOT_OK_INVALID_INGREDIENT, 400);
        map.put(CreateIngredientCommand.NOT_OK_INGREDIENT_NAME_TAKEN, 400);

        map.put(CreateUserCommand.OK_USER_CREATED, 201);
        map.put(CreateUserCommand.NOT_OK_INVALID_USER, 400);
        map.put(CreateUserCommand.NOT_OK_USERNAME_TAKEN, 400);

        map.put(GetTagCommand.OK_TAG_RETRIEVED, 200);
        map.put(GetTagCommand.NOT_OK_TAG_NOT_FOUND, 404);

        map.put(GetIngredientCommand.OK_INGREDIENT_RETRIEVED, 200);
        map.put(GetIngredientCommand.NOT_OK_INGREDIENT_NOT_FOUND, 404);

        map.put(GetRecipeCommand.OK_RECIPE_RETRIEVED, 200);
        map.put(GetRecipeCommand.NOT_OK_RECIPE_NOT_FOUND, 404);

        map.put(GetUserCommand.OK_USER_RETRIEVED, 200);
        map.put(GetUserCommand.NOT_OK_USER_NOT_FOUND, 404);

        return map;
    }
}
