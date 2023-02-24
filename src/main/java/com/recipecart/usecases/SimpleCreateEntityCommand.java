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

    public T getEntityToAdd() {
        return toAdd;
    }

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

    protected abstract String getOkEntityCreatedMessage();

    protected abstract String getNotOkInvalidEntityMessage();

    protected abstract String getNotOkEntityNameAlreadyTakenMessage();

    protected abstract String getEntityName(T entity);

    protected abstract String getEntityClassName();

    protected abstract void updateEntities(EntitySaver saver, Collection<T> entities);

    protected abstract boolean entityNameExists(EntityLoader loader, String entityName);
}
