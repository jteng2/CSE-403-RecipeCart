/* (C)2023 */
package com.recipecart.database;

import com.mongodb.client.MongoCollection;
import java.io.FileNotFoundException;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;

/**
 * This class represents a place to read/write various RecipeCart entities from/to a Mongo database
 * that houses such entities.
 */
public class MongoConnector {

    /**
     * Creates a MongoConnector that's connected to the database whose address is in the given file.
     * The file needs to be a JSON file, with the format of
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
        throw new NotImplementedException();
    }

    // possible helper functions that can help with implementations of MongoConnector

    MongoCollection<Document> getTagCollection() {
        throw new NotImplementedException();
    }

    MongoCollection<Document> getIngredientCollection() {
        throw new NotImplementedException();
    }

    MongoCollection<Document> getRecipeCollection() {
        throw new NotImplementedException();
    }

    MongoCollection<Document> getUserCollection() {
        throw new NotImplementedException();
    }
}
