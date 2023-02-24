/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;
import static com.recipecart.usecases.CreateUserCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CreateUserCommandTest {
    private static Stream<Arguments> getTwoStrings() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getNotNullStrings, TestData::getStrings), 1, true);
    }

    private static Stream<Arguments> getStorageWithTwoStrings() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(),
                Collections.singletonList(TestData::getNotNullStrings),
                Collections.singletonList(TestData::getStrings));
    }

    private static CreateUserCommand createAndExecuteCommand(
            String username, String emailAddress, EntityStorage storage) {
        CreateUserCommand command = new CreateUserCommand(username, emailAddress);
        command.setStorageSource(storage);
        command.execute();

        return command;
    }

    private static void assertUnsuccessfulExecution(CreateUserCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedEntity());
        assertEquals(message, command.getExecutionMessage());
    }

    private static void assertSuccessfulExecution(CreateUserCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());

        User createdUser = command.getCreatedEntity();
        assertNotNull(createdUser);
        assertNotNull(createdUser.getUsername());
        assertTrue(createdUser.getAuthoredRecipes().isEmpty());
        assertTrue(createdUser.getSavedRecipes().isEmpty());
        assertTrue(createdUser.getRatedRecipes().isEmpty());
        assertTrue(createdUser.getOwnedIngredients().isEmpty());
        assertTrue(createdUser.getShoppingList().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getTwoStrings")
    void testState(String username, String emailAddress) {
        CreateUserCommand command = new CreateUserCommand(username, emailAddress);

        User toAdd = command.getEntityToAdd();
        assertEquals(username, toAdd.getUsername());
        assertEquals(emailAddress, toAdd.getEmailAddress());
        assertTrue(toAdd.getAuthoredRecipes().isEmpty());
        assertTrue(toAdd.getSavedRecipes().isEmpty());
        assertTrue(toAdd.getRatedRecipes().isEmpty());
        assertTrue(toAdd.getOwnedIngredients().isEmpty());
        assertTrue(toAdd.getShoppingList().isEmpty());
    }

    @ParameterizedTest
    @MethodSource("getTwoStrings")
    void testGetCreatedUserBeforeExecution(String username, String emailAddress) {
        CreateUserCommand command = new CreateUserCommand(username, emailAddress);

        assertThrows(IllegalStateException.class, command::getCreatedEntity);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTwoStrings")
    void testCreateUser(EntityStorage storage, String username, String emailAddress) {
        CreateUserCommand command = createAndExecuteCommand(username, emailAddress, storage);

        assertSuccessfulExecution(command, OK_USER_CREATED);
    }

    @Test
    void testNullUser() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateUserCommand command = new CreateUserCommand(null, null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_USER);
    }

    @Test
    void testInvalidUser() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateUserCommand command = new CreateUserCommand(null, null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_USER);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTwoStrings")
    void testUsernameTaken(EntityStorage storage, String username, String emailAddress) {
        storage.getSaver()
                .updateUsers(
                        Collections.singletonList(
                                new User.Builder()
                                        .setUsername(username)
                                        .setEmailAddress(emailAddress)
                                        .build()));
        CreateUserCommand command = createAndExecuteCommand(username, emailAddress, storage);

        assertUnsuccessfulExecution(command, NOT_OK_USERNAME_TAKEN);
    }

    @ParameterizedTest
    @MethodSource("getTwoStrings")
    void testNullStorageSource(String username, String emailAddress) {
        CreateUserCommand command = new CreateUserCommand(username, emailAddress);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getTwoStrings")
    void testCreateUserWithError(String username, String emailAddress) {
        CreateUserCommand command = new CreateUserCommand(username, emailAddress);
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTwoStrings")
    void testExceptionsAfterUserCreation(
            EntityStorage storage, String username, String emailAddress) {
        CreateUserCommand command = createAndExecuteCommand(username, emailAddress, storage);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
