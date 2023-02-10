/* (C)2023 */
package com.recipecart.testutil;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.io.IOException;
import java.util.*;
import org.jetbrains.annotations.NotNull;

/**
 * This class implements the storage EntitySaver and EntityLoader using a Map. This class is meant
 * to be used for testing other parts of the backend.
 */
public class MockEntitySaveAndLoader implements EntitySaver, EntityLoader {
    private final Map<String, Tag> savedTags;
    private final Map<String, Ingredient> savedIngredients;
    private final Map<String, Recipe> savedRecipes;
    private final Map<Recipe, String> recipePresentationNames;
    private final Map<String, User> savedUsers;

    public MockEntitySaveAndLoader() {
        this.savedTags = new HashMap<>();
        this.savedIngredients = new HashMap<>();
        this.savedRecipes = new HashMap<>();
        this.recipePresentationNames = new HashMap<>();
        this.savedUsers = new HashMap<>();
    }

    private static <K, V> List<V> getByIds(@NotNull List<@NotNull K> ids, Map<K, V> saved)
            throws IOException {
        List<V> matched = new ArrayList<>(ids.size());
        for (K id : ids) {
            if (!saved.containsKey(id)) {
                throw new IOException("Entry not found");
            }
            matched.add(saved.get(id));
        }
        return matched;
    }

    @Override
    public @NotNull List<@NotNull Tag> getTagsByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        return getByIds(names, savedTags);
    }

    @Override
    public @NotNull List<@NotNull Ingredient> getIngredientsByNames(
            @NotNull List<@NotNull String> names) throws IOException {
        return getByIds(names, savedIngredients);
    }

    @Override
    public @NotNull List<@NotNull Recipe> getRecipesByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        return getByIds(names, savedRecipes);
    }

    @Override
    public @NotNull List<@NotNull User> getUsersByNames(@NotNull List<@NotNull String> usernames)
            throws IOException {
        return getByIds(usernames, savedUsers);
    }

    @Override
    public boolean tagNameExists(@NotNull String name) {
        return savedUsers.containsKey(name);
    }

    @Override
    public boolean ingredientNameExists(@NotNull String name) {
        return savedIngredients.containsKey(name);
    }

    @Override
    public boolean recipeNameExists(@NotNull String name) {
        return savedRecipes.containsKey(name);
    }

    @Override
    public boolean usernameExists(@NotNull String name) {
        return savedUsers.containsKey(name);
    }

    private static <T> boolean matches(T toExamine, Collection<T> tokens) {
        for (T token : tokens) {
            if (toExamine.equals(token)) {
                return true;
            }
        }
        return false;
    }

    private static <T> Set<T> findMatches(Collection<T> toSearch, Collection<T> tokens) {
        Set<T> matches = new HashSet<>();
        for (T ts : toSearch) {
            if (matches(ts, tokens)) {
                matches.add(ts);
            }
        }
        return matches;
    }

    private static <K, V> Set<K> findKeysOfMatchingValues(
            Map<K, V> toSearch, Collection<V> tokens) {
        Set<K> matches = new HashSet<>();
        for (K key : toSearch.keySet()) {
            if (matches(toSearch.get(key), tokens)) {
                matches.add(key);
            }
        }
        return matches;
    }

    private static <K, V> Set<V> getValuesOf(Set<K> keys, Map<K, V> map) {
        Set<V> values = new HashSet<>();
        for (K key : keys) {
            values.add(map.get(key));
        }
        return values;
    }

    @Override
    public @NotNull Collection<@NotNull Tag> searchTags(@NotNull List<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedTags.keySet(), tokens);
        return getValuesOf(matchedNames, savedTags);
    }

    @Override
    public @NotNull Collection<@NotNull Ingredient> searchIngredients(
            @NotNull List<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedIngredients.keySet(), tokens);
        return getValuesOf(matchedNames, savedIngredients);
    }

    @Override
    public @NotNull Collection<@NotNull Recipe> searchRecipes(
            @NotNull List<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedRecipes.keySet(), tokens);
        Set<Recipe> matchedRecipes = getValuesOf(matchedNames, savedRecipes);
        matchedRecipes.addAll(findKeysOfMatchingValues(recipePresentationNames, tokens));
        return matchedRecipes;
    }

    @Override
    public @NotNull Collection<@NotNull User> searchUsers(@NotNull List<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedUsers.keySet(), tokens);
        return getValuesOf(matchedNames, savedUsers);
    }

    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        for (Tag tag : tags) {
            savedTags.put(tag.getName(), tag);
        }
    }

    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            savedIngredients.put(ingredient.getName(), ingredient);
        }
    }

    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        for (Recipe recipe : recipes) {
            savedRecipes.put(recipe.getName(), recipe);
            recipePresentationNames.put(recipe, recipe.getPresentationName());
        }
    }

    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        for (User user : users) {
            savedUsers.put(user.getUsername(), user);
        }
    }
}
