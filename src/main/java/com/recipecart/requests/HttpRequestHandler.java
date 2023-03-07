/* (C)2023 */
package com.recipecart.requests;

import static spark.Spark.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.execution.EntityCommander;
import com.recipecart.usecases.*;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.Utils;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import spark.Request;
import spark.Response;

/**
 * This class takes requests from the front-end to do some use case, tells the Business Logic Layer
 * to perform that use case, then gives the front-end an output response.
 */
public class HttpRequestHandler {
    static final int OK = 200,
            CREATED = 201,
            BAD_REQUEST = 400,
            UNAUTHORIZED = 401,
            NOT_FOUND = 404,
            INTERNAL_SERVER_ERROR = 500;
    private static final String APPLICATION_JSON = "application/json";
    private static final String UNAUTHORIZED_MESSAGE =
            "User is not properly authorized to do this task";
    private static final Map<String, Integer> messageToStatusCode = initializeMessageToStatusCode();

    private final @NotNull EntityCommander commander;
    private final @NotNull JwtValidator loginChecker;
    private final int listenPort;

    private final Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();

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
        post("/bookmark/recipe", APPLICATION_JSON, this::handleBookmarkRecipeRequest, gson::toJson);
        post(
                "/shopping-list/add-ingredients",
                APPLICATION_JSON,
                this::handleAddIngredientsToShoppingListRequest,
                gson::toJson);
        post(
                "/shopping-list/add-recipe-ingredients",
                APPLICATION_JSON,
                this::handleAddRecipeToShoppingListRequest,
                gson::toJson);
    }

    private boolean isAuthorized(RequestBodies.WithLoginRequired requestBodyDetails) {
        return true;
        // use the line of code below once checkValidity is implemented
        // return loginChecker.checkValidity(requestBodyDetails.getEncryptedJwtToken());
    }

    private String handleUnauthorized(Response response) {
        response.status(messageToStatusCode.get(UNAUTHORIZED_MESSAGE));
        response.type(APPLICATION_JSON);
        return UNAUTHORIZED_MESSAGE;
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

    private <T> Object handleGetEntityRequest(
            Request request,
            Response response,
            String paramName,
            Function<String, ? extends SimpleGetCommand<T>> commandMaker,
            BiFunction<String, ? super T, ? extends ResponseBodies.WithMessage> retrieval) {
        String entityName = request.params(paramName);

        SimpleGetCommand<T> command = commandMaker.apply(entityName);
        String executionMessage = handleCommand(command, response);

        return retrieval.apply(executionMessage, command.getRetrievedEntity());
    }

    private <T> Object handleSimplePostRequest(
            Request request,
            Response response,
            boolean authenticationRequired,
            Class<T> requestBodyClass,
            Function<? super T, ? extends EntityCommand> commandMaker) {
        T bodyDetails = getRequestBodyDetails(request, requestBodyClass);

        String executionMessage;
        if (!authenticationRequired
                || isAuthorized((RequestBodies.WithLoginRequired) bodyDetails)) {
            EntityCommand command = commandMaker.apply(bodyDetails);
            executionMessage = handleCommand(command, response);
        } else {
            executionMessage = handleUnauthorized(response);
        }

        return new ResponseBodies.WithMessage(executionMessage);
    }

    private Object handleSearchRecipesRequest(Request request, Response response) {
        Set<String> searchTerms = getQueryArgumentWords(request, "terms");

        SearchRecipesCommand searchRecipesCommand = new SearchRecipesCommand(searchTerms);
        String message = handleCommand(searchRecipesCommand, response);

        if (searchRecipesCommand.getMatchingEntities() != null) {
            List<RecipeForm> matches = new ArrayList<>();
            searchRecipesCommand
                    .getMatchingEntities()
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
            return new ResponseBodies.RecipeCreation(handleUnauthorized(response), null, null);
        }
    }

    private Object handleCreateUserRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                false,
                RequestBodies.UserCreation.class,
                (bodyDetails) ->
                        new CreateUserCommand(
                                bodyDetails.getUsername(), bodyDetails.getEmailAddress()));
    }

    private Object handleCreateIngredientRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                false,
                RequestBodies.IngredientCreation.class,
                (bodyDetails) ->
                        new CreateIngredientCommand(
                                bodyDetails.getName(),
                                bodyDetails.getUnits(),
                                bodyDetails.getImageUri()));
    }

    private Object handleCreateTagRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                false,
                RequestBodies.TagCreation.class,
                (bodyDetails) -> new CreateTagCommand(bodyDetails.getName()));
    }

    private Object handleGetTagRequest(Request request, Response response) {
        return handleGetEntityRequest(
                request, response, ":tag", GetTagCommand::new, ResponseBodies.TagRetrieval::new);
    }

    private Object handleGetIngredientRequest(Request request, Response response) {
        return handleGetEntityRequest(
                request,
                response,
                ":ingredient",
                GetIngredientCommand::new,
                ResponseBodies.IngredientRetrieval::new);
    }

    private Object handleGetRecipeRequest(Request request, Response response) {
        return handleGetEntityRequest(
                request,
                response,
                ":recipe",
                GetRecipeCommand::new,
                ResponseBodies.RecipeRetrieval::new);
    }

    private Object handleGetUserRequest(Request request, Response response) {
        return handleGetEntityRequest(
                request, response, ":user", GetUserCommand::new, ResponseBodies.UserRetrieval::new);
    }

    private Object handleBookmarkRecipeRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                true,
                RequestBodies.RecipeBookmarking.class,
                (bodyDetails) ->
                        new BookmarkRecipeCommand(
                                bodyDetails.getUsername(), bodyDetails.getRecipeName()));
    }

    private Object handleAddIngredientsToShoppingListRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                true,
                RequestBodies.IngredientToShoppingListAddition.class,
                (bodyDetails) ->
                        new AddIngredientsToShoppingListCommand(
                                bodyDetails.getUsername(), bodyDetails.getIngredients()));
    }

    private Object handleAddRecipeToShoppingListRequest(Request request, Response response) {
        return handleSimplePostRequest(
                request,
                response,
                true,
                RequestBodies.RecipeToShoppingListAddition.class,
                (bodyDetails) ->
                        new AddRecipeToShoppingListCommand(
                                bodyDetails.getUsername(),
                                bodyDetails.getRecipeName(),
                                bodyDetails.isAddOnlyMissingIngredients()));
    }

    private static Map<String, Integer> initializeMessageToStatusCode() {
        Map<String, Integer> map = new HashMap<>();

        map.put(UNAUTHORIZED_MESSAGE, UNAUTHORIZED);
        map.put(Command.NOT_OK_ERROR, INTERNAL_SERVER_ERROR);
        map.put(Command.NOT_OK_IMPOSSIBLE_OUTCOME, INTERNAL_SERVER_ERROR);
        map.put(EntityCommand.NOT_OK_BAD_STORAGE, INTERNAL_SERVER_ERROR);

        map.put(SearchRecipesCommand.NOT_OK_BAD_SEARCH_TERMS, BAD_REQUEST);
        map.put(SearchRecipesCommand.OK_MATCHES_FOUND, OK);
        map.put(SearchRecipesCommand.OK_NO_MATCHES_FOUND, OK);

        map.put(CreateRecipeCommand.OK_RECIPE_CREATED_WITH_GIVEN_NAME, CREATED);
        map.put(CreateRecipeCommand.OK_RECIPE_CREATED_NAME_ASSIGNED, CREATED);
        map.put(CreateRecipeCommand.NOT_OK_INVALID_RECIPE, BAD_REQUEST);
        map.put(CreateRecipeCommand.NOT_OK_RECIPE_RESOURCES_NOT_FOUND, NOT_FOUND);
        map.put(CreateRecipeCommand.NOT_OK_RECIPE_NAME_TAKEN, BAD_REQUEST);

        map.put(CreateTagCommand.OK_TAG_CREATED, CREATED);
        map.put(CreateTagCommand.NOT_OK_INVALID_TAG, BAD_REQUEST);
        map.put(CreateTagCommand.NOT_OK_TAG_NAME_TAKEN, BAD_REQUEST);

        map.put(CreateIngredientCommand.OK_INGREDIENT_CREATED, CREATED);
        map.put(CreateIngredientCommand.NOT_OK_INVALID_INGREDIENT, BAD_REQUEST);
        map.put(CreateIngredientCommand.NOT_OK_INGREDIENT_NAME_TAKEN, BAD_REQUEST);

        map.put(CreateUserCommand.OK_USER_CREATED, CREATED);
        map.put(CreateUserCommand.NOT_OK_INVALID_USER, BAD_REQUEST);
        map.put(CreateUserCommand.NOT_OK_USERNAME_TAKEN, BAD_REQUEST);

        map.put(GetTagCommand.OK_TAG_RETRIEVED, OK);
        map.put(GetTagCommand.NOT_OK_TAG_NOT_FOUND, NOT_FOUND);

        map.put(GetIngredientCommand.OK_INGREDIENT_RETRIEVED, OK);
        map.put(GetIngredientCommand.NOT_OK_INGREDIENT_NOT_FOUND, NOT_FOUND);

        map.put(GetRecipeCommand.OK_RECIPE_RETRIEVED, OK);
        map.put(GetRecipeCommand.NOT_OK_RECIPE_NOT_FOUND, NOT_FOUND);

        map.put(GetUserCommand.OK_USER_RETRIEVED, OK);
        map.put(GetUserCommand.NOT_OK_USER_NOT_FOUND, NOT_FOUND);

        map.put(BookmarkRecipeCommand.OK_RECIPE_BOOKMARKED, OK);
        map.put(BookmarkRecipeCommand.NOT_OK_INVALID_RECIPE_NAME, BAD_REQUEST);
        map.put(BookmarkRecipeCommand.NOT_OK_INVALID_USERNAME, BAD_REQUEST);
        map.put(BookmarkRecipeCommand.NOT_OK_RECIPE_NOT_FOUND, NOT_FOUND);
        map.put(BookmarkRecipeCommand.NOT_OK_USER_NOT_FOUND, NOT_FOUND);
        map.put(BookmarkRecipeCommand.NOT_OK_RECIPE_ALREADY_BOOKMARKED, BAD_REQUEST);

        map.put(ShoppingListCommand.OK_SHOPPING_LIST_UPDATED, OK);
        map.put(ShoppingListCommand.NOT_OK_INVALID_USERNAME, BAD_REQUEST);
        map.put(ShoppingListCommand.NOT_OK_USER_NOT_FOUND, NOT_FOUND);

        map.put(AddIngredientsToShoppingListCommand.NOT_OK_INVALID_INGREDIENTS, BAD_REQUEST);
        map.put(AddIngredientsToShoppingListCommand.NOT_OK_INGREDIENT_NOT_FOUND, NOT_FOUND);

        map.put(AddRecipeToShoppingListCommand.NOT_OK_INVALID_RECIPE, BAD_REQUEST);
        map.put(AddRecipeToShoppingListCommand.NOT_OK_RECIPE_NOT_FOUND, NOT_FOUND);

        return map;
    }
}
