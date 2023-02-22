/* (C)2023 */
package com.recipecart.execution;

import com.recipecart.storage.EntityStorage;
import com.recipecart.usecases.EntityCommand;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the way for things outside the Business Logic Layer to perform Business
 * Logic use cases.
 */
public class EntityCommander {
    private final @NotNull EntityStorage storage;

    /**
     * Creates a Commander that makes the commands it executes uses the given storage source.
     *
     * @param storage the storage source that executed commands will use
     */
    public EntityCommander(@NotNull EntityStorage storage) {
        this.storage = storage;
    }

    @NotNull public EntityStorage getStorageSource() {
        return storage;
    }

    /**
     * Executes the given command.
     *
     * @param command the command to execute
     */
    public void execute(@NotNull EntityCommand command) {
        command.setStorageSource(getStorageSource());
        command.execute();
    }
}
