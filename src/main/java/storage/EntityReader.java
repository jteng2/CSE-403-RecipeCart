/* (C)2023 */
package storage;

import entities.*;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * This interface represents the place where entities are loaded into the Business Logic Layer, from
 * when they were saved. This interface is implemented by classes outside this layer, where they
 * deal with the underlying data representation of what's "saved" in this layer.
 */
public interface EntityReader {
    /**
     * Loads saved Tags with the given names.
     *
     * @param names the exact names of the Tags to be loaded
     * @throws IOException if, for any name, the Tag with the given name failed to be found
     * @throws IllegalArgumentException if names or any of its elements are null
     * @return for each name given, the respective saved Tag with that name
     */
    @NotNull List<@NotNull Tag> getTagsByName(@NotNull List<@NotNull String> names) throws IOException;

    /**
     * Loads saved Ingredients with the given names.
     *
     * @param names the exact names of the Ingredients to be loaded
     * @throws IOException if, for any name, the Ingredient with the given name failed to be found
     * @throws IllegalArgumentException if names or any of its elements are null
     * @return for each name given, the respective saved Ingredient with that name
     */
    @NotNull List<@NotNull Ingredient> getIngredientsByName(@NotNull List<@NotNull String> names)
            throws IOException;

    /**
     * Loads saved Recipes with the given names.
     *
     * @param names the exact names of the Recipes to be loaded
     * @throws IOException if, for any name, the Recipe with the given name failed to be found
     * @throws IllegalArgumentException if names or any of its elements are null
     * @return for each name given, the respective saved Recipe with that name
     */
    @NotNull List<@NotNull Recipe> getRecipesByName(@NotNull List<@NotNull String> names) throws IOException;

    /**
     * Loads saved Users with the given names.
     *
     * @param usernames the exact names of the Users to be loaded
     * @throws IOException if, for any name, the User with the given name failed to be found
     * @throws IllegalArgumentException if names or any of its elements are null
     * @return for each name given, the respective saved User with that name
     */
    @NotNull List<@NotNull User> getUsersByName(@NotNull List<@NotNull String> usernames) throws IOException;

    /**
     * Checks if a Tag with the given name exists.
     *
     * @throws IllegalArgumentException if name is null
     * @return true if a saved Tag with the given name exists, false otherwise
     */
    boolean tagNameExists(@NotNull String name);

    /**
     * Checks if an Ingredient with the given name exists.
     *
     * @throws IllegalArgumentException if name is null
     * @return true if a saved Ingredient with the given name exists, false otherwise
     */
    boolean ingredientNameExists(@NotNull String name);

    /**
     * Checks if a Recipe with the given name (not presentation name) exists.
     *
     * @throws IllegalArgumentException if name is null
     * @return true if a saved Recipe with the given name exists, false otherwise
     */
    boolean recipeNameExists(@NotNull String name);

    /**
     * Checks if a User with the given username exists.
     *
     * @throws IllegalArgumentException if name is null
     * @return true if a saved User with the given username exists, false otherwise
     */
    boolean usernameExists(@NotNull String name);

    /**
     * Searches for saved Tags whose names contain at least one of the given tokens.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Tags whose names matched, or an empty Collection if none matched
     */
    @NotNull Collection<@NotNull Tag> searchTags(@NotNull List<@NotNull String> tokens);

    /**
     * Searches for saved Ingredients whose names contain at least one of the given tokens.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Ingredients whose names matched, or an empty Collection if none matched
     */
    @NotNull Collection<@NotNull Ingredient> searchIngredients(@NotNull List<@NotNull String> tokens);

    /**
     * Searches for saved Recipes whose names (name or presentation name) contain at least one of
     * the given tokens.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Recipes whose names matched, or an empty Collection if none matched
     */
    @NotNull Collection<@NotNull Recipe> searchRecipes(@NotNull List<@NotNull String> tokens);

    /**
     * Searches for saved Users whose names contain at least one of the given tokens.
     *
     * @param tokens the tokens for name-matching
     * @return the saved Users whose names matched, or an empty Collection if none matched
     */
    @NotNull Collection<@NotNull User> searchUsers(@NotNull List<@NotNull String> tokens);
}
