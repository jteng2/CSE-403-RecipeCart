/* (C)2023 */
package com.recipecart.database;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import com.recipecart.utils.TwoTuple;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class EntitySaverAndLoaderTest {

    static Stream<Arguments> getStorageParams() {
        Stream.Builder<Arguments> argumentsBuilder = Stream.builder();
        for (Supplier<EntityStorage> storageGenerator : getStorageGenerators()) {
            argumentsBuilder.add(Arguments.of(storageGenerator.get()));
        }
        return argumentsBuilder.build();
    }

    // Uncomment the lines in the following two functions once MongoEntitySaver/Loader is being
    // implemented. Also uncomment lines in TestDataTest.java that contain
    // TestDataTest::getMongoEntityStorages.

    private static List<Supplier<EntityStorage>> getStorageGenerators() {
        return List.of(
                // () -> {
                //    ServerAddress databaseAddress = TestUtils.getTestDatabaseAddress();
                //    return new EntityStorage(
                //            new MongoEntitySaver(databaseAddress),
                //            new MongoEntityLoader(databaseAddress));
                // },
                () -> {
                    MockEntitySaveAndLoader saverAndLoader = new MockEntitySaveAndLoader();
                    return new EntityStorage(saverAndLoader, saverAndLoader);
                });
    }

    private static List<Supplier<Object[]>> getStorageArrayGenerators() {
        return List.of(
                // TestData::getMongoEntityStorages,
                TestData::getMockEntityStorages);
    }

    private static Stream<Arguments> generateArgumentsWithStorage(Supplier<Object[]> generator) {
        List<Supplier<Object[]>> storageGenerators = getStorageArrayGenerators();

        Stream<Arguments> concatenatedArgs = null;
        for (Supplier<Object[]> storageGenerator : storageGenerators) {
            Stream<Arguments> toConcatenate =
                    generateMultiArguments(List.of(storageGenerator, generator), 1, true);
            concatenatedArgs =
                    concatenatedArgs == null
                            ? toConcatenate
                            : Stream.concat(concatenatedArgs, toConcatenate);
        }

        return concatenatedArgs;
    }

    static Stream<Arguments> listTagParams() {
        return generateArgumentsWithStorage(TestData::getListTagNoNulls);
    }

    static Stream<Arguments> listIngredientParams() {
        return generateArgumentsWithStorage(TestData::getListIngredientNoNulls);
    }

    static Stream<Arguments> listRecipeParams() {
        return generateArgumentsWithStorage(TestData::getListRecipeNoNulls);
    }

    static Stream<Arguments> listUserParams() {
        return generateArgumentsWithStorage(TestData::getListUserNoNulls);
    }

    static Stream<Arguments> nullableCollectionTagParams() {
        return generateArgumentsWithStorage(TestData::getListTagWithNulls);
    }

    static Stream<Arguments> nullableCollectionIngredientParams() {
        return generateArgumentsWithStorage(TestData::getListIngredientWithNulls);
    }

    static Stream<Arguments> nullableCollectionRecipeParams() {
        return generateArgumentsWithStorage(TestData::getListRecipeWithNulls);
    }

    static Stream<Arguments> nullableCollectionUserParams() {
        return generateArgumentsWithStorage(TestData::getListUserWithNulls);
    }

    static Stream<Arguments> nullableListStringParams() {
        return generateArgumentsWithStorage(TestData::getListStringWithNulls);
    }

    static Stream<Arguments> nullableSetStringParams() {
        return generateArgumentsWithStorage(TestData::getSetStringWithNulls);
    }

    static Stream<Arguments> listTagParamsSomeInvalid() {
        return generateArgumentsWithStorage(TestData::getListTagSomeInvalid);
    }

    static Stream<Arguments> listIngredientParamsSomeInvalid() {
        return generateArgumentsWithStorage(TestData::getListIngredientSomeInvalid);
    }

    static Stream<Arguments> listRecipeParamsSomeInvalid() {
        return generateArgumentsWithStorage(TestData::getListRecipeSomeInvalid);
    }

    static Stream<Arguments> listUserParamsSomeInvalid() {
        return generateArgumentsWithStorage(TestData::getListUserSomeInvalid);
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
        for (Supplier<EntityStorage> storageGenerator : getStorageGenerators()) {
            for (int i = 0; i < expectedEntitySets.size(); i++) {
                paramsListsBuilder.add(
                        Arguments.of(
                                storageGenerator.get(),
                                entities,
                                tokenSets.get(i),
                                expectedEntitySets.get(i)));
            }
        }
        // return withStorage(paramsListsBuilder.build());
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
    void testSearchForTag(
            EntityStorage storage, List<Tag> tags, Set<String> tokens, Set<Tag> expected) {
        storage.getSaver().updateTags(tags);

        assertEquals(expected, storage.getLoader().searchTags(tokens));
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
            EntityStorage storage,
            List<Ingredient> ingredients,
            Set<String> tokens,
            Set<Ingredient> expected) {
        storage.getSaver().updateIngredients(ingredients);
        assertEquals(expected, storage.getLoader().searchIngredients(tokens));
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
    void testSearchForRecipe(
            EntityStorage storage, List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected) {
        storage.getSaver().updateRecipes(recipes);
        assertEquals(expected, storage.getLoader().searchRecipes(tokens));
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
