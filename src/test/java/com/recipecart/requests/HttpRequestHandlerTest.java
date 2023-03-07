/* (C)2023 */
package com.recipecart.requests;

import static com.recipecart.requests.HttpRequestHandler.*;
import static com.recipecart.testutil.TestUtils.getMockStorageGenerators;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.recipecart.database.MapEntitySaveAndLoader;
import com.recipecart.entities.*;
import com.recipecart.execution.EntityCommander;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import com.recipecart.usecases.*;
import com.recipecart.utils.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class HttpRequestHandlerTest {
    private static final int PORT = 7654;
    private static final String APPLICATION_JSON = "application/json",
            ACCEPT = "Accept",
            POST = "POST",
            GET = "GET",
            CONTENT_TYPE = "Content-type",
            BASE_URL = "http://localhost:" + PORT;

    private static final Gson gson =
            new GsonBuilder().serializeSpecialFloatingPointValues().create();
    private static ModifiableCommander commander;
    private static EntityStorage storageSource;
    private static HttpRequestHandler handler;

    private static String getFullUrl(String route) {
        return BASE_URL + route;
    }

    private static HttpURLConnection initRequestJson(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty(ACCEPT, APPLICATION_JSON);
        connection.setDoOutput(true);

        return connection;
    }

    private static HttpURLConnection initPostRequestJson(String urlString) throws IOException {
        HttpURLConnection connection = initRequestJson(urlString);
        connection.setRequestMethod(POST);
        connection.setRequestProperty(CONTENT_TYPE, APPLICATION_JSON);
        connection.setDoOutput(true);

        return connection;
    }

    private static HttpURLConnection initGetRequestJson(String urlString) throws IOException {
        HttpURLConnection connection = initRequestJson(urlString);
        connection.setRequestMethod(GET);

        return connection;
    }

    private static <T> void sendPostRequestJson(URLConnection connection, T requestBody)
            throws IOException {
        OutputStreamWriter requestBodyWriter =
                new OutputStreamWriter(connection.getOutputStream(), StandardCharsets.UTF_8);
        String requestBodyJson = gson.toJson(requestBody, requestBody.getClass());
        requestBodyWriter.write(requestBodyJson);
        requestBodyWriter.flush();
        requestBodyWriter.close();
    }

    private static <U> TwoTuple<Integer, U> requestResultsJson(
            HttpURLConnection connection, Class<U> responseBodyType) throws IOException {
        InputStreamReader responseBodyReader =
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8);

        U response = gson.fromJson(responseBodyReader, responseBodyType);
        responseBodyReader.close();
        return new TwoTuple<>(connection.getResponseCode(), response);
    }

    private static <T, U> TwoTuple<Integer, U> performPostRequestJson(
            String urlString, T requestBody, Class<U> responseBodyType) throws IOException {
        HttpURLConnection connection = initPostRequestJson(urlString);
        sendPostRequestJson(connection, requestBody);
        return requestResultsJson(connection, responseBodyType);
    }

    private static <U> TwoTuple<Integer, U> performGetRequestJson(
            String urlString, Class<U> responseBodyType) throws IOException {
        HttpURLConnection connection = initGetRequestJson(urlString);
        return requestResultsJson(connection, responseBodyType);
    }

    @BeforeAll
    static void initRequestHandler() {
        MapEntitySaveAndLoader saveAndLoader = new MapEntitySaveAndLoader();
        EntityStorage storage = new EntityStorage(saveAndLoader, saveAndLoader);
        commander = new ModifiableCommander(storage);
        JwtValidator alwaysPassValidator =
                new JwtValidator() {
                    @Override
                    public boolean checkValidity(String encryptedJwt) {
                        return true;
                    }
                };
        handler = new HttpRequestHandler(commander, alwaysPassValidator, PORT);
        handler.startHandler();
    }

    @BeforeEach
    void resetStorage() {
        MapEntitySaveAndLoader saveAndLoader = new MapEntitySaveAndLoader();
        EntityStorage storage = new EntityStorage(saveAndLoader, saveAndLoader);
        commander.setStorageSource(storage);
        storageSource = storage;
    }

    static Stream<Arguments> getRecipe() {
        return TestUtils.generateArguments(TestData::getRecipes);
    }

    static Stream<Arguments> getUser() {
        return TestUtils.generateArguments(TestData::getUsers);
    }

    static Stream<Arguments> getIngredient() {
        return TestUtils.generateArguments(TestData::getIngredients);
    }

    static Stream<Arguments> getTag() {
        return TestUtils.generateArguments(TestData::getTags);
    }

    static Stream<Arguments> getUserAndRecipe() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getRecipes), 1, true);
    }

    private static Stream<Arguments> getUserAndIngredients() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getNonEmptyMapIngredientDoubleNoNulls),
                1,
                true);
    }

    private static Stream<Arguments> getSearchRecipes() {
        return TestUtils.getSearchRecipes(getMockStorageGenerators());
    }

    private static Stream<Arguments> getSearchUsers() {
        return TestUtils.getSearchUsers(getMockStorageGenerators());
    }

    private static Stream<Arguments> getSearchIngredients() {
        return TestUtils.getSearchIngredients(getMockStorageGenerators());
    }

    private static Stream<Arguments> getSearchTags() {
        return TestUtils.getSearchTags(getMockStorageGenerators());
    }

    // @ParameterizedTest
    @MethodSource("getRecipe")
    void testGetRecipe(Recipe recipe) throws IOException {
        storageSource.getSaver().updateRecipes(Collections.singleton(recipe));
        RecipeForm expectedRecipe = new RecipeForm(recipe);
        String url = getFullUrl("/recipes/" + recipe.getName());

        TwoTuple<Integer, ResponseBodies.RecipeRetrieval> retrieval =
                performGetRequestJson(url, ResponseBodies.RecipeRetrieval.class);

        assertEquals(OK, retrieval.getFirst());
        assertEquals(GetRecipeCommand.OK_RECIPE_RETRIEVED, retrieval.getSecond().getMessage());
        assertEquals(expectedRecipe, retrieval.getSecond().getRecipe());
    }

    // @ParameterizedTest
    @MethodSource("getUser")
    void testGetUser(User user) throws IOException {
        storageSource.getSaver().updateUsers(Collections.singleton(user));
        UserForm expectedUser = new UserForm(user);
        String url = getFullUrl("/users/" + user.getUsername());

        TwoTuple<Integer, ResponseBodies.UserRetrieval> retrieval =
                performGetRequestJson(url, ResponseBodies.UserRetrieval.class);

        assertEquals(OK, retrieval.getFirst());
        assertEquals(GetUserCommand.OK_USER_RETRIEVED, retrieval.getSecond().getMessage());
        assertEquals(expectedUser, retrieval.getSecond().getUser());
    }

    // @ParameterizedTest
    @MethodSource("getIngredient")
    void testGetIngredient(Ingredient ingredient) throws IOException {
        storageSource.getSaver().updateIngredients(Collections.singleton(ingredient));
        String url = getFullUrl("/ingredients/" + ingredient.getName());

        TwoTuple<Integer, ResponseBodies.IngredientRetrieval> retrieval =
                performGetRequestJson(url, ResponseBodies.IngredientRetrieval.class);

        assertEquals(OK, retrieval.getFirst());
        assertEquals(
                GetIngredientCommand.OK_INGREDIENT_RETRIEVED, retrieval.getSecond().getMessage());
        assertEquals(ingredient, retrieval.getSecond().getIngredient());
    }

    // @ParameterizedTest
    @MethodSource("getTag")
    void testGetTag(Tag tag) throws IOException {
        storageSource.getSaver().updateTags(Collections.singleton(tag));
        String url = getFullUrl("/tags/" + tag.getName());

        TwoTuple<Integer, ResponseBodies.TagRetrieval> retrieval =
                performGetRequestJson(url, ResponseBodies.TagRetrieval.class);

        assertEquals(OK, retrieval.getFirst());
        assertEquals(GetTagCommand.OK_TAG_RETRIEVED, retrieval.getSecond().getMessage());
        assertEquals(tag, retrieval.getSecond().getTag());
    }

    // @ParameterizedTest
    @MethodSource("getRecipe")
    void testCreateRecipe(Recipe recipe) throws IOException {
        assertNotNull(recipe.getName());
        assertFalse(storageSource.getLoader().recipeNameExists(recipe.getName()));
        storageSource
                .getSaver()
                .updateUsers(
                        Collections.singleton(
                                new User.Builder()
                                        .setUsername(recipe.getAuthorUsername())
                                        .build()));
        storageSource.getSaver().updateIngredients(recipe.getRequiredIngredients().keySet());

        String url = getFullUrl("/create/recipe");
        RequestBodies.RecipeCreation body =
                new RequestBodies.RecipeCreation("", new RecipeForm(recipe));
        TwoTuple<Integer, ResponseBodies.RecipeCreation> response =
                performPostRequestJson(url, body, ResponseBodies.RecipeCreation.class);

        assertEquals(CREATED, response.getFirst());
        assertEquals(
                CreateRecipeCommand.OK_RECIPE_CREATED_WITH_GIVEN_NAME,
                response.getSecond().getMessage());
        assertEquals(Utils.fromTagSet(recipe.getTags()), response.getSecond().getCreatedTags());
        assertTrue(storageSource.getLoader().recipeNameExists(recipe.getName()));
        assertEquals(
                recipe,
                storageSource
                        .getLoader()
                        .getRecipesByNames(Collections.singletonList(recipe.getName()))
                        .get(0));
    }

    // @ParameterizedTest
    @MethodSource("getUser")
    void testCreateUser(User user) throws IOException {
        assertNotNull(user.getUsername());
        assertFalse(storageSource.getLoader().usernameExists(user.getUsername()));
        User expected =
                new User.Builder()
                        .setUsername(user.getUsername())
                        .setEmailAddress(user.getEmailAddress())
                        .build();

        String url = getFullUrl("/create/user");
        RequestBodies.UserCreation body =
                new RequestBodies.UserCreation(user.getUsername(), user.getEmailAddress());
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(CREATED, response.getFirst());
        assertEquals(CreateUserCommand.OK_USER_CREATED, response.getSecond().getMessage());
        assertTrue(storageSource.getLoader().usernameExists(user.getUsername()));
        assertEquals(
                expected,
                storageSource
                        .getLoader()
                        .getUsersByNames(Collections.singletonList(user.getUsername()))
                        .get(0));
    }

    // @ParameterizedTest
    @MethodSource("getIngredient")
    void testCreateIngredient(Ingredient ingredient) throws IOException {
        assertNotNull(ingredient.getName());
        assertFalse(storageSource.getLoader().ingredientNameExists(ingredient.getName()));

        String url = getFullUrl("/create/ingredient");
        RequestBodies.IngredientCreation body =
                new RequestBodies.IngredientCreation(
                        ingredient.getName(), ingredient.getUnits(), ingredient.getImageUri());
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(CREATED, response.getFirst());
        assertEquals(
                CreateIngredientCommand.OK_INGREDIENT_CREATED, response.getSecond().getMessage());
        assertTrue(storageSource.getLoader().ingredientNameExists(ingredient.getName()));
        assertEquals(
                ingredient,
                storageSource
                        .getLoader()
                        .getIngredientsByNames(Collections.singletonList(ingredient.getName()))
                        .get(0));
    }

    // @ParameterizedTest
    @MethodSource("getTag")
    void testCreateTag(Tag tag) throws IOException {
        assertNotNull(tag.getName());

        String url = getFullUrl("/create/tag");
        RequestBodies.TagCreation body = new RequestBodies.TagCreation(tag.getName());
        assertFalse(storageSource.getLoader().tagNameExists(tag.getName()));
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(CREATED, response.getFirst());
        assertEquals(CreateTagCommand.OK_TAG_CREATED, response.getSecond().getMessage());
        assertTrue(storageSource.getLoader().tagNameExists(tag.getName()));
        assertEquals(
                tag,
                storageSource
                        .getLoader()
                        .getTagsByNames(Collections.singletonList(tag.getName()))
                        .get(0));
    }

    // @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testBookmarkRecipe(User baseUser, Recipe recipe) throws IOException {
        User user = new User.Builder(baseUser).setSavedRecipes(Collections.emptyList()).build();
        assertNotNull(user.getUsername());
        storageSource.getSaver().updateRecipes(Collections.singleton(recipe));
        storageSource.getSaver().updateUsers(Collections.singleton(user));

        String url = getFullUrl("/bookmark/recipe");
        RequestBodies.RecipeBookmarking body =
                new RequestBodies.RecipeBookmarking("", user.getUsername(), recipe.getName());
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(OK, response.getFirst());
        assertEquals(BookmarkRecipeCommand.OK_RECIPE_BOOKMARKED, response.getSecond().getMessage());
        User savedUser =
                storageSource
                        .getLoader()
                        .getUsersByNames(Collections.singletonList(user.getUsername()))
                        .get(0);
        assertTrue(savedUser.getSavedRecipes().contains(recipe));
    }

    // @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testAddIngredientsToShoppingList(User user, Map<Ingredient, Double> ingredients)
            throws IOException {
        assertNotNull(user.getUsername());
        storageSource.getSaver().updateUsers(Collections.singleton(user));
        storageSource.getSaver().updateIngredients(ingredients.keySet());

        String url = getFullUrl("/shopping-list/add-ingredients");
        RequestBodies.IngredientToShoppingListAddition body =
                new RequestBodies.IngredientToShoppingListAddition(
                        "", user.getUsername(), Utils.fromIngredientMap(ingredients));
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(OK, response.getFirst());
        assertEquals(
                ShoppingListCommand.OK_SHOPPING_LIST_UPDATED, response.getSecond().getMessage());
        User savedUser =
                storageSource
                        .getLoader()
                        .getUsersByNames(Collections.singletonList(user.getUsername()))
                        .get(0);
        Map<Ingredient, Double> expectedShoppingList =
                Utils.addMaps(ingredients, user.getShoppingList());
        assertEquals(savedUser.getShoppingList(), expectedShoppingList);
    }

    // @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testAddRecipeToShoppingList(User user, Recipe recipe) throws IOException {
        assertNotNull(user.getUsername());
        assertNotNull(recipe.getName());
        storageSource.getSaver().updateUsers(Collections.singleton(user));
        storageSource.getSaver().updateRecipes(Collections.singleton(recipe));

        String url = getFullUrl("/shopping-list/add-recipe-ingredients");
        RequestBodies.RecipeToShoppingListAddition body =
                new RequestBodies.RecipeToShoppingListAddition(
                        "", user.getUsername(), recipe.getName(), false);
        TwoTuple<Integer, ResponseBodies.WithMessage> response =
                performPostRequestJson(url, body, ResponseBodies.WithMessage.class);

        assertEquals(OK, response.getFirst());
        assertEquals(
                ShoppingListCommand.OK_SHOPPING_LIST_UPDATED, response.getSecond().getMessage());
        User savedUser =
                storageSource
                        .getLoader()
                        .getUsersByNames(Collections.singletonList(user.getUsername()))
                        .get(0);
        Map<Ingredient, Double> expectedShoppingList =
                Utils.addMaps(recipe.getRequiredIngredients(), user.getShoppingList());
        assertEquals(savedUser.getShoppingList(), expectedShoppingList);
    }

    // @ParameterizedTest
    @MethodSource("getSearchRecipes")
    void testSearchRecipes(
            EntityStorage storage, List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected)
            throws IOException {
        storage.getSaver().updateRecipes(recipes);
        commander.setStorageSource(storage);
        Set<RecipeForm> expectedRecipeForms = new HashSet<>();
        for (Recipe r : expected) {
            expectedRecipeForms.add(new RecipeForm(r));
        }

        String query = String.join("+", tokens);
        String url = getFullUrl("/search/recipes?terms=" + query);
        TwoTuple<Integer, ResponseBodies.RecipeSearch> response =
                performGetRequestJson(url, ResponseBodies.RecipeSearch.class);

        assertEquals(OK, response.getFirst());
        String expectedMessage =
                expected.isEmpty()
                        ? SearchRecipesCommand.OK_NO_MATCHES_FOUND
                        : SearchRecipesCommand.OK_MATCHES_FOUND;
        assertEquals(expectedMessage, response.getSecond().getMessage());
        List<RecipeForm> matchesList = response.getSecond().getMatches();
        assertEquals(expected.size(), matchesList.size());
        assertNotNull(matchesList);
        Set<RecipeForm> matches = new HashSet<>(matchesList);
        assertEquals(expectedRecipeForms, matches);
    }

    // @ParameterizedTest
    @MethodSource("getSearchUsers")
    void testSearchUsers(
            EntityStorage storage, List<User> users, Set<String> tokens, Set<User> expected)
            throws IOException {
        storage.getSaver().updateUsers(users);
        commander.setStorageSource(storage);
        Set<UserForm> expectedUserForms = new HashSet<>();
        for (User r : expected) {
            expectedUserForms.add(new UserForm(r));
        }

        String query = String.join("+", tokens);
        String url = getFullUrl("/search/users?terms=" + query);
        TwoTuple<Integer, ResponseBodies.UserSearch> response =
                performGetRequestJson(url, ResponseBodies.UserSearch.class);

        assertEquals(OK, response.getFirst());
        String expectedMessage =
                expected.isEmpty()
                        ? SearchUsersCommand.OK_NO_MATCHES_FOUND
                        : SearchUsersCommand.OK_MATCHES_FOUND;
        assertEquals(expectedMessage, response.getSecond().getMessage());
        List<UserForm> matchesList = response.getSecond().getMatches();
        assertEquals(expected.size(), matchesList.size());
        assertNotNull(matchesList);
        Set<UserForm> matches = new HashSet<>(matchesList);
        assertEquals(expectedUserForms, matches);
    }

    // @ParameterizedTest
    @MethodSource("getSearchIngredients")
    void testSearchIngredients(
            EntityStorage storage,
            List<Ingredient> ingredients,
            Set<String> tokens,
            Set<Ingredient> expected)
            throws IOException {
        storage.getSaver().updateIngredients(ingredients);
        commander.setStorageSource(storage);

        String query = String.join("+", tokens);
        String url = getFullUrl("/search/ingredients?terms=" + query);
        TwoTuple<Integer, ResponseBodies.IngredientSearch> response =
                performGetRequestJson(url, ResponseBodies.IngredientSearch.class);

        assertEquals(OK, response.getFirst());
        String expectedMessage =
                expected.isEmpty()
                        ? SearchIngredientsCommand.OK_NO_MATCHES_FOUND
                        : SearchIngredientsCommand.OK_MATCHES_FOUND;
        assertEquals(expectedMessage, response.getSecond().getMessage());
        List<Ingredient> matchesList = response.getSecond().getMatches();
        assertEquals(expected.size(), matchesList.size());
        assertNotNull(matchesList);
        Set<Ingredient> matches = new HashSet<>(matchesList);
        assertEquals(expected, matches);
    }

    // @ParameterizedTest
    @MethodSource("getSearchTags")
    void testSearchTags(
            EntityStorage storage, List<Tag> tags, Set<String> tokens, Set<Tag> expected)
            throws IOException {
        storage.getSaver().updateTags(tags);
        commander.setStorageSource(storage);

        String query = String.join("+", tokens);
        String url = getFullUrl("/search/tags?terms=" + query);
        TwoTuple<Integer, ResponseBodies.TagSearch> response =
                performGetRequestJson(url, ResponseBodies.TagSearch.class);

        assertEquals(OK, response.getFirst());
        String expectedMessage =
                expected.isEmpty()
                        ? SearchTagsCommand.OK_NO_MATCHES_FOUND
                        : SearchTagsCommand.OK_MATCHES_FOUND;
        assertEquals(expectedMessage, response.getSecond().getMessage());
        List<Tag> matchesList = response.getSecond().getMatches();
        assertEquals(expected.size(), matchesList.size());
        assertNotNull(matchesList);
        Set<Tag> matches = new HashSet<>(matchesList);
        assertEquals(expected, matches);
    }

    private static class ModifiableCommander extends EntityCommander {
        private EntityStorage storage;

        public ModifiableCommander(@NotNull EntityStorage storage) {
            super(storage);
            setStorageSource(storage);
        }

        void setStorageSource(EntityStorage storage) {
            this.storage = storage;
        }

        @Override
        @NotNull public EntityStorage getStorageSource() {
            return storage;
        }
    }
}
