/* (C)2023 */
package com.recipecart.usages;

import com.recipecart.storage.EntityStorage;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents the way for things outside the Business Logic Layer to perform Business
 * Logic use cases.
 */
public class Commander {
    private final @NotNull EntityStorage storage;

    /**
     * Creates a Commander that makes the commands it executes uses the given storage source.
     *
     * @param storage the storage source that executed commands will use
     */
    public Commander(@NotNull EntityStorage storage) {
        this.storage = storage;
    }

    @NotNull public EntityStorage getStorageSource() {
        return storage;
    }

    /**
     * Executes the given command.
     *
     * @param command the command to execute
     * @return the output of executing the command. This return value may have to be cast to a
     *     subtype in order to see the full output. The documentation for the command's execute
     *     method will say which type the return will be.
     */
    @NotNull public EntityCommand.Result execute(@NotNull EntityCommand command) {
        throw new NotImplementedException();
    }
}
