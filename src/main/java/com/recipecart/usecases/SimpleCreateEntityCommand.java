/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents the action item of a new entity of type T being created/saved into a given
 * EntityStorage.
 *
 * @param <T> the type of the entity (e.g. Tag, Ingredient, User) to be created/saved
 */
public abstract class SimpleCreateEntityCommand<T> extends EntityCommand {
    private final T toAdd;
    private T createdEntity = null;

    /**
     * Creates the action item of saving the given entity to the EntityStorage inputted into this
     * command.
     *
     * @param toAdd the entity to save
     */
    protected SimpleCreateEntityCommand(T toAdd) {
        this.toAdd = toAdd;
    }

    /**
     * Returns the (output) T that was created and saved when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) T that was created/saved, if the command successfully added it; null
     *     otherwise.
     */
    @Nullable public T getCreatedEntity() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return createdEntity;
    }

    private void setCreatedEntity(@NotNull T createdEntity) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the created "
                            + getEntityClassName()
                            + "after command has executed");
        }
        if (this.createdEntity != null) {
            throw new IllegalStateException(
                    "Can only set created " + getEntityClassName() + " once");
        }
        Objects.requireNonNull(createdEntity);
        this.createdEntity = createdEntity;
    }

    /**
     * @return the entity to save into the storage given into this command.
     */
    public T getEntityToAdd() {
        return toAdd;
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isEntityValid()) {
            return getNotOkInvalidEntityMessage();
        }
        try {
            if (!isEntityNameAvailable()) {
                return getNotOkEntityNameAlreadyTakenMessage();
            }
        } catch (RuntimeException e) { // for data access layer failures
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isEntityValid() {
        return getEntityToAdd() != null && getEntityName(getEntityToAdd()) != null;
    }

    private boolean isEntityNameAvailable() {
        assert getStorageSource() != null;
        assert getEntityName(getEntityToAdd()) != null;
        return !entityNameExists(getStorageSource().getLoader(), getEntityName(getEntityToAdd()));
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

        try {
            saveEntity(getEntityToAdd());
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        }

        finishExecutingSuccessfulEntityCreation(getEntityToAdd());
    }

    private void saveEntity(T toSave) {
        assert getStorageSource() != null;

        updateEntities(getStorageSource().getSaver(), Collections.singletonList(toSave));
    }

    private void finishExecutingSuccessfulEntityCreation(T created) {
        setCreatedEntity(created);
        setExecutionMessage(getOkEntityCreatedMessage());
        beSuccessful();
        finishExecuting();
    }

    /**
     * @return a message saying that the command's execution was successful; the entity was
     *     successfully added.
     */
    protected abstract String getOkEntityCreatedMessage();

    /**
     * @return a message saying that the command's execution was unsuccessful due to the given
     *     entity being invalid.
     */
    protected abstract String getNotOkInvalidEntityMessage();

    /**
     * @return a message saying that the command's execution was unsuccessful due to an entity with
     *     the same name as the given entity already existing in the storage.
     */
    protected abstract String getNotOkEntityNameAlreadyTakenMessage();

    /**
     * Gets the (unique) name of the given entity.
     *
     * @param entity the entity to get the name of
     * @return the entity's name.
     */
    protected abstract String getEntityName(T entity);

    /**
     * @return the class name of the entities being searched for.
     */
    protected abstract String getEntityClassName();

    /**
     * Saves the given entities into the given storage.
     *
     * @param saver the storage to save the entities into.
     * @param entities the entities to save.
     */
    protected abstract void updateEntities(EntitySaver saver, Collection<T> entities);

    /**
     * Checks the storage corresponding to the given loader, if an entity (of the same type) with
     * the given name exists there.
     *
     * @param loader the loader to do the check with
     * @param entityName the name of the entity to check for
     * @return true if an entity with the given name was found, false otherwise
     */
    protected abstract boolean entityNameExists(EntityLoader loader, String entityName);
}
