/* (C)2023 */
package com.recipecart.usages;

import com.recipecart.storage.EntityStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This abstract class represents an action item that represents a use case involving entities. */
public abstract class EntityCommand {
    private @Nullable EntityStorage storage = null;

    /** @return the place this EntityCommand saves and loads entities during execution. */
    @Nullable public EntityStorage getStorageSource() {
        return storage;
    }

    /**
     * Sets the place where this EntityCommand will save and load entities while executing.
     *
     * @param storage the place to save/load entities
     */
    void setStorageSource(@NotNull EntityStorage storage) {
        this.storage = storage;
    }

    /**
     * Executes this EntityCommand, performing the use case represented by the implementing class.
     *
     * @return the results of executing this command
     */
    @NotNull abstract Result execute();

    /** This class represents the results from executing a EntityCommand. */
    public static class Result {
        private final boolean success;
        private final @NotNull String message;

        Result(boolean success, @NotNull String message) {
            this.success = success;
            this.message = message;
        }

        /** @return true if the EntityCommand was executed successfully, false otherwise */
        public boolean isSuccessful() {
            return success;
        }

        /** @return a message of if the execution was successful, or why it was unsuccessful */
        @NotNull public String getMessage() {
            return message;
        }
    }
}
