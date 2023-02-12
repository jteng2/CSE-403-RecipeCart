/* (C)2023 */
package com.recipecart.testutil;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import com.recipecart.utils.TwoTuple;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class MockEntitySaveAndLoaderTest {
    EntitySaver saver;
    EntityLoader loader;

    static Stream<Arguments> listTagParams() {
        return generateArguments(TestData::getListTagNoNulls);
    }

    static Stream<Arguments> listIngredientParams() {
        return generateArguments(TestData::getListIngredientNoNulls);
    }

    static Stream<Arguments> listRecipeParams() {
        return generateArguments(TestData::getListRecipeNoNulls);
    }

    static Stream<Arguments> listUserParams() {
        return generateArguments(TestData::getListUserNoNulls);
    }

    static Stream<Arguments> nullableCollectionTagParams() {
        return generateArguments(TestData::getListTagWithNulls);
    }

    static Stream<Arguments> nullableCollectionIngredientParams() {
        return generateArguments(TestData::getListIngredientWithNulls);
    }

    static Stream<Arguments> nullableCollectionRecipeParams() {
        return generateArguments(TestData::getListRecipeWithNulls);
    }

    static Stream<Arguments> nullableCollectionUserParams() {
        return generateArguments(TestData::getListUserWithNulls);
    }

    static Stream<Arguments> nullableListStringParams() {
        return generateArguments(TestData::getListStringWithNulls);
    }

    static Stream<Arguments> nullableSetStringParams() {
        return generateArguments(TestData::getSetStringWithNulls);
    }

    static Stream<Arguments> listTagParamsSomeInvalid() {
        return generateArguments(TestData::getListTagSomeInvalid);
    }

    static Stream<Arguments> listIngredientParamsSomeInvalid() {
        return generateArguments(TestData::getListIngredientSomeInvalid);
    }

    static Stream<Arguments> listRecipeParamsSomeInvalid() {
        return generateArguments(TestData::getListRecipeSomeInvalid);
    }

    static Stream<Arguments> listUserParamsSomeInvalid() {
        return generateArguments(TestData::getListUserSomeInvalid);
    }

    @BeforeEach
    void initSaverAndLoader() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        saver = saveAndLoader;
        loader = saveAndLoader;
    }

    private static <T, U> List<T> functionOutputForEach(List<U> source, Function<U, T> toApply) {
        List<T> outputs = new ArrayList<>();
        for (U elem : source) {
            outputs.add(toApply.apply(elem));
        }
        return outputs;
    }

    // Where the entity "id" is the "name" for Tag/Ingredient/Recipe and "username" for User,
    // and the "idIncludedChecker" is the (tag/ingredient/etc)NameExists method for EntityLoader
    private static <T, U> void testProperIdsIncluded(
            List<T> allEntities,
            List<T> includedEntities,
            Function<T, U> idGetter,
            Function<U, Boolean> idIncludedChecker) {
        for (T entity : allEntities) {
            U id = idGetter.apply(entity);
            assertNotNull(id);
            assertEquals(includedEntities.contains(entity), idIncludedChecker.apply(id));
        }
    }

    private static <T, U> void testCorrectEntitiesGottenFromIds(
            List<T> allEntities,
            List<T> includedEntities,
            List<U> allIds,
            List<U> includedIds,
            Function<U, T> getTFromId) {
        assertEquals(allEntities.size(), allIds.size());
        assertEquals(includedEntities.size(), includedIds.size());
        for (int i = 0; i < allEntities.size(); i++) {
            T entity = allEntities.get(i);
            U id = allIds.get(i);

            assertEquals(includedEntities.contains(entity), includedIds.contains(id));

            if (includedEntities.contains(entity)) {
                assertEquals(entity, getTFromId.apply(id));
            } else {
                assertThrows(CustomUncheckedException.class, () -> getTFromId.apply(id));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("listTagParams")
    void testSaveLoadTags(@NotNull List<@NotNull Tag> saveTags) throws IOException {
        saver.updateTags(saveTags);

        List<Tag> allTags = TestUtils.convertToTypedList(TestData.getTags());
        testProperIdsIncluded(allTags, saveTags, Tag::getName, loader::tagNameExists);

        List<String> saveNames = functionOutputForEach(saveTags, Tag::getName);
        List<String> allNames = functionOutputForEach(allTags, Tag::getName);
        testCorrectEntitiesGottenFromIds(
                allTags,
                saveTags,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return loader.getTagsByNames(Collections.singletonList(name)).get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveTags, loader.getTagsByNames(saveNames));
        if (!saveTags.containsAll(allTags)) {
            assertThrows(IOException.class, () -> loader.getTagsByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listIngredientParams")
    void testSaveLoadIngredients(@NotNull List<@NotNull Ingredient> saveIngredients)
            throws IOException {
        saver.updateIngredients(saveIngredients);

        List<Ingredient> allIngredients = TestUtils.convertToTypedList(TestData.getIngredients());
        testProperIdsIncluded(
                allIngredients, saveIngredients, Ingredient::getName, loader::ingredientNameExists);

        List<String> saveNames = functionOutputForEach(saveIngredients, Ingredient::getName);
        List<String> allNames = functionOutputForEach(allIngredients, Ingredient::getName);
        testCorrectEntitiesGottenFromIds(
                allIngredients,
                saveIngredients,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return loader.getIngredientsByNames(Collections.singletonList(name)).get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveIngredients, loader.getIngredientsByNames(saveNames));
        if (!saveIngredients.containsAll(allIngredients)) {
            assertThrows(IOException.class, () -> loader.getIngredientsByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listRecipeParams")
    void testSaveLoadRecipes(@NotNull List<@NotNull Recipe> saveRecipes) throws IOException {
        saver.updateRecipes(saveRecipes);

        List<Recipe> allRecipes = TestUtils.convertToTypedList(TestData.getRecipes());
        testProperIdsIncluded(allRecipes, saveRecipes, Recipe::getName, loader::recipeNameExists);

        List<String> saveNames = functionOutputForEach(saveRecipes, Recipe::getName);
        List<String> allNames = functionOutputForEach(allRecipes, Recipe::getName);
        testCorrectEntitiesGottenFromIds(
                allRecipes,
                saveRecipes,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return loader.getRecipesByNames(Collections.singletonList(name)).get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveRecipes, loader.getRecipesByNames(saveNames));
        if (!saveRecipes.containsAll(allRecipes)) {
            assertThrows(IOException.class, () -> loader.getRecipesByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listUserParams")
    void testSaveLoadUsers(@NotNull List<@NotNull User> saveUsers) throws IOException {
        saver.updateUsers(saveUsers);

        List<User> allUsers = TestUtils.convertToTypedList(TestData.getUsers());
        testProperIdsIncluded(allUsers, saveUsers, User::getUsername, loader::usernameExists);

        List<String> saveNames = functionOutputForEach(saveUsers, User::getUsername);
        List<String> allNames = functionOutputForEach(allUsers, User::getUsername);
        testCorrectEntitiesGottenFromIds(
                allUsers,
                saveUsers,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return loader.getUsersByNames(Collections.singletonList(name)).get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveUsers, loader.getUsersByNames(saveNames));
        if (!saveUsers.containsAll(allUsers)) {
            assertThrows(IOException.class, () -> loader.getUsersByNames(allNames));
        }
    }

    private static List<String> getEntityNames() {
        return List.of("Chicken Adobo", "hella veggies", "null", "Mangoes", "Deleted User");
    }

    private static List<String> getPresentationNames() {
        return List.of(
                "Tasty Chicken Adobo with Rice",
                "Healthy Hearty Veggie Dish",
                "Address zero",
                "My favorite fruit",
                "");
    }

    private static List<Set<String>> getTokenSets() {
        return List.of(
                Set.of("Healthy", "Chicken"),
                Set.of("veggies", "fruit"),
                Set.of("adobo", "Null", "Mangoes", "user", "withrice", "Tasty", "address"),
                Set.of(),
                Set.of("Mangos, hellaveggies", "deleted", "zero"));
    }

    private static List<Set<String>> getExpectedEntityNameSets() {
        return List.of(
                Set.of("Chicken Adobo"),
                Set.of("hella veggies"),
                Set.of("Chicken Adobo", "null", "Mangoes", "Deleted User"),
                Set.of(),
                Set.of("Deleted User"));
    }

    private static List<Set<String>> getExpectedRecipeNameSets() {
        return List.of(
                Set.of("Chicken Adobo", "hella veggies"),
                Set.of("hella veggies", "Mangoes"),
                Set.of("Chicken Adobo", "null", "Mangoes", "Deleted User"),
                Set.of(),
                Set.of("Deleted User", "null"));
    }

    private static <T> Stream<Arguments> getSearchTestParams(
            List<T> entitiesOriginal,
            List<List<String>> newEntityNameLists,
            List<Set<String>> tokenSets,
            List<Set<String>> expectedEntityNameSets,
            List<Function<TwoTuple<T, String>, T>> entityRenamers) {
        assertNotEquals(0, newEntityNameLists.size());
        assertEquals(newEntityNameLists.size(), entityRenamers.size());
        for (List<String> newEntityNames : newEntityNameLists) {
            assertEquals(entitiesOriginal.size(), newEntityNames.size());
        }
        assertEquals(tokenSets.size(), expectedEntityNameSets.size());

        List<Set<T>> expectedEntitySets = new ArrayList<>();
        for (int i = 0; i < expectedEntityNameSets.size(); i++) {
            expectedEntitySets.add(new HashSet<>());
        }

        List<T> entities = new ArrayList<>();
        for (int i = 0; i < entitiesOriginal.size(); i++) {
            T newEntity = entitiesOriginal.get(i);
            for (int j = 0; j < newEntityNameLists.size(); j++) {
                String rename = newEntityNameLists.get(j).get(i);
                newEntity = entityRenamers.get(j).apply(new TwoTuple<>(newEntity, rename));
            }
            entities.add(newEntity);

            // element 0 of newEntityNameLists reserved for the "unique" names of the entities
            for (int j = 0; j < expectedEntitySets.size(); j++) {
                String newName = newEntityNameLists.get(0).get(i);
                if (expectedEntityNameSets.get(j).contains(newName)) {
                    expectedEntitySets.get(j).add(newEntity);
                }
            }
        }

        Stream.Builder<Arguments> paramsListsBuilder = Stream.builder();
        for (int i = 0; i < expectedEntitySets.size(); i++) {
            paramsListsBuilder.add(
                    Arguments.of(entities, tokenSets.get(i), expectedEntitySets.get(i)));
        }
        return paramsListsBuilder.build();
    }

    private static <T> Stream<Arguments> getSearchTestParams(
            List<T> entitiesOriginal,
            List<String> newEntityNames,
            List<Set<String>> tokenSets,
            List<Set<String>> expectedEntityNameSets,
            Function<TwoTuple<T, String>, T> renameEntity) {
        return getSearchTestParams(
                entitiesOriginal,
                Collections.singletonList(newEntityNames),
                tokenSets,
                expectedEntityNameSets,
                Collections.singletonList(renameEntity));
    }

    private static Stream<Arguments> getSearchTags() {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getTags()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (tagAndString) -> new Tag(tagAndString.getSecond()));
    }

    @ParameterizedTest
    @MethodSource("getSearchTags")
    void testSearchForTag(List<Tag> tags, Set<String> tokens, Set<Tag> expected) {
        saver.updateTags(tags);

        assertEquals(expected, loader.searchTags(tokens));
    }

    private static Ingredient renameIngredient(Ingredient ingredient, String toRename) {
        return new Ingredient(toRename, ingredient.getUnits(), ingredient.getImageUri());
    }

    private static Stream<Arguments> getSearchIngredients() {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getIngredients()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (TwoTuple<Ingredient, String> ingredientAndString) ->
                        renameIngredient(
                                ingredientAndString.getFirst(), ingredientAndString.getSecond()));
    }

    @ParameterizedTest
    @MethodSource("getSearchIngredients")
    void testSearchForIngredient(
            List<Ingredient> ingredients, Set<String> tokens, Set<Ingredient> expected) {
        saver.updateIngredients(ingredients);
        assertEquals(expected, loader.searchIngredients(tokens));
    }

    private static Recipe renameRecipe(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setName(toRename).build();
    }

    private static Recipe renameRecipePresentationName(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setPresentationName(toRename).build();
    }

    private static Stream<Arguments> getSearchRecipes() {
        Function<TwoTuple<Recipe, String>, Recipe> recipeRenamer =
                (TwoTuple<Recipe, String> recipeAndString) ->
                        renameRecipe(recipeAndString.getFirst(), recipeAndString.getSecond());
        Function<TwoTuple<Recipe, String>, Recipe> recipePresentationRenamer =
                (TwoTuple<Recipe, String> recipeAndString) ->
                        renameRecipePresentationName(
                                recipeAndString.getFirst(), recipeAndString.getSecond());

        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getRecipes()),
                List.of(getEntityNames(), getPresentationNames()),
                getTokenSets(),
                getExpectedRecipeNameSets(),
                List.of(recipeRenamer, recipePresentationRenamer));
    }

    @ParameterizedTest
    @MethodSource("getSearchRecipes")
    void testSearchForRecipe(List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected) {
        saver.updateRecipes(recipes);
        assertEquals(expected, loader.searchRecipes(tokens));
    }

    private static User renameUser(User User, String toRename) {
        return new User.Builder(User).setUsername(toRename).build();
    }

    private static Stream<Arguments> getSearchUsers() {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getUsers()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (TwoTuple<User, String> userAndString) ->
                        renameUser(userAndString.getFirst(), userAndString.getSecond()));
    }

    @ParameterizedTest
    @MethodSource("getSearchUsers")
    void testSearchForUser(List<User> users, Set<String> tokens, Set<User> expected) {
        saver.updateUsers(users);
        assertEquals(expected, loader.searchUsers(tokens));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionTagParams")
    void testSaveTagsNullCheck(@Nullable Collection<@Nullable Tag> tags) {
        assertThrows(NullPointerException.class, () -> saver.updateTags(tags));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionIngredientParams")
    void testSaveIngredientNullCheck(@Nullable Collection<@Nullable Ingredient> ingredients) {
        assertThrows(NullPointerException.class, () -> saver.updateIngredients(ingredients));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionRecipeParams")
    void testSaveRecipeNullCheck(@Nullable Collection<@Nullable Recipe> recipes) {
        assertThrows(NullPointerException.class, () -> saver.updateRecipes(recipes));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionUserParams")
    void testSaveUserNullCheck(@Nullable Collection<@Nullable User> users) {
        assertThrows(NullPointerException.class, () -> saver.updateUsers(users));
    }

    @ParameterizedTest
    @MethodSource("nullableListStringParams")
    void testGetEntityByIdNullCheck(@Nullable List<@Nullable String> ids) {
        assertThrows(NullPointerException.class, () -> loader.getTagsByNames(ids));
        assertThrows(NullPointerException.class, () -> loader.getIngredientsByNames(ids));
        assertThrows(NullPointerException.class, () -> loader.getRecipesByNames(ids));
        assertThrows(NullPointerException.class, () -> loader.getUsersByNames(ids));
    }

    @ParameterizedTest
    @MethodSource("nullableSetStringParams")
    void testSearchEntityNullCheck(@Nullable Set<@Nullable String> tokens) {
        assertThrows(NullPointerException.class, () -> loader.searchTags(tokens));
        assertThrows(NullPointerException.class, () -> loader.searchIngredients(tokens));
        assertThrows(NullPointerException.class, () -> loader.searchRecipes(tokens));
        assertThrows(NullPointerException.class, () -> loader.searchUsers(tokens));
    }

    @Test
    void testEntityExistsNullCheck() {
        assertThrows(NullPointerException.class, () -> loader.tagNameExists(null));
        assertThrows(NullPointerException.class, () -> loader.ingredientNameExists(null));
        assertThrows(NullPointerException.class, () -> loader.recipeNameExists(null));
        assertThrows(NullPointerException.class, () -> loader.usernameExists(null));
    }

    @ParameterizedTest
    @MethodSource("listTagParamsSomeInvalid")
    void testSaveTagsSomeInvalid(List<Tag> tags) {
        assertThrows(IllegalArgumentException.class, () -> saver.updateTags(tags));
    }

    @ParameterizedTest
    @MethodSource("listIngredientParamsSomeInvalid")
    void testSaveIngredientsSomeInvalid(List<Ingredient> ingredients) {
        assertThrows(IllegalArgumentException.class, () -> saver.updateIngredients(ingredients));
    }

    @ParameterizedTest
    @MethodSource("listRecipeParamsSomeInvalid")
    void testSaveRecipesSomeInvalid(List<Recipe> recipes) {
        assertThrows(IllegalArgumentException.class, () -> saver.updateRecipes(recipes));
    }

    @ParameterizedTest
    @MethodSource("listUserParamsSomeInvalid")
    void testSaveUsersSomeInvalid(List<User> users) {
        assertThrows(IllegalArgumentException.class, () -> saver.updateUsers(users));
    }

    // This unchecked exception exists so that Function objects can have functions that
    // throw checked exceptions. Those expected exceptions are caught, this exception is thrown,
    // and corresponding assertThrows checks for this exception being thrown.
    private static class CustomUncheckedException extends RuntimeException {
        CustomUncheckedException(String errorMsg, Throwable err) {
            super(errorMsg, err);
        }
    }
}
