/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;
import static com.recipecart.usecases.CreateRecipeCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CreateRecipeCommandTest {
    private static Stream<Arguments> getRecipe() {
        return generateArguments(TestData::getRecipes);
    }

    private static Stream<Arguments> getStorageWithRecipe() {
        return generateArgumentsWithStorage(getMockStorageArrayGenerators(), TestData::getRecipes);
    }

    private static Stream<Arguments> getStorageWithString() {
        return generateArgumentsWithStorage(
                getMockStorageArrayGenerators(), TestData::getNotNullStrings);
    }

    private static Stream<Arguments> getStorageWithRecipeNonEmptyDataStructures() {
        return generateArgumentsWithStorage(
                getMockStorageArrayGenerators(), TestData::getRecipesNonEmptyDataStructures);
    }

    private static void setupStorage(
            Recipe recipe,
            EntityStorage storage,
            boolean setupIngredients,
            boolean setupTags,
            boolean setupUser) {
        if (setupIngredients) {
            storage.getSaver().updateIngredients(recipe.getRequiredIngredients().keySet());
        }
        if (setupTags) {
            storage.getSaver().updateTags(recipe.getTags());
        }
        if (setupUser && recipe.getAuthorUsername() != null) {
            User author = new User.Builder().setUsername(recipe.getAuthorUsername()).build();
            storage.getSaver().updateUsers(Collections.singletonList(author));
        }
    }

    private static void setupStorage(Recipe recipe, EntityStorage storage) {
        setupStorage(recipe, storage, true, true, true);
    }

    private static CreateRecipeCommand createAndExecuteCommand(
            RecipeForm recipeForm, EntityStorage storageSource) {
        CreateRecipeCommand command = new CreateRecipeCommand(recipeForm);
        command.setStorageSource(storageSource);
        command.execute();

        return command;
    }

    private static CreateRecipeCommand createAndExecuteCommand(
            Recipe recipe, EntityStorage storageSource) {
        return createAndExecuteCommand(new RecipeForm(recipe), storageSource);
    }

    private static void assertUnsuccessfulExecution(CreateRecipeCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(message, command.getExecutionMessage());
        assertNull(command.getCreatedTags());
    }

    private static void assertSuccessfulExecution(CreateRecipeCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());

        Recipe createdRecipe = command.getCreatedRecipe();
        assertNotNull(createdRecipe);
        assertNotNull(createdRecipe.getName());
        assertNotNull(command.getCreatedTags());
    }

    private static void assertUserHasRecipe(Recipe creation, EntityStorage storage)
            throws IOException {
        String username = creation.getAuthorUsername();
        assertNotNull(username);
        User user = storage.getLoader().getUsersByNames(Collections.singletonList(username)).get(0);
        assertTrue(user.getAuthoredRecipes().contains(creation));
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testGetRecipeToAdd(Recipe toAdd) {
        RecipeForm toAddForm = new RecipeForm(toAdd);
        CreateRecipeCommand command = new CreateRecipeCommand(toAddForm);

        RecipeForm fromCommand = command.getToAdd();
        assertNotNull(fromCommand);
        assertEquals(toAddForm, fromCommand);
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testGetCreatedRecipeBeforeExecution(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(new RecipeForm(toAdd));

        assertThrows(IllegalStateException.class, command::getCreatedRecipe);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipe_AssignedNameSame(EntityStorage storageSource, Recipe toAdd)
            throws IOException {
        setupStorage(toAdd, storageSource);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertSuccessfulExecution(command, OK_RECIPE_CREATED_WITH_GIVEN_NAME);
        Recipe createdRecipe = command.getCreatedRecipe();
        assertNotNull(createdRecipe);
        assertEquals(toAdd, createdRecipe);

        assertNotNull(toAdd.getName());
        assertTrue(storageSource.getLoader().recipeNameExists(toAdd.getName()));
        Recipe fromStorage =
                storageSource.getLoader().getRecipesByNames(List.of(toAdd.getName())).get(0);
        assertEquals(fromStorage, toAdd);

        assertUserHasRecipe(createdRecipe, storageSource);

        assertNotNull(command.getCreatedTags());
        assertTrue(command.getCreatedTags().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipe_AssignedNameDifferent(EntityStorage storageSource, Recipe baseRecipe)
            throws IOException {
        setupStorage(baseRecipe, storageSource);
        Recipe toAdd = Utils.renameRecipe(baseRecipe, null);

        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertSuccessfulExecution(command, OK_RECIPE_CREATED_NAME_ASSIGNED);

        Recipe createdRecipe = command.getCreatedRecipe();
        assertNotNull(createdRecipe);
        assertNotNull(createdRecipe.getName());
        assertEquals(Utils.renameRecipe(baseRecipe, createdRecipe.getName()), createdRecipe);

        Recipe fromStorage =
                storageSource
                        .getLoader()
                        .getRecipesByNames(List.of(createdRecipe.getName()))
                        .get(0);
        assertEquals(fromStorage, createdRecipe);

        assertUserHasRecipe(createdRecipe, storageSource);

        assertNotNull(command.getCreatedTags());
        assertTrue(command.getCreatedTags().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeInvalidTagNames(EntityStorage storageSource, Recipe toAdd) {
        setupStorage(toAdd, storageSource, true, false, true);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertSuccessfulExecution(command, OK_RECIPE_CREATED_WITH_GIVEN_NAME);
        for (Tag t : toAdd.getTags()) {
            assertNotNull(t.getName());
            assertTrue(storageSource.getLoader().tagNameExists(t.getName()));
        }
        assertEquals(toAdd.getTags(), command.getCreatedTags());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithString")
    void testCreateRecipeAllExceptNeededFieldsNull(EntityStorage storageSource, String name) {
        RecipeForm toAdd =
                new RecipeForm(null, name, name, null, null, null, null, 0, 0, null, null, null);
        storageSource
                .getSaver()
                .updateUsers(
                        Collections.singletonList(new User.Builder().setUsername(name).build()));
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertSuccessfulExecution(command, OK_RECIPE_CREATED_NAME_ASSIGNED);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipeNameTaken(EntityStorage storageSource, Recipe toAdd) {
        setupStorage(toAdd, storageSource);
        Recipe withReservedName = new Recipe.Builder().setName(toAdd.getName()).build();
        storageSource.getSaver().updateRecipes(Collections.singletonList(withReservedName));

        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_NAME_TAKEN);
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testNullStorageSource(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(new RecipeForm(toAdd));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @Test
    void testCreateNullRecipe() {
        CreateRecipeCommand command = new CreateRecipeCommand(null);
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipeNullPresentationName(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        Recipe toAdd = Utils.renameRecipePresentationName(baseRecipe, null);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipeNullAuthorName(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        Recipe toAdd = new Recipe.Builder(baseRecipe).setAuthorUsername(null).build();
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeNullDirections(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        List<String> directions = new ArrayList<>(baseRecipe.getDirections());
        directions.add(null);
        RecipeForm toAdd =
                new RecipeForm(
                        baseRecipe.getName(),
                        baseRecipe.getPresentationName(),
                        baseRecipe.getAuthorUsername(),
                        baseRecipe.getPrepTime(),
                        baseRecipe.getCookTime(),
                        baseRecipe.getImageUri(),
                        baseRecipe.getNumServings(),
                        baseRecipe.getAvgRating(),
                        baseRecipe.getNumRatings(),
                        directions,
                        Utils.fromTags(baseRecipe.getTags()),
                        Utils.fromIngredients(baseRecipe.getRequiredIngredients()));
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeNullIngredientNames(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        Map<String, Double> requiredIngredients =
                Utils.fromIngredients(baseRecipe.getRequiredIngredients());
        requiredIngredients.put(null, 1.0);
        RecipeForm toAdd =
                new RecipeForm(
                        baseRecipe.getName(),
                        baseRecipe.getPresentationName(),
                        baseRecipe.getAuthorUsername(),
                        baseRecipe.getPrepTime(),
                        baseRecipe.getCookTime(),
                        baseRecipe.getImageUri(),
                        baseRecipe.getNumServings(),
                        baseRecipe.getAvgRating(),
                        baseRecipe.getNumRatings(),
                        baseRecipe.getDirections(),
                        Utils.fromTags(baseRecipe.getTags()),
                        requiredIngredients);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeNullIngredientValues(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        Map<String, Double> requiredIngredients =
                Utils.fromIngredients(baseRecipe.getRequiredIngredients());
        requiredIngredients.put("Null ingredient", null);
        RecipeForm toAdd =
                new RecipeForm(
                        baseRecipe.getName(),
                        baseRecipe.getPresentationName(),
                        baseRecipe.getAuthorUsername(),
                        baseRecipe.getPrepTime(),
                        baseRecipe.getCookTime(),
                        baseRecipe.getImageUri(),
                        baseRecipe.getNumServings(),
                        baseRecipe.getAvgRating(),
                        baseRecipe.getNumRatings(),
                        baseRecipe.getDirections(),
                        Utils.fromTags(baseRecipe.getTags()),
                        requiredIngredients);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeNullTagNames(EntityStorage storageSource, Recipe baseRecipe) {
        setupStorage(baseRecipe, storageSource);
        Set<String> tags = Utils.fromTags(baseRecipe.getTags());
        tags.add(null);
        RecipeForm toAdd =
                new RecipeForm(
                        baseRecipe.getName(),
                        baseRecipe.getPresentationName(),
                        baseRecipe.getAuthorUsername(),
                        baseRecipe.getPrepTime(),
                        baseRecipe.getCookTime(),
                        baseRecipe.getImageUri(),
                        baseRecipe.getNumServings(),
                        baseRecipe.getAvgRating(),
                        baseRecipe.getNumRatings(),
                        baseRecipe.getDirections(),
                        tags,
                        Utils.fromIngredients(baseRecipe.getRequiredIngredients()));
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeInvalidUsernames(EntityStorage storageSource, Recipe toAdd) {
        setupStorage(toAdd, storageSource, true, true, false);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_RESOURCES_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipeNonEmptyDataStructures")
    void testCreateRecipeInvalidIngredientNames(EntityStorage storageSource, Recipe toAdd) {
        setupStorage(toAdd, storageSource, false, true, true);
        CreateRecipeCommand command = createAndExecuteCommand(toAdd, storageSource);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_RESOURCES_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testCreateRecipeWithError(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(new RecipeForm(toAdd));
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testExceptionsAfterRecipeCreation(EntityStorage storageSource, Recipe toAdd) {
        setupStorage(toAdd, storageSource);
        CreateRecipeCommand command = new CreateRecipeCommand(new RecipeForm(toAdd));
        command.setStorageSource(storageSource);
        command.execute();

        assertThrows(IllegalStateException.class, command::execute);
    }
}
