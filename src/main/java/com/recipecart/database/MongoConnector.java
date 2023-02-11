/* (C)2023 */
package com.recipecart.database;

import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;

/**
 * This class represents a place to read/write various RecipeCart entities from/to a Mongo database
 * that houses such entities.
 */
public class MongoConnector {

    /**
     * Creates a MongoConnector that's connected to the database at the given host address.
     *
     * @param hostAddress the host address of the database to connect to
     */
    public MongoConnector(ServerAddress hostAddress) {
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