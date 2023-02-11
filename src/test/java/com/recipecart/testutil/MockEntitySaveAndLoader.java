/* (C)2023 */
package com.recipecart.testutil;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import com.recipecart.utils.Utils;
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
        Utils.requireAllNotNull(
                ids,
                "Identifying name list cannot be null",
                "Elements in identifying names cannot be null");
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
        Objects.requireNonNull(name);
        return savedTags.containsKey(name);
    }

    @Override
    public boolean ingredientNameExists(@NotNull String name) {
        Objects.requireNonNull(name);
        return savedIngredients.containsKey(name);
    }

    @Override
    public boolean recipeNameExists(@NotNull String name) {
        Objects.requireNonNull(name);
        return savedRecipes.containsKey(name);
    }

    @Override
    public boolean usernameExists(@NotNull String name) {
        Objects.requireNonNull(name);
        return savedUsers.containsKey(name);
    }

    private static boolean matches(String toExamine, Set<String> tokens) {
        for (String word : toExamine.split("\\s+")) {
            if (tokens.contains(word)) {
                return true;
            }
        }
        return false;
    }

    private static Set<String> findMatches(
            Collection<String> toSearch, @NotNull Set<@NotNull String> tokens) {
        Utils.requireAllNotNull(tokens, "Tokens set cannot be null", "Tokens cannot be null");
        Set<String> matches = new HashSet<>();
        for (String ts : toSearch) {
            if (matches(ts, tokens)) {
                matches.add(ts);
            }
        }
        return matches;
    }

    private static <K> Set<K> findKeysOfMatchingValues(
            Map<K, String> toSearch, Set<String> tokens) {
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
    public @NotNull Collection<@NotNull Tag> searchTags(@NotNull Set<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedTags.keySet(), tokens);
        return getValuesOf(matchedNames, savedTags);
    }

    @Override
    public @NotNull Collection<@NotNull Ingredient> searchIngredients(
            @NotNull Set<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedIngredients.keySet(), tokens);
        return getValuesOf(matchedNames, savedIngredients);
    }

    @Override
    public @NotNull Collection<@NotNull Recipe> searchRecipes(
            @NotNull Set<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedRecipes.keySet(), tokens);
        Set<Recipe> matchedRecipes = getValuesOf(matchedNames, savedRecipes);
        matchedRecipes.addAll(findKeysOfMatchingValues(recipePresentationNames, tokens));
        return matchedRecipes;
    }

    @Override
    public @NotNull Collection<@NotNull User> searchUsers(@NotNull Set<@NotNull String> tokens) {
        Set<String> matchedNames = findMatches(savedUsers.keySet(), tokens);
        return getValuesOf(matchedNames, savedUsers);
    }

    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        Utils.requireAllNotNull(
                tags, "Tag collection cannot be null", "Elements of tags cannot be null");
        Utils.nullCheckTagNames(tags);
        for (Tag tag : tags) {
            savedTags.put(tag.getName(), tag);
        }
    }

    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        Utils.requireAllNotNull(
                ingredients,
                "Ingredient collection cannot be null",
                "Elements of ingredients cannot be null");
        Utils.nullCheckIngredientNames(ingredients);
        for (Ingredient ingredient : ingredients) {
            savedIngredients.put(ingredient.getName(), ingredient);
        }
    }

    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        Utils.requireAllNotNull(
                recipes, "Recipe collection cannot be null", "Elements of recipes cannot be null");
        Utils.nullCheckRecipeNames(recipes);
        for (Recipe recipe : recipes) {
            savedRecipes.put(recipe.getName(), recipe);
            recipePresentationNames.put(recipe, recipe.getPresentationName());
        }
    }

    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        Utils.requireAllNotNull(
                users, "User collection cannot be null", "Elements of users cannot be null");
        Utils.nullCheckUserNames(users);
        for (User user : users) {
            savedUsers.put(user.getUsername(), user);
        }
    }
}
