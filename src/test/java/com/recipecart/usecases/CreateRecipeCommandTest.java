/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;
import static com.recipecart.usecases.CreateRecipeCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
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

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testGetRecipeToAdd(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);

        Recipe fromCommand = command.getRecipeToAdd();
        assertNotNull(fromCommand);
        assertEquals(toAdd, fromCommand);
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testGetCreatedRecipeBeforeExecution(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);

        assertThrows(IllegalStateException.class, command::getCreatedRecipe);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipe_AssignedNameSame(EntityStorage storageSource, Recipe toAdd)
            throws IOException {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(storageSource);

        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(OK_RECIPE_CREATED_WITH_GIVEN_NAME, command.getExecutionMessage());

        Recipe createdRecipe = command.getCreatedRecipe();
        assertNotNull(createdRecipe);
        assertEquals(toAdd, createdRecipe);

        assertTrue(storageSource.getLoader().recipeNameExists(toAdd.getName()));
        Recipe fromStorage =
                storageSource.getLoader().getRecipesByNames(List.of(toAdd.getName())).get(0);
        assertEquals(fromStorage, toAdd);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipe_AssignedNameDifferent(EntityStorage storageSource, Recipe baseRecipe)
            throws IOException {
        Recipe toAdd = Utils.renameRecipe(baseRecipe, null);
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(storageSource);

        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(OK_RECIPE_CREATED_NAME_ASSIGNED, command.getExecutionMessage());

        Recipe createdRecipe = command.getCreatedRecipe();
        assertNotNull(createdRecipe);
        assertEquals(Utils.renameRecipe(baseRecipe, createdRecipe.getName()), createdRecipe);

        Recipe fromStorage =
                storageSource.getLoader().getRecipesByNames(List.of(baseRecipe.getName())).get(0);
        assertEquals(fromStorage, createdRecipe);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateRecipeNameTaken(EntityStorage storageSource, Recipe toAdd) {
        Recipe withReservedName = new Recipe.Builder().setName(toAdd.getName()).build();
        storageSource.getSaver().updateRecipes(Collections.singletonList(withReservedName));

        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(storageSource);
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_NAME_TAKEN, command.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testNullStorageSource(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_BAD_STORAGE, command.getExecutionMessage());
    }

    @Test
    void testCreateNullRecipe() {
        CreateRecipeCommand command = new CreateRecipeCommand(null);
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_INVALID_RECIPE, command.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateInvalidRecipePresentationName(EntityStorage storageSource, Recipe baseRecipe) {
        Recipe toAdd = Utils.renameRecipePresentationName(baseRecipe, null);
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(storageSource);
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_INVALID_RECIPE, command.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testCreateInvalidRecipeAuthorName(EntityStorage storageSource, Recipe toAdd) {
        CreateRecipeCommand command =
                new CreateRecipeCommand(new Recipe.Builder(toAdd).setAuthorUsername(null).build());
        command.setStorageSource(storageSource);
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_INVALID_RECIPE, command.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testCreateRecipeWithError(Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedRecipe());
        assertEquals(NOT_OK_ERROR, command.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithRecipe")
    void testExceptionsAfterRecipeCreation(EntityStorage storageSource, Recipe toAdd) {
        CreateRecipeCommand command = new CreateRecipeCommand(toAdd);
        command.setStorageSource(storageSource);
        command.execute();

        assertThrows(IllegalStateException.class, command::execute);
    }
}
