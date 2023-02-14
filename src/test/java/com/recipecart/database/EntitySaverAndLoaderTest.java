/* (C)2023 */
package com.recipecart.database;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EntitySaverAndLoaderTest {

    static Stream<Arguments> listTagParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListTagNoNulls);
    }

    static Stream<Arguments> listIngredientParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListIngredientNoNulls);
    }

    static Stream<Arguments> listRecipeParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListRecipeNoNulls);
    }

    static Stream<Arguments> listUserParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListUserNoNulls);
    }

    static Stream<Arguments> nullableCollectionTagParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListTagWithNulls);
    }

    static Stream<Arguments> nullableCollectionIngredientParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListIngredientWithNulls);
    }

    static Stream<Arguments> nullableCollectionRecipeParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListRecipeWithNulls);
    }

    static Stream<Arguments> nullableCollectionUserParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListUserWithNulls);
    }

    static Stream<Arguments> nullableListStringParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListStringWithNulls);
    }

    static Stream<Arguments> nullableSetStringParams() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getSetStringWithNulls);
    }

    static Stream<Arguments> listTagParamsSomeInvalid() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListTagSomeInvalid);
    }

    static Stream<Arguments> listIngredientParamsSomeInvalid() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListIngredientSomeInvalid);
    }

    static Stream<Arguments> listRecipeParamsSomeInvalid() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListRecipeSomeInvalid);
    }

    static Stream<Arguments> listUserParamsSomeInvalid() {
        return generateArgumentsWithStorage(
                getStorageArrayGenerators(), TestData::getListUserSomeInvalid);
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
            Function<U, T> getEntityFromId) {
        assertEquals(allEntities.size(), allIds.size());
        assertEquals(includedEntities.size(), includedIds.size());
        for (int i = 0; i < allEntities.size(); i++) {
            T entity = allEntities.get(i);
            U id = allIds.get(i);

            assertEquals(includedEntities.contains(entity), includedIds.contains(id));

            if (includedEntities.contains(entity)) {
                assertEquals(entity, getEntityFromId.apply(id));
            } else {
                assertThrows(CustomUncheckedException.class, () -> getEntityFromId.apply(id));
            }
        }
    }

    @ParameterizedTest
    @MethodSource("listTagParams")
    void testSaveLoadTags(EntityStorage storage, @NotNull List<@NotNull Tag> saveTags)
            throws IOException {
        storage.getSaver().updateTags(saveTags);

        List<Tag> allTags = TestUtils.convertToTypedList(TestData.getTags());
        testProperIdsIncluded(allTags, saveTags, Tag::getName, storage.getLoader()::tagNameExists);

        List<String> saveNames = functionOutputForEach(saveTags, Tag::getName);
        List<String> allNames = functionOutputForEach(allTags, Tag::getName);
        testCorrectEntitiesGottenFromIds(
                allTags,
                saveTags,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return storage.getLoader()
                                .getTagsByNames(Collections.singletonList(name))
                                .get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveTags, storage.getLoader().getTagsByNames(saveNames));
        if (!saveTags.containsAll(allTags)) {
            assertThrows(IOException.class, () -> storage.getLoader().getTagsByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listIngredientParams")
    void testSaveLoadIngredients(
            EntityStorage storage, @NotNull List<@NotNull Ingredient> saveIngredients)
            throws IOException {
        storage.getSaver().updateIngredients(saveIngredients);

        List<Ingredient> allIngredients = TestUtils.convertToTypedList(TestData.getIngredients());
        testProperIdsIncluded(
                allIngredients,
                saveIngredients,
                Ingredient::getName,
                storage.getLoader()::ingredientNameExists);

        List<String> saveNames = functionOutputForEach(saveIngredients, Ingredient::getName);
        List<String> allNames = functionOutputForEach(allIngredients, Ingredient::getName);
        testCorrectEntitiesGottenFromIds(
                allIngredients,
                saveIngredients,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return storage.getLoader()
                                .getIngredientsByNames(Collections.singletonList(name))
                                .get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveIngredients, storage.getLoader().getIngredientsByNames(saveNames));
        if (!saveIngredients.containsAll(allIngredients)) {
            assertThrows(
                    IOException.class, () -> storage.getLoader().getIngredientsByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listRecipeParams")
    void testSaveLoadRecipes(EntityStorage storage, @NotNull List<@NotNull Recipe> saveRecipes)
            throws IOException {
        storage.getSaver().updateRecipes(saveRecipes);

        List<Recipe> allRecipes = TestUtils.convertToTypedList(TestData.getRecipes());
        testProperIdsIncluded(
                allRecipes, saveRecipes, Recipe::getName, storage.getLoader()::recipeNameExists);

        List<String> saveNames = functionOutputForEach(saveRecipes, Recipe::getName);
        List<String> allNames = functionOutputForEach(allRecipes, Recipe::getName);
        testCorrectEntitiesGottenFromIds(
                allRecipes,
                saveRecipes,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return storage.getLoader()
                                .getRecipesByNames(Collections.singletonList(name))
                                .get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveRecipes, storage.getLoader().getRecipesByNames(saveNames));
        if (!saveRecipes.containsAll(allRecipes)) {
            assertThrows(IOException.class, () -> storage.getLoader().getRecipesByNames(allNames));
        }
    }

    @ParameterizedTest
    @MethodSource("listUserParams")
    void testSaveLoadUsers(EntityStorage storage, @NotNull List<@NotNull User> saveUsers)
            throws IOException {
        storage.getSaver().updateUsers(saveUsers);

        List<User> allUsers = TestUtils.convertToTypedList(TestData.getUsers());
        testProperIdsIncluded(
                allUsers, saveUsers, User::getUsername, storage.getLoader()::usernameExists);

        List<String> saveNames = functionOutputForEach(saveUsers, User::getUsername);
        List<String> allNames = functionOutputForEach(allUsers, User::getUsername);
        testCorrectEntitiesGottenFromIds(
                allUsers,
                saveUsers,
                allNames,
                saveNames,
                (String name) -> {
                    try {
                        return storage.getLoader()
                                .getUsersByNames(Collections.singletonList(name))
                                .get(0);
                    } catch (IOException e) {
                        throw new CustomUncheckedException("Name not found ", e);
                    }
                });

        assertEquals(saveUsers, storage.getLoader().getUsersByNames(saveNames));
        if (!saveUsers.containsAll(allUsers)) {
            assertThrows(IOException.class, () -> storage.getLoader().getUsersByNames(allNames));
        }
    }

    private static Stream<Arguments> getSearchTags() {
        return TestUtils.getSearchTags(getStorageGenerators());
    }

    private static Stream<Arguments> getSearchIngredients() {
        return TestUtils.getSearchIngredients(getStorageGenerators());
    }

    private static Stream<Arguments> getSearchRecipes() {
        return TestUtils.getSearchRecipes(getStorageGenerators());
    }

    private static Stream<Arguments> getSearchUsers() {
        return TestUtils.getSearchUsers(getStorageGenerators());
    }

    @ParameterizedTest
    @MethodSource("getSearchTags")
    void testSearchForTag(
            EntityStorage storage, List<Tag> tags, Set<String> tokens, Set<Tag> expected) {
        storage.getSaver().updateTags(tags);

        assertEquals(expected, storage.getLoader().searchTags(tokens));
    }

    @ParameterizedTest
    @MethodSource("getSearchIngredients")
    void testSearchForIngredient(
            EntityStorage storage,
            List<Ingredient> ingredients,
            Set<String> tokens,
            Set<Ingredient> expected) {
        storage.getSaver().updateIngredients(ingredients);
        assertEquals(expected, storage.getLoader().searchIngredients(tokens));
    }

    @ParameterizedTest
    @MethodSource("getSearchRecipes")
    void testSearchForRecipe(
            EntityStorage storage, List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected) {
        storage.getSaver().updateRecipes(recipes);
        assertEquals(expected, storage.getLoader().searchRecipes(tokens));
    }

    @ParameterizedTest
    @MethodSource("getSearchUsers")
    void testSearchForUser(
            EntityStorage storage, List<User> users, Set<String> tokens, Set<User> expected) {
        storage.getSaver().updateUsers(users);
        assertEquals(expected, storage.getLoader().searchUsers(tokens));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionTagParams")
    void testSaveTagsNullCheck(EntityStorage storage, @Nullable Collection<@Nullable Tag> tags) {
        assertThrows(NullPointerException.class, () -> storage.getSaver().updateTags(tags));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionIngredientParams")
    void testSaveIngredientNullCheck(
            EntityStorage storage, @Nullable Collection<@Nullable Ingredient> ingredients) {
        assertThrows(
                NullPointerException.class,
                () -> storage.getSaver().updateIngredients(ingredients));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionRecipeParams")
    void testSaveRecipeNullCheck(
            EntityStorage storage, @Nullable Collection<@Nullable Recipe> recipes) {
        assertThrows(NullPointerException.class, () -> storage.getSaver().updateRecipes(recipes));
    }

    @ParameterizedTest
    @MethodSource("nullableCollectionUserParams")
    void testSaveUserNullCheck(EntityStorage storage, @Nullable Collection<@Nullable User> users) {
        assertThrows(NullPointerException.class, () -> storage.getSaver().updateUsers(users));
    }

    @ParameterizedTest
    @MethodSource("nullableListStringParams")
    void testGetEntityByIdNullCheck(EntityStorage storage, @Nullable List<@Nullable String> ids) {
        assertThrows(NullPointerException.class, () -> storage.getLoader().getTagsByNames(ids));
        assertThrows(
                NullPointerException.class, () -> storage.getLoader().getIngredientsByNames(ids));
        assertThrows(NullPointerException.class, () -> storage.getLoader().getRecipesByNames(ids));
        assertThrows(NullPointerException.class, () -> storage.getLoader().getUsersByNames(ids));
    }

    @ParameterizedTest
    @MethodSource("nullableSetStringParams")
    void testSearchEntityNullCheck(EntityStorage storage, @Nullable Set<@Nullable String> tokens) {
        assertThrows(NullPointerException.class, () -> storage.getLoader().searchTags(tokens));
        assertThrows(
                NullPointerException.class, () -> storage.getLoader().searchIngredients(tokens));
        assertThrows(NullPointerException.class, () -> storage.getLoader().searchRecipes(tokens));
        assertThrows(NullPointerException.class, () -> storage.getLoader().searchUsers(tokens));
    }

    private static Stream<Arguments> getStorageParams() {
        return TestUtils.getStorageParams(getStorageGenerators());
    }

    @ParameterizedTest
    @MethodSource("getStorageParams")
    void testEntityExistsNullCheck(EntityStorage storage) {
        assertThrows(NullPointerException.class, () -> storage.getLoader().tagNameExists(null));
        assertThrows(
                NullPointerException.class, () -> storage.getLoader().ingredientNameExists(null));
        assertThrows(NullPointerException.class, () -> storage.getLoader().recipeNameExists(null));
        assertThrows(NullPointerException.class, () -> storage.getLoader().usernameExists(null));
    }

    @ParameterizedTest
    @MethodSource("listTagParamsSomeInvalid")
    void testSaveTagsSomeInvalid(EntityStorage storage, List<Tag> tags) {
        assertThrows(IllegalArgumentException.class, () -> storage.getSaver().updateTags(tags));
    }

    @ParameterizedTest
    @MethodSource("listIngredientParamsSomeInvalid")
    void testSaveIngredientsSomeInvalid(EntityStorage storage, List<Ingredient> ingredients) {
        assertThrows(
                IllegalArgumentException.class,
                () -> storage.getSaver().updateIngredients(ingredients));
    }

    @ParameterizedTest
    @MethodSource("listRecipeParamsSomeInvalid")
    void testSaveRecipesSomeInvalid(EntityStorage storage, List<Recipe> recipes) {
        assertThrows(
                IllegalArgumentException.class, () -> storage.getSaver().updateRecipes(recipes));
    }

    @ParameterizedTest
    @MethodSource("listUserParamsSomeInvalid")
    void testSaveUsersSomeInvalid(EntityStorage storage, List<User> users) {
        assertThrows(IllegalArgumentException.class, () -> storage.getSaver().updateUsers(users));
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
