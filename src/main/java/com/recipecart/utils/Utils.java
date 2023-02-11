/* (C)2023 */
package com.recipecart.utils;

import com.recipecart.entities.*;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/** This class contains general utility methods to help with the implementation of RecipeCart. */
public class Utils {
    /**
     * Throws an exception if the given collection is null or has null values.
     *
     * @param collection the collection to null-check
     * @param collectionNullMessage the exception's message if the collection is null
     * @param elementNullMessage the exception's message if an element is null
     * @param <T> the element type of the collection
     * @throws NullPointerException if there are any nulls
     */
    public static <T> void requireAllNotNull(
            Collection<T> collection, String collectionNullMessage, String elementNullMessage) {
        Objects.requireNonNull(collection, collectionNullMessage);
        for (T element : collection) {
            Objects.requireNonNull(element, elementNullMessage);
        }
    }

    /**
     * Throws an exception if the given map is null or has null values.
     *
     * @param map the map to null-check
     * @param mapNullMessage the exception's message if the map is null
     * @param keyNullMessage the exception's message if a key is null
     * @param valueNullMessage the exception's message if a value is null
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @throws NullPointerException if there are any nulls
     */
    public static <K, V> void requireAllMapNotNull(
            Map<K, V> map, String mapNullMessage, String keyNullMessage, String valueNullMessage) {
        Objects.requireNonNull(map, mapNullMessage);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Objects.requireNonNull(entry.getKey(), keyNullMessage);
            Objects.requireNonNull(entry.getValue(), valueNullMessage);
        }
    }

    /**
     * Throws an exception if any tag names are null.
     *
     * @param tags the Tags to check
     * @throws IllegalArgumentException if any tags have null names
     */
    public static void nullCheckTagNames(Collection<Tag> tags) {
        for (Tag t : tags) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Tag names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given tag has a null name.
     *
     * @param tag the Tag to null-check
     * @throws IllegalArgumentException if the tag is null
     */
    public static void nullCheckTagName(Tag tag) {
        nullCheckTagNames(Collections.singletonList(tag));
    }

    /**
     * Throws an exception if any ingredient names are null.
     *
     * @param ingredients the Ingredients to check
     * @throws IllegalArgumentException if any ingredients have null names
     */
    public static void nullCheckIngredientNames(Collection<Ingredient> ingredients) {
        for (Ingredient t : ingredients) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Ingredient names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given ingredient has a null name.
     *
     * @param ingredient the Ingredient to null-check
     * @throws IllegalArgumentException if the ingredient is null
     */
    public static void nullCheckIngredientName(Ingredient ingredient) {
        nullCheckIngredientNames(Collections.singletonList(ingredient));
    }

    /**
     * Throws an exception if any recipe names are null.
     *
     * @param recipes the Recipes to check
     * @throws IllegalArgumentException if any recipes have null names
     */
    public static void nullCheckRecipeNames(Collection<Recipe> recipes) {
        for (Recipe t : recipes) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Recipe names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given recipe has a null name.
     *
     * @param recipe the Recipe to null-check
     * @throws IllegalArgumentException if the recipe is null
     */
    public static void nullCheckRecipeName(Recipe recipe) {
        nullCheckRecipeNames(Collections.singletonList(recipe));
    }

    /**
     * Throws an exception if any usernames are null.
     *
     * @param users the Users to check
     * @throws IllegalArgumentException if any users have null names
     */
    public static void nullCheckUserNames(Collection<User> users) {
        for (User t : users) {
            if (t.getUsername() == null) {
                throw new IllegalArgumentException("User names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given user has a null name.
     *
     * @param user the User to null-check
     * @throws IllegalArgumentException if the user is null
     */
    public static void nullCheckUserName(User user) {
        nullCheckUserNames(Collections.singletonList(user));
    }
}
