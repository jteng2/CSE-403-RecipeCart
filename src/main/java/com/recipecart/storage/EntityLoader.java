/* (C)2023 */
package com.recipecart.storage;

import com.recipecart.entities.*;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This interface represents the place where entities are loaded into the Business Logic Layer, from
 * when they were saved. This interface is implemented by classes outside this layer, where they
 * deal with the underlying data representation of what's "saved" in this layer.
 */
public interface EntityLoader {
    /**
     * Loads saved Tags with the given names.
     *
     * @param names the exact names of the Tags to be loaded
     * @throws IOException if, for any name, the Tag with the given name failed to be found
     * @return for each name given, the respective saved Tag with that name
     */
    @NotNull List<@NotNull Tag> getTagsByNames(@NotNull List<@NotNull String> names) throws IOException;

    /**
     * Loads saved Ingredients with the given names.
     *
     * @param names the exact names of the Ingredients to be loaded
     * @throws IOException if, for any name, the Ingredient with the given name failed to be found
     * @return for each name given, the respective saved Ingredient with that name
     */
    @NotNull List<@NotNull Ingredient> getIngredientsByNames(@NotNull List<@NotNull String> names)
            throws IOException;

    /**
     * Loads saved Recipes with the given names.
     *
     * @param names the exact names of the Recipes to be loaded
     * @throws IOException if, for any name, the Recipe with the given name failed to be found
     * @return for each name given, the respective saved Recipe with that name
     */
    @NotNull List<@NotNull Recipe> getRecipesByNames(@NotNull List<@NotNull String> names)
            throws IOException;

    /**
     * Loads saved Users with the given names.
     *
     * @param usernames the exact usernames of the Users to be loaded
     * @throws IOException if, for any name, the User with the given name failed to be found
     * @return for each name given, the respective saved User with that name
     */
    @NotNull List<@NotNull User> getUsersByNames(@NotNull List<@NotNull String> usernames)
            throws IOException;

    /**
     * Checks if a tag with the given name exists.
     *
     * @return true if a saved tag with the given name exists, false otherwise
     */
    boolean tagNameExists(@NotNull String name);

    /**
     * Checks if an ingredient with the given name exists.
     *
     * @return true if a saved ingredient with the given name exists, false otherwise
     */
    boolean ingredientNameExists(@NotNull String name);

    /**
     * Checks if a recipe with the given name (not presentation name) exists.
     *
     * @return true if a saved recipe with the given name exists, false otherwise
     */
    boolean recipeNameExists(@NotNull String name);

    /**
     * Checks if a user with the given username exists.
     *
     * @return true if a saved user with the given username exists, false otherwise
     */
    boolean usernameExists(@NotNull String name);

    /**
     * Searches for saved tags whose names contain at least one of the given tokens
     * (case-insensitive).
     *
     * @param tokens the tokens for name-matching
     * @return the saved Tags whose names matched, or an empty Collection if none matched
     */
    @NotNull Set<@NotNull Tag> searchTags(@NotNull Set<@NotNull String> tokens);

    /**
     * Searches for saved ingredients whose names contain at least one of the given tokens
     * (case-insensitive).
     *
     * @param tokens the tokens for name-matching
     * @return the saved Ingredients whose names matched, or an empty Set if none matched
     */
    @NotNull Set<@NotNull Ingredient> searchIngredients(@NotNull Set<@NotNull String> tokens);

    /**
     * Searches for saved recipes whose names (name or presentation name) contain at least one of
     * the given tokens (case-insensitive).
     *
     * @param tokens the tokens for name-matching
     * @return the saved Recipes whose names matched, or an empty Set if none matched
     */
    @NotNull Set<@NotNull Recipe> searchRecipes(@NotNull Set<@NotNull String> tokens);

    /**
     * Searches for saved users whose names contain at least one of the given tokens
     * (case-insensitive).
     *
     * @param tokens the tokens for name-matching
     * @return the saved Users whose names matched, or an empty Set if none matched
     */
    @NotNull Set<@NotNull User> searchUsers(@NotNull Set<@NotNull String> tokens);

    /**
     * Generates a recipe non-presentation name (based on the given presentation name), such that no
     * other saved recipe has that same non-presentation name. If
     *
     * @param presentationName the name to base the generated name off of
     * @return the generated name
     */
    @NotNull String generateUniqueRecipeName(@Nullable String presentationName);
}
