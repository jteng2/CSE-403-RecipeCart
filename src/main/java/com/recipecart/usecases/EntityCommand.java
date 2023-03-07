/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.storage.EntityStorage;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This abstract class represents an action item that represents a use case involving entities. The
 * general workflow for an implementer of EntityCommand (internally) is:
 *
 * <ul>
 *   <li>Have the command be constructed with the arguments needed for its execution
 *   <li>Have the <code>execute</code> method be called
 *   <li>Perform whatever execution the implementer's spec calls for until execution is finished
 *   <li>Call <code>setExecutionMessage</code>, giving a message describing how the command executed
 *       (un)successfully
 *   <li>Call <code>beSuccessful</code> if the execution was successful according to the
 *       implementer's spec
 *   <li>Call any other output setter methods corresponding to output of the command according to
 *       the implementer's spec
 *   <li>Call <code>finishExecuting</code>
 * </ul>
 *
 * <code>finishExecuting</code> is meant for "finalizing" the command's state. <code>
 * setExecutionMessage</code> and <code>beSuccessful</code> cannot be called after <code>
 * finishExecuting</code> has been called, and they can only be called once. The other output setter
 * methods in implementations of this class should also follow this convention. But <code>
 * beSuccessful</code>, <code>setExecutionMessage</code>, and these other output setter methods can
 * be called in any order.
 */
public abstract class EntityCommand implements Command {
    public static final String NOT_OK_BAD_STORAGE =
            "Execution unsuccessful: an internal error has occurred while trying to execute this"
                    + " task (as a result of internal improper handling of entity storage)";

    private boolean finishedExecuting = false;
    private boolean successful = false;
    private String executionMessage = null;
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
    public void setStorageSource(@NotNull EntityStorage storage) {
        this.storage = storage;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isFinishedExecuting() {
        return finishedExecuting;
    }

    /**
     * Sets this command's visible state to where it's finished executing (isFinishedExecuting
     * returns false before calling this method, but will now return true after). Can only be called
     * once.
     *
     * @throws IllegalStateException if this method has been called before.
     */
    protected void finishExecuting() {
        if (isFinishedExecuting()) {
            throw new IllegalStateException("Command has already finished executing");
        }
        if (executionMessage == null) {
            setExecutionMessage(DEFAULT_MESSAGE);
        }
        finishedExecuting = true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isSuccessful() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return successful;
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
        if (isFinishedExecuting()) {
            throw new IllegalStateException("Cannot set success after command finished executing");
        }
        if (successful) {
            throw new IllegalStateException("Cannot set success twice");
        }
        successful = true;
    }

    /** {@inheritDoc} */
    @Override
    @NotNull public String getExecutionMessage() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return executionMessage;
    }

    /**
     * Sets this command's execution message to the given message. Can only be called once, and can
     * only be called before the command finished executing. If this method isn't called before the
     * command finishes executing, then the command's message will default to
     * Command.DEFAULT_MESSAGE.
     *
     * @param message the execution message to give this command
     * @throws IllegalStateException if this method has been called before, or if it's called after
     *     finishExecuting() was called.
     */
    protected void setExecutionMessage(@NotNull String message) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException("Cannot set message after command finished executing");
        }
        if (executionMessage != null) {
            throw new IllegalStateException("Can only set the execution message once");
        }
        Objects.requireNonNull(message);
        executionMessage = message;
    }

    /**
     * @throws IllegalStateException if this command has already finished executing
     */
    protected void checkExecutionAlreadyDone() {
        if (isFinishedExecuting()) {
            throw new IllegalStateException("Cannot execute command twice");
        }
    }

    /**
     * Appropriately finishes execution of a command if it's invalid.
     *
     * @return true if the command is invalid (and thus finished), false otherwise
     */
    protected boolean finishInvalidCommand() {
        String invalidMessage = getInvalidCommandMessage();
        if (invalidMessage != null) {
            setExecutionMessage(invalidMessage);
            finishExecuting();
            return true;
        }
        return false;
    }

    /**
     * Gets a message that explains what's invalid about this command (i.e. what was inputted into
     * it) or its execution.
     *
     * @return the message, or null if the command is valid.
     */
    protected String getInvalidCommandMessage() {
        if (!isStorageSourceValid()) {
            return NOT_OK_BAD_STORAGE;
        }
        return null;
    }

    /**
     * @return true if the EntityStorage this command was given is valid (i.e. not-null); false
     *     otherwise
     */
    protected boolean isStorageSourceValid() {
        return getStorageSource() != null;
    }

    /**
     * Performs the necessary method calls to set this command's visible state to where its
     * execution was unsuccessful due to the given error. Assumes beSuccessful hasn't been called.
     *
     * @param e the exception whose stacktrace to print
     */
    protected void finishExecutingFromError(Exception e) {
        if (e != null) {
            e.printStackTrace();
        }
        setExecutionMessage(NOT_OK_ERROR);
        finishExecuting();
    }

    /**
     * Performs the necessary method calls to set this command's visible state to where its
     * execution was unsuccessful due to an error. Assumes beSuccessful hasn't been called.
     */
    protected void finishExecutingFromError() {
        finishExecutingFromError(null);
    }

    /**
     * Performs the necessary method calls to set this command's visible state to where its
     * execution was unsuccessful due to an exception that's thought to be impossible to be thrown.
     * Assumes beSuccessful hasn't been called.
     *
     * @param e the exception that's thought to be impossible to be thrown
     */
    protected void finishExecutingImpossibleOutcome(Exception e) {
        e.printStackTrace();
        setExecutionMessage(NOT_OK_IMPOSSIBLE_OUTCOME);
        finishExecuting();
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
