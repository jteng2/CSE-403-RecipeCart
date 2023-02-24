/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.usecases.Command.NOT_OK_ERROR;
import static com.recipecart.usecases.EntityCommand.NOT_OK_BAD_STORAGE;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.storage.EntitySaver;
import com.recipecart.storage.EntityStorage;
import java.util.stream.Stream;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class SimpleGetCommandTest<T> {
    private SimpleGetCommand<T> getAndExecuteCommand(T entity, EntityStorage storage) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));
        addEntityToStorage(entity, storage.getSaver());
        command.setStorageSource(storage);
        command.execute();

        return command;
    }

    private static <T> void assertUnsuccessfulExecution(
            SimpleGetCommand<T> command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertNull(command.getRetrievedEntity());
        assertEquals(message, command.getExecutionMessage());
    }

    private static <T> void assertSuccessfulExecution(SimpleGetCommand<T> command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertNotNull(command.getRetrievedEntity());
        assertEquals(message, command.getExecutionMessage());
    }

    protected abstract void addEntityToStorage(T entity, EntitySaver saver);

    protected abstract SimpleGetCommand<T> getGetEntityCommand(String name);

    protected abstract Stream<Arguments> getEntity();

    protected abstract Stream<Arguments> getStorageWithEntity();

    protected abstract String getName(T entity);

    @ParameterizedTest
    @MethodSource("getEntity")
    void testState(T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));

        assertEquals(getName(entity), command.getEntityName());
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testGetRetrievedEntityBeforeExecution(T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));

        assertThrows(IllegalStateException.class, command::getRetrievedEntity);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testRetrieveEntity(EntityStorage storage, T entity) {
        SimpleGetCommand<T> command = getAndExecuteCommand(entity, storage);

        assertSuccessfulExecution(command, command.getOkEntityRetrievedMessage());
        assertEquals(entity, command.getRetrievedEntity());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testEntityNotFound(EntityStorage storage, T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));
        command.setStorageSource(storage);
        command.execute();

        assertUnsuccessfulExecution(command, command.getNotOkNotFoundMessage());
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testNullName(EntityStorage storage, T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(null);
        addEntityToStorage(entity, storage.getSaver());
        command.setStorageSource(storage);
        command.execute();

        assertUnsuccessfulExecution(command, command.getNotOkNotFoundMessage());
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testNullStorageSource(T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getEntity")
    void testRetrievalWithError(T entity) {
        SimpleGetCommand<T> command = getGetEntityCommand(getName(entity));
        command.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getStorageWithEntity")
    void testExceptionsAfterRetrieval(EntityStorage storage, T entity) {
        SimpleGetCommand<T> command = getAndExecuteCommand(entity, storage);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
