/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.*;
import com.recipecart.storage.EntitySaver;
import java.io.FileNotFoundException;
import java.util.Collection;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a place to save various RecipeCart entities into a Mongo database (that
 * houses such entities). While this class isn't strictly write-only to the database, its intention
 * is purely to write entities to the database.
 */
public class MongoEntitySaver extends MongoConnector implements EntitySaver {

    /** {@inheritDoc} */
    public MongoEntitySaver(String filename) throws FileNotFoundException {
        super(filename);
    }

    /**
     * Saves the given Tags to the Mongo database this saver is connected to.
     *
     * @param tags Tags that need to be saved
     * @throws IllegalArgumentException if any names of the Tags are null
     */
    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        throw new NotImplementedException();
    }

    /**
     * Saves the given Ingredients to the Mongo database this saver is connected to.
     *
     * @param ingredients Ingredients that need to be saved
     * @throws IllegalArgumentException if any names of the Ingredients are null
     */
    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        throw new NotImplementedException();
    }

    /**
     * Saves the given Recipes to the Mongo database this saver is connected to.
     *
     * @param recipes Recipes that need to be saved
     * @throws IllegalArgumentException if any (non-presentation) names of the Recipes are null
     */
    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        throw new NotImplementedException();
    }

    /**
     * Saves the given Users to the Mongo database this saver is connected to.
     *
     * @param users Users that need to be saved
     * @throws IllegalArgumentException if any usernames of the Users are null
     */
    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        throw new NotImplementedException();
    }

    // possible helper functions that can help with the implementation of EntitySaver

    private Document tagToDocument(@NotNull Tag tag) {
        throw new NotImplementedException();
    }

    private Document ingredientToDocument(@NotNull Ingredient ingredient) {
        throw new NotImplementedException();
    }

    private Document recipeToDocument(@NotNull Recipe recipe) {
        throw new NotImplementedException();
    }

    private Document userToDocument(@NotNull User user) {
        throw new NotImplementedException();
    }

    private @NotNull ObjectId tagToId(@NotNull Tag tags) {
        throw new NotImplementedException();
    }

    private @NotNull ObjectId ingredientToId(@NotNull Ingredient ingredient) {
        throw new NotImplementedException();
    }

    private @NotNull ObjectId recipeToId(@NotNull Recipe recipe) {
        throw new NotImplementedException();
    }

    private @NotNull ObjectId userToId(@NotNull User user) {
        throw new NotImplementedException();
    }
}
