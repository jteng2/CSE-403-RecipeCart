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

    /**
     * Sets this command's visible state to where it's finished executing (isFinishedExecuting
     * returns false before calling this method, but will now return true after). Can only be called
     * once.
     *
     * @throws IllegalStateException if this method has been called before
     */
    protected void finishExecuting() {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSuccessful() {
        throw new NotImplementedException();
    }

    /**
     * Sets this command's visible state to where its execution was successful. Can only be called
     * once, and can only be called before the command finishes executing. If this method isn't
     * called before the command finishes executing, then the command's visible state will default
     * to its execution being unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before, or if it's called after
     *     finishExecuting() was called.
     */
    protected void beSuccessful() {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    @NotNull public String getExecutionMessage() {
        throw new NotImplementedException();
    }

    /**
     * Sets this command's execution message to the given message. Can only be called once, and can
     * only be called before the command finished executing. If this method isn't called before the
     * command finishes executing, then the command's message will default to
     * Command.NOT_OK_GENERAL.
     *
     * @throws IllegalStateException if this method has been called before.
     */
    protected void setExecutionMessage(String message) {
        throw new NotImplementedException();
    }

    /**
     * Executes this command, performing the use case represented by the implementing class. This
     * method must call finishExecuting, beSuccessful, and setExecutionMessage in a way that
     * satisfies their specs and the implementer's spec of this method. (This doesn't necessarily
     * mean that beSuccessful or setExecutionMessage has to be called, but it necessarily means
     * finishExecuting has to be called.)
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public abstract void execute();
}
