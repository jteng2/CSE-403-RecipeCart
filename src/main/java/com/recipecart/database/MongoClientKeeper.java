/* (C)2023 */
package com.recipecart.database;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * This singleton class keeps track of MongoClients created, to prevent duplicate MongoClients to
 * the same database from being made.
 */
public class MongoClientKeeper {
    private static MongoClientKeeper instance = null;

    private final @NotNull Map<@NotNull ServerAddress, @NotNull MongoClient> connections;

    private MongoClientKeeper() {
        connections = new HashMap<>();
    }

    /**
     * Returns Mongo client connected to the database at the given host address.
     *
     * @param hostAddress the host address of the database to connect to
     * @return a Mongo client connected to the address. This client will either be a new or existing
     *     client based on if a connection was already made via MongoConnections.
     */
    public MongoClient getConnectionTo(ServerAddress hostAddress) {
        throw new NotImplementedException();
    }

    /** @return the singleton MongoConnections */
    public static MongoClientKeeper getInstance() {
        if (instance == null) {
            instance = new MongoClientKeeper();
        }
        return instance;
    }
}
