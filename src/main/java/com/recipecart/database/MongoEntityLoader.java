/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents a place to load various RecipeCart entities from a Mongo database (that
 * houses such entities) into memory. This class intends to be read-only for the database.
 */
public class MongoEntityLoader extends MongoConnector implements EntityLoader {

    /** {@inheritDoc} */
    public MongoEntityLoader(String filename) throws FileNotFoundException {
        super(filename);
    }

    /**
     * Loads saved Tags with the given names from the database this loader is connected to.
     *
     * @param names the exact names of the tags to be loaded
     * @throws IOException if, for any name, the tag with the given name failed to be found
     * @return for each name given, the respective saved Tag with that name
     */
    @Override
    public @NotNull List<@NotNull Tag> getTagsByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        throw new NotImplementedException();
    }

    /**
     * Loads saved Ingredients with the given names from the database this loader is connected to.
     *
     * @param names the exact names of the ingredients to be loaded
     * @throws IOException if, for any name, the ingredient with the given name failed to be found
     * @return for each name given, the respective saved Ingredient with that name
     */
    @Override
    public @NotNull List<@NotNull Ingredient> getIngredientsByNames(
            @NotNull List<@NotNull String> names) throws IOException {
        throw new NotImplementedException();
    }

    /**
     * Loads saved Recipes with the given names from the database this loader is connected to.
     *
     * @param names the exact names of the recipes to be loaded
     * @throws IOException if, for any name, the recipe with the given name failed to be found
     * @return for each name given, the respective saved Recipe with that name
     */
    @Override
    public @NotNull List<@NotNull Recipe> getRecipesByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        throw new NotImplementedException();
    }

    /**
     * Loads saved Users with the given names from the database this loader is connected to.
     *
     * @param usernames the exact usernames of the users to be loaded
     * @throws IOException if, for any name, the user with the given name failed to be found
     * @return for each name given, the respective saved User with that name
     */
    @Override
    public @NotNull List<@NotNull User> getUsersByNames(@NotNull List<@NotNull String> usernames)
            throws IOException {
        throw new NotImplementedException();
    }

    /**
     * Checks if a tag with the given name exists in the database this loader is connected to.
     *
     * @return true if a saved tag with the given name exists, false otherwise
     */
    @Override
    public boolean tagNameExists(@NotNull String name) {
        throw new NotImplementedException();
    }

    /**
     * Checks if an ingredient with the given name exists in the database this loader is connected
     * to.
     *
     * @return true if a saved ingredient with the given name exists, false otherwise
     */
    @Override
    public boolean ingredientNameExists(@NotNull String name) {
        throw new NotImplementedException();
    }

    /**
     * Checks if a recipe with the given name exists in the database this loader is connected to.
     *
     * @return true if a saved recipe with the given name exists, false otherwise
     */
    @Override
    public boolean recipeNameExists(@NotNull String name) {
        throw new NotImplementedException();
    }

    /**
     * Checks if a user with the given name exists in the database this loader is connected to.
     *
     * @return true if a saved user with the given name exists, false otherwise
     */
    @Override
    public boolean usernameExists(@NotNull String name) {
        throw new NotImplementedException();
    }

    /**
     * Searches for saved tags whose names contain at least one of the given tokens, in the database
     * this loader is connected to.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Tags whose names matched, or an empty Set if none matched
     */
    @Override
    public @NotNull Set<@NotNull Tag> searchTags(@NotNull Set<@NotNull String> tokens) {
        throw new NotImplementedException();
    }

    /**
     * Searches for saved ingredients whose names contain at least one of the given tokens, in the
     * database this loader is connected to.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Ingredients whose names matched, or an empty Set if none matched
     */
    @Override
    public @NotNull Set<@NotNull Ingredient> searchIngredients(
            @NotNull Set<@NotNull String> tokens) {
        throw new NotImplementedException();
    }

    /**
     * Searches for saved recipes whose names contain at least one of the given tokens, in the
     * database this loader is connected to.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Recipes whose names matched, or an empty Set if none matched
     */
    @Override
    public @NotNull Set<@NotNull Recipe> searchRecipes(@NotNull Set<@NotNull String> tokens) {
        throw new NotImplementedException();
    }

    /**
     * Searches for saved users whose names contain at least one of the given tokens, in the
     * database this loader is connected to.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Users whose names matched, or an empty Set if none matched
     */
    @Override
    public @NotNull Set<@NotNull User> searchUsers(@NotNull Set<@NotNull String> tokens) {
        throw new NotImplementedException();
    }

    /** {@inheritDoc} */
    @Override
    public @NotNull String generateUniqueRecipeName(@Nullable String presentationName) {
        throw new NotImplementedException();
    }

    // possible helper functions that can help with the implementation of EntityLoader

    private @NotNull Tag documentToTag(@NotNull Document tag) {
        throw new NotImplementedException();
    }

    private @NotNull Ingredient documentToIngredient(@NotNull Document ingredient) {
        throw new NotImplementedException();
    }

    private @NotNull Recipe documentToRecipe(@NotNull Document recipe) {
        throw new NotImplementedException();
    }

    private @NotNull User documentToUser(@NotNull Document user) {
        throw new NotImplementedException();
    }

    private @NotNull Tag idToTag(@NotNull ObjectId tag) {
        throw new NotImplementedException();
    }

    private @NotNull Ingredient idToIngredient(@NotNull ObjectId ingredient) {
        throw new NotImplementedException();
    }

    private @NotNull Recipe idToRecipe(@NotNull ObjectId recipe) {
        throw new NotImplementedException();
    }

    private @NotNull User idToUser(@NotNull ObjectId user) {
        throw new NotImplementedException();
    }
}
