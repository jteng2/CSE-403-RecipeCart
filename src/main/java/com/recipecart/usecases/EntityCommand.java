/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.storage.EntityStorage;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This abstract class represents an action item that represents a use case involving entities. */
public abstract class EntityCommand implements Command {
    private @Nullable EntityStorage storage = null;

    /**
     * @return the place this command saves and loads entities during execution.
     */
    @Nullable public EntityStorage getStorageSource() {
        return storage;
    }

    /**
     * Sets the place where this command will save and load entities while executing.
     *
     * @param storage the place to save/load entities
     */
    void setStorageSource(@NotNull EntityStorage storage) {
        this.storage = storage;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinishedExecuting() {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSuccessful() {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull public String getExecutionMessage() {
        throw new NotImplementedException();
    }

    /**
     * Executes this command, performing the use case represented by the implementing class.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public abstract void execute();
}
