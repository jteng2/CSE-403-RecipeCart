/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Objects;

/**
 * This class represents the action item of an entity, of type T, with the given name, being
 * retrieved from a given EntityStorage.
 *
 * @param <T> the type of the entity (e.g. Tag, Ingredient, User, Recipe) to be retrieved
 */
public abstract class SimpleGetCommand<T> extends EntityCommand {
    private T retrievedEntity;
    private final String name;

    /**
     * Creates the action item of retrieving the entity, of type T, with the given name.
     *
     * @param name the name of the T entity to retrieve
     */
    SimpleGetCommand(String name) {
        this.name = name;
    }

    /**
     * @return the name of the entity to retrieve.
     */
    public String getEntityName() {
        return name;
    }

    /**
     * Returns the (output) T that was retrieved when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) T that was retrieved, if the command successfully retrieved it; null
     *     otherwise.
     */
    public T getRetrievedEntity() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return retrievedEntity;
    }

    private void setRetrievedEntity(T retrievedEntity) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the retrieved "
                            + getEntityClassName()
                            + "after command has executed");
        }
        if (this.retrievedEntity != null) {
            throw new IllegalStateException(
                    "Can only set retrieved " + getEntityClassName() + " once");
        }
        Objects.requireNonNull(retrievedEntity);
        this.retrievedEntity = retrievedEntity;
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isNameValid()) {
            return getNotOkNotFoundMessage();
        }
        try {
            if (!doesEntityExist()) {
                return getNotOkNotFoundMessage();
            }
        } catch (RuntimeException e) { // for data access layer failures
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isNameValid() {
        return getEntityName() != null;
    }

    private boolean doesEntityExist() {
        assert getStorageSource() != null;
        return entityNameExists(getStorageSource().getLoader(), getEntityName());
    }

    /**
     * Has the entity (T) be created and saved. If the entity name is null or corresponds to an
     * already-existing entity of the same type, then this command's execution will be unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        checkExecutionAlreadyDone();
        if (finishInvalidCommand()) {
            return;
        }

        T retrieved;
        try {
            assert getStorageSource() != null;
            retrieved = retrieveEntity(getStorageSource().getLoader(), getEntityName());
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        } catch (IOException e) {
            finishExecutingImpossibleOutcome(e);
            return;
        }

        finishExecutingSuccessfulEntityCreation(retrieved);
    }

    private void finishExecutingSuccessfulEntityCreation(T retrieved) {
        setRetrievedEntity(retrieved);
        setExecutionMessage(getOkEntityRetrievedMessage());
        beSuccessful();
        finishExecuting();
    }

    /**
     * @return a message saying that the command's execution was successful; the entity with the
     *     given name was retrieved.
     */
    protected abstract String getOkEntityRetrievedMessage();

    /**
     * @return a message saying that the command's execution was unsuccessful, as the entity (of the
     *     same type) with the given name was not found.
     */
    protected abstract String getNotOkNotFoundMessage();

    /**
     * @return the class name of the entities being searched for.
     */
    protected abstract String getEntityClassName();

    /**
     * Checks the storage corresponding to the given loader, if an entity (of the same type) with
     * the given name exists there.
     *
     * @param loader the loader to do the check with
     * @param entityName the name of the entity to check for
     * @return true if an entity with the given name was found, false otherwise
     */
    protected abstract boolean entityNameExists(EntityLoader loader, String entityName);

    /**
     * Retrieves the entity with the given name from the storage corresponding to the given loader.
     *
     * @param loader the loader to do the retrieval with
     * @param entityName the name of the entity to retrieve
     * @return the retrieved entity
     * @throws IOException if an entity with the given name could not be found
     */
    protected abstract T retrieveEntity(EntityLoader loader, String entityName) throws IOException;
}
