/* (C)2023 */
package com.recipecart.testutil;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
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

    @BeforeEach
    void initSaverAndLoader() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        saver = saveAndLoader;
        loader = saveAndLoader;
    }

    // for all uses of this method, each Object in things is of type T
    @SuppressWarnings("unchecked")
    private static <T> List<T> convertToTypedList(Object[] things) {
        List<T> list = new ArrayList<>();
        for (Object o : things) {
            list.add((T) o);
        }
        return list;
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

        List<Tag> allTags = convertToTypedList(TestData.getTags());
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

        List<Ingredient> allIngredients = convertToTypedList(TestData.getIngredients());
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

        List<Recipe> allRecipes = convertToTypedList(TestData.getRecipes());
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

        List<User> allUsers = convertToTypedList(TestData.getUsers());
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

    // This unchecked exception exists so that Function objects can have functions that
    // throw checked exceptions. Those expected exceptions are caught, this exception is thrown,
    // and corresponding assertThrows checks for this exception being thrown.
    private static class CustomUncheckedException extends RuntimeException {
        CustomUncheckedException(String errorMsg, Throwable err) {
            super(errorMsg, err);
        }
    }
}
