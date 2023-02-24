/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsWithStorage;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;
import static com.recipecart.usecases.CreateTagCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.Tag;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class CreateTagCommandTest {
    private static Stream<Arguments> getTag() {
        return TestUtils.generateArguments(TestData::getTags);
    }

    private static Stream<Arguments> getStorageWithTag() {
        return generateArgumentsWithStorage(getMockStorageArrayGenerators(), TestData::getTags);
    }

    private static CreateTagCommand createAndExecuteCommand(Tag tag, EntityStorage storage) {
        CreateTagCommand command = new CreateTagCommand(tag);
        command.setStorageSource(storage);
        command.execute();

        return command;
    }

    private static void assertUnsuccessfulExecution(CreateTagCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedTag());
        assertEquals(message, command.getExecutionMessage());
    }

    private static void assertSuccessfulExecution(CreateTagCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());

        Tag createdTag = command.getCreatedTag();
        assertNotNull(createdTag);
        assertNotNull(createdTag.getName());
    }

    @ParameterizedTest
    @MethodSource("getTag")
    void testState(Tag toAdd) {
        CreateTagCommand command = new CreateTagCommand(toAdd);

        assertEquals(toAdd, command.getToAdd());
    }

    @ParameterizedTest
    @MethodSource("getTag")
    void testGetCreatedTagBeforeExecution(Tag toAdd) {
        CreateTagCommand command = new CreateTagCommand(toAdd);

        assertThrows(IllegalStateException.class, command::getCreatedTag);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTag")
    void testCreateTag(EntityStorage storage, Tag toAdd) {
        CreateTagCommand command = createAndExecuteCommand(toAdd, storage);

        assertSuccessfulExecution(command, OK_TAG_CREATED);
    }

    @Test
    void testNullTag() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateTagCommand command = new CreateTagCommand((Tag) null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_TAG);
    }

    @Test
    void testInvalidTag() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        CreateTagCommand command = new CreateTagCommand((String) null);
        command.setStorageSource(new EntityStorage(saveAndLoader, saveAndLoader));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_TAG);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTag")
    void testTagNameTaken(EntityStorage storage, Tag toAdd) {
        storage.getSaver().updateTags(Collections.singletonList(toAdd));
        CreateTagCommand command = createAndExecuteCommand(toAdd, storage);

        assertUnsuccessfulExecution(command, NOT_OK_TAG_NAME_TAKEN);
    }

    @ParameterizedTest
    @MethodSource("getTag")
    void testNullStorageSource(Tag toAdd) {
        CreateTagCommand command = new CreateTagCommand(toAdd);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getTag")
    void testCreateTagWithError(Tag toAdd) {
        CreateTagCommand command = new CreateTagCommand(toAdd);
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithTag")
    void testExceptionsAfterTagCreation(EntityStorage storage, Tag toAdd) {
        CreateTagCommand command = createAndExecuteCommand(toAdd, storage);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
