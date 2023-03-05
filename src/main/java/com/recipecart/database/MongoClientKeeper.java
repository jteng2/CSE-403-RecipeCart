/* (C)2023 */
package com.recipecart.database;

import com.mongodb.ConnectionString;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import java.util.HashMap;
import java.util.Map;
import org.bson.BsonDocument;
import org.bson.BsonInt64;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.jetbrains.annotations.NotNull;

/**
 * This singleton class keeps track of MongoClients created, to prevent duplicate MongoClients to
 * the same database from being made.
 */
public class MongoClientKeeper {
    private static MongoClientKeeper instance = null;

    private final @NotNull Map<@NotNull ConnectionString, @NotNull MongoClient> connections;

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
    public MongoClient getConnectionTo(ConnectionString hostAddress) {
        if (connections.containsKey(hostAddress)) {
            return connections.get(hostAddress);
        }

        MongoClient client = MongoClients.create(hostAddress);
        connections.put(hostAddress, client);

        pingDatabase(client);

        return client;
    }

    private static void pingDatabase(MongoClient client) {
        MongoDatabase database = client.getDatabase("admin");
        try {
            Bson ping = new BsonDocument("ping", new BsonInt64(1));
            Document commandResult = database.runCommand(ping);
            System.out.println("Connected successfully to server.");
        } catch (MongoException me) {
            System.err.println("An error occurred while attempting to run a command: " + me);
        }
    }

    /**
     * @return the singleton MongoConnections
     */
    public static MongoClientKeeper getInstance() {
        if (instance == null) {
            instance = new MongoClientKeeper();
        }
        return instance;
    }
}
