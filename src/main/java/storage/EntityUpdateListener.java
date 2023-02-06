package storage;

import entities.*;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * This interface represents the place where entities are saved in the Business Logic Layer.
 * This interface is implemented by classes outside this layer, where they deal with the underlying
 * data representation of what's "saved" in this layer.
 */
public interface EntityUpdateListener {

    /**
     * Performs necessary changes to the underlying data representation
     * to reflect changes in the Tags that are "saved".
     *
     * @param tags Tags that need to be saved
     * @throws IllegalArgumentException if tags, any of its elements,
     *      or any names of the Tags are null
     */
    void onTagsUpdated(@NotNull Collection<@NotNull Tag> tags);

    /**
     * Performs necessary changes to the underlying data representation
     * to reflect changes in the Ingredients that are "saved".
     *
     * @param ingredients Ingredients that need to be saved
     * @throws IllegalArgumentException if ingredients, any of its elements,
     *      or any names of the Ingredients are null
     */
    void onIngredientsUpdated(@NotNull Collection<@NotNull Ingredient> ingredients);

    /**
     * Performs necessary changes to the underlying data representation
     * to reflect changes in the Recipes that are "saved".
     *
     * @param recipes Recipes that need to be saved
     * @throws IllegalArgumentException if recipes, any of its elements,
     *      or any names of the Recipes are null
     */
    void onRecipesUpdated(@NotNull Collection<@NotNull Recipe> recipes);

    /**
     * Performs necessary changes to the underlying data representation
     * to reflect changes in the Users that are "saved".
     *
     * @param users Users that need to be saved
     * @throws IllegalArgumentException if users, any of its elements,
     *      or any usernames of the Users are null
     */
    void onUsersUpdated(@NotNull Collection<@NotNull User> users);
}
