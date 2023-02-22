/* (C)2023 */
package com.recipecart.database;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import org.bson.Document;

/**
 * This class represents a place to read/write various RecipeCart entities from/to a Mongo database
 * that houses such entities.
 */
public class MongoConnector {

    /**
     * Creates a MongoConnector that's connected to the database whose address is in the given file.
     * The file needs to be a JSON file, with the format of:
     *
     * <pre>{@code
     * {
     *      ...
     *      "hostname": "host address of the db goes here",
     *      "port": "port of the db goes here",
     *      "tags": ["database name", "collection name"],
     *      "ingredients": ["database name", "collection name"],
     *      "recipes": ["database name", "collection name"],
     *      "users": ["database name", "collection name"],
     *      ...
     * }
     * }</pre>
     *
     * @param filename the file with the database address details
     */
    public MongoConnector(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader(filename), JsonObject.class);

        String hostname = json.get("hostname").getAsString();
        int port = json.get("port").getAsInt();
        String databaseName = json.get("database").getAsString();
        List<String> tagCollectionName = gson.fromJson(json.get("tags"), List.class);
        List<String> ingredientCollectionName = gson.fromJson(json.get("ingredients"), List.class);
        List<String> recipeCollectionName = gson.fromJson(json.get("recipes"), List.class);
        List<String> userCollectionName = gson.fromJson(json.get("users"), List.class);

        MongoClient mongoClient = MongoClients.create("mongodb://" + hostname + ":" + port);
        MongoDatabase database = mongoClient.getDatabase(databaseName);
        MongoCollection<Document> tagCollection = database.getCollection(tagCollectionName.get(1));
        MongoCollection<Document> ingredientCollection =
                database.getCollection(ingredientCollectionName.get(1));
        MongoCollection<Document> recipeCollection =
                database.getCollection(recipeCollectionName.get(1));
        MongoCollection<Document> userCollection =
                database.getCollection(userCollectionName.get(1));
    }
    // possible helper functions that can help with implementations of MongoConnector

    MongoCollection<Document> getTagCollection() {
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            database = mongoClient.getDatabase("myDatabase");
        }
        return database.getCollection("tags");
    }

    MongoCollection<Document> getIngredientCollection() {
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            database = mongoClient.getDatabase("myDatabase");
        }
        return database.getCollection("ingredients");
    }

    MongoCollection<Document> getRecipeCollection() {
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            database = mongoClient.getDatabase("myDatabase");
        }
        return database.getCollection("recipes");
    }

    MongoCollection<Document> getUserCollection() {
        MongoDatabase database;
        try (MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017")) {
            database = mongoClient.getDatabase("myDatabase");
        }
        return database.getCollection("users");
    }
}
