/* (C)2023 */
package com.recipecart.database;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import java.io.FileNotFoundException;
import java.io.FileReader;
import org.bson.Document;

/**
 * This class represents a place to read/write various RecipeCart entities from/to a Mongo database
 * that houses such entities.
 */
public class MongoConnector {
    private final MongoCollection<Document> tags, ingredients, recipes, users;

    /**
     * Creates a MongoConnector that's connected to the database whose address is in the given file.
     * The file needs to be a JSON file, with the format of:
     *
     * <pre>{@code
     * {
     *      ...
     *      "connection": "connection string to the db goes here",
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
        JsonObject dbDetails = gson.fromJson(new FileReader(filename), JsonObject.class);

        ConnectionString host = new ConnectionString(dbDetails.get("connection").getAsString());
        MongoClient client = MongoClientKeeper.getInstance().getConnectionTo(host);

        tags = getCollectionFromJson(dbDetails, client, "tags");
        ingredients = getCollectionFromJson(dbDetails, client, "ingredients");
        recipes = getCollectionFromJson(dbDetails, client, "recipes");
        users = getCollectionFromJson(dbDetails, client, "users");
    }

    private static MongoCollection<Document> getCollectionFromJson(
            JsonObject dbDetails, MongoClient client, String key) {
        JsonArray dbAndCollection = dbDetails.get(key).getAsJsonArray();
        String database = dbAndCollection.get(0).getAsString();
        String collection = dbAndCollection.get(1).getAsString();

        return client.getDatabase(database).getCollection(collection);
    }

    protected MongoCollection<Document> getTagCollection() {
        return tags;
    }

    protected MongoCollection<Document> getIngredientCollection() {
        return ingredients;
    }

    protected MongoCollection<Document> getRecipeCollection() {
        return recipes;
    }

    protected MongoCollection<Document> getUserCollection() {
        return users;
    }
}
