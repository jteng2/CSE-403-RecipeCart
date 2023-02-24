/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.usecases.Command.NOT_OK_ERROR;
import static com.recipecart.usecases.EntityCommand.NOT_OK_BAD_STORAGE;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.storage.EntitySaver;
import com.recipecart.storage.EntityStorage;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SimpleCreateEntityCommandTest<T> {
    private SimpleCreateEntityCommand<T> getAndExecuteCommand(T entity, EntityStorage storage) {
        SimpleCreateEntityCommand<T> command = getCreateEntityCommand(entity);
        command.setStorageSource(storage);
        command.execute();

        return command;
    }

    private static <T> void assertUnsuccessfulExecution(
            SimpleCreateEntityCommand<T> command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getCreatedEntity());
        assertEquals(message, command.getExecutionMessage());
    }

    private static <T> void assertSuccessfulExecution(
            SimpleCreateEntityCommand<T> command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());
        assertNotNull(command.getCreatedEntity());
    }

    protected void assertGoodEntity(T baseEntity, T outputEntity) {
        assertEquals(baseEntity, outputEntity);
    }

    protected abstract Stream<Arguments> getEntity();

    protected abstract Stream<Arguments> getStorageWithEntity();

    protected abstract SimpleCreateEntityCommand<T> getCreateEntityCommand(T entity);

    protected abstract T renameToNullEntity(T entity);

    protected abstract void addEntitiesToStorage(Collection<T> entities, EntitySaver saver);

    @ParameterizedTest
    @MethodSource("getEntity")
    void testState(T toAdd) {
        SimpleCreateEntityCommand<T> command = getCreateEntityCommand(toAdd);

        assertGoodEntity(toAdd, command.getEntityToAdd());
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testGetCreatedEntityBeforeExecution(T toAdd) {
        SimpleCreateEntityCommand<T> command = getCreateEntityCommand(toAdd);

        assertThrows(IllegalStateException.class, command::getCreatedEntity);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testCreateEntity(EntityStorage storage, T toAdd) {
        SimpleCreateEntityCommand<T> command = getAndExecuteCommand(toAdd, storage);

        assertSuccessfulExecution(command, command.getOkEntityCreatedMessage());
        assertGoodEntity(toAdd, command.getCreatedEntity());
    }

    @Test
    void testNullEntity() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        SimpleCreateEntityCommand<T> command =
                getAndExecuteCommand(null, new EntityStorage(saveAndLoader, saveAndLoader));

        assertUnsuccessfulExecution(command, command.getNotOkInvalidEntityMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testInvalidEntity(EntityStorage storage, T toAdd) {
        T invalidEntity = renameToNullEntity(toAdd);
        SimpleCreateEntityCommand<T> command = getAndExecuteCommand(invalidEntity, storage);

        assertUnsuccessfulExecution(command, command.getNotOkInvalidEntityMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testEntityNameTaken(EntityStorage storage, T toAdd) {
        addEntitiesToStorage(Collections.singleton(toAdd), storage.getSaver());
        SimpleCreateEntityCommand<T> command = getAndExecuteCommand(toAdd, storage);

        assertUnsuccessfulExecution(command, command.getNotOkEntityNameAlreadyTakenMessage());
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testNullStorageSource(T toAdd) {
        SimpleCreateEntityCommand<T> command = getCreateEntityCommand(toAdd);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testCreateEntityWithError(T toAdd) {
        SimpleCreateEntityCommand<T> command = getCreateEntityCommand(toAdd);
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testExceptionsAfterEntityCreation(EntityStorage storage, T toAdd) {
        SimpleCreateEntityCommand<T> command = getAndExecuteCommand(toAdd, storage);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
