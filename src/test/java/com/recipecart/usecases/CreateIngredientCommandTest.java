/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;
import static com.recipecart.usecases.CreateIngredientCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CreateIngredientCommandTest {
    private static Stream<Arguments> getIngredient() {
        return TestUtils.generateArguments(TestData::getIngredients);
    }

    private static Stream<Arguments> getStorageWithIngredient() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(),
                Collections.singletonList(TestData::getIngredients));
    }

    private static CreateIngredientCommand createAndExecuteCommand(
            Ingredient ingredient, EntityStorage storage) {
        CreateIngredientCommand command = new CreateIngredientCommand(ingredient);
        command.setStorageSource(storage);
        command.execute();

        return command;
    }

    private static void assertUnsuccessfulExecution(
            CreateIngredientCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedEntity());
        assertEquals(message, command.getExecutionMessage());
    }

    private static void assertSuccessfulExecution(CreateIngredientCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());

        Ingredient createdIngredient = command.getCreatedEntity();
        assertNotNull(createdIngredient);
        assertNotNull(createdIngredient.getName());
    }

    @ParameterizedTest
    @MethodSource("getIngredient")
    void testState(Ingredient toAdd) {
        CreateIngredientCommand command = new CreateIngredientCommand(toAdd);

        assertEquals(toAdd, command.getEntityToAdd());
    }

    @ParameterizedTest
    @MethodSource("getIngredient")
    void testGetCreatedIngredientBeforeExecution(Ingredient toAdd) {
        CreateIngredientCommand command = new CreateIngredientCommand(toAdd);

        assertThrows(IllegalStateException.class, command::getCreatedEntity);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithIngredient")
    void testCreateIngredient(EntityStorage storage, Ingredient toAdd) {
        CreateIngredientCommand command = createAndExecuteCommand(toAdd, storage);

        assertSuccessfulExecution(command, OK_INGREDIENT_CREATED);
    }

    @Test
    void testNullIngredient() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateIngredientCommand command = new CreateIngredientCommand(null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_INGREDIENT);
    }

    @Test
    void testInvalidIngredient() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateIngredientCommand command = new CreateIngredientCommand(null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_INGREDIENT);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithIngredient")
    void testIngredientNameTaken(EntityStorage storage, Ingredient toAdd) {
        storage.getSaver().updateIngredients(Collections.singletonList(toAdd));
        CreateIngredientCommand command = createAndExecuteCommand(toAdd, storage);

        assertUnsuccessfulExecution(command, NOT_OK_INGREDIENT_NAME_TAKEN);
    }

    @ParameterizedTest
    @MethodSource("getIngredient")
    void testNullStorageSource(Ingredient toAdd) {
        CreateIngredientCommand command = new CreateIngredientCommand(toAdd);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getIngredient")
    void testCreateIngredientWithError(Ingredient toAdd) {
        CreateIngredientCommand command = new CreateIngredientCommand(toAdd);
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithIngredient")
    void testExceptionsAfterIngredientCreation(EntityStorage storage, Ingredient toAdd) {
        CreateIngredientCommand command = createAndExecuteCommand(toAdd, storage);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
