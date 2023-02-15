/* (C)2023 */
package com.recipecart.usecases;

import org.jetbrains.annotations.NotNull;

/**
 * This interface represents a general executable command with outputs of whether the command was
 * successful, as well as a message output. Each Command instance can only be executed once.
 */
public interface Command {
    String OK_GENERAL = "OK", NOT_OK_GENERAL = "Not OK", DEFAULT_MESSAGE = NOT_OK_GENERAL;

    /**
     * @return true if this command has finished executing, false otherwise.
     */
    boolean isFinishedExecuting();

    /**
     * Returns whether the command's execution was successful. Can only be called once the command
     * has finished executing.
     *
     * @throws IllegalStateException if the command hasn't finished executing (or hasn't started).
     * @return true if this command's execution was successful, false otherwise.
     */
    boolean isSuccessful();

    /**
     * Returns a message describing the result of the command's execution (ex. what went wrong, if
     * the command failed). Can only be called once the command has finished executing.
     *
     * @throws IllegalStateException if the command hasn't finished executing (or hasn't started).
     * @return the message describing the result of the command's execution
     */
    @NotNull String getExecutionMessage();

    /**
     * Executes this command, performing whatever action is specified by the implementer.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    void execute();
}
