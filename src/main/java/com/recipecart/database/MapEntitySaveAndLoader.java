/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class implements the storage EntitySaver and EntityLoader using a Map. */
public class MapEntitySaveAndLoader implements EntitySaver, EntityLoader {
    private final ReadWriteLock tagLock = new ReentrantReadWriteLock(),
            ingredientLock = new ReentrantReadWriteLock(),
            recipeLock = new ReentrantReadWriteLock(),
            userLock = new ReentrantReadWriteLock();
    private final Lock tagReadLock = tagLock.readLock(),
            ingredientReadLock = ingredientLock.readLock(),
            recipeReadLock = recipeLock.readLock(),
            userReadLock = userLock.readLock(),
            tagWriteLock = tagLock.writeLock(),
            ingredientWriteLock = ingredientLock.writeLock(),
            recipeWriteLock = recipeLock.writeLock(),
            userWriteLock = userLock.writeLock();

    private final Map<String, Tag> savedTags;
    private final Map<String, Ingredient> savedIngredients;
    private final Map<String, Recipe> savedRecipes;
    private final Map<String, User> savedUsers;

    public MapEntitySaveAndLoader() {
        this.savedTags = new ConcurrentHashMap<>();
        this.savedIngredients = new ConcurrentHashMap<>();
        this.savedRecipes = new ConcurrentHashMap<>();
        this.savedUsers = new ConcurrentHashMap<>();
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
        List<Tag> tags;
        tagReadLock.lock();
        try {
            tags = getByIds(names, savedTags);
        } finally {
            tagReadLock.unlock();
        }
        return tags;
    }

    @Override
    public @NotNull List<@NotNull Ingredient> getIngredientsByNames(
            @NotNull List<@NotNull String> names) throws IOException {
        List<Ingredient> ingredients;
        ingredientReadLock.lock();
        try {
            ingredients = getByIds(names, savedIngredients);
        } finally {
            ingredientReadLock.unlock();
        }
        return ingredients;
    }

    @Override
    public @NotNull List<@NotNull Recipe> getRecipesByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        List<Recipe> recipes;
        recipeReadLock.lock();
        try {
            recipes = getByIds(names, savedRecipes);
        } finally {
            recipeReadLock.unlock();
        }
        return recipes;
    }

    @Override
    public @NotNull List<@NotNull User> getUsersByNames(@NotNull List<@NotNull String> usernames)
            throws IOException {
        List<User> users;
        userReadLock.lock();
        try {
            users = getByIds(usernames, savedUsers);
        } finally {
            userReadLock.unlock();
        }
        return users;
    }

    @Override
    public boolean tagNameExists(@NotNull String name) {
        Objects.requireNonNull(name);

        boolean exists;
        tagReadLock.lock();
        try {
            exists = savedTags.containsKey(name);
        } finally {
            tagReadLock.unlock();
        }
        return exists;
    }

    @Override
    public boolean ingredientNameExists(@NotNull String name) {
        Objects.requireNonNull(name);

        boolean exists;
        ingredientReadLock.lock();
        try {
            exists = savedIngredients.containsKey(name);
        } finally {
            ingredientReadLock.unlock();
        }
        return exists;
    }

    @Override
    public boolean recipeNameExists(@NotNull String name) {
        Objects.requireNonNull(name);

        boolean exists;
        recipeReadLock.lock();
        try {
            exists = savedRecipes.containsKey(name);
        } finally {
            recipeReadLock.unlock();
        }
        return exists;
    }

    @Override
    public boolean usernameExists(@NotNull String name) {
        Objects.requireNonNull(name);

        boolean exists;
        userReadLock.lock();
        try {
            exists = savedUsers.containsKey(name);
        } finally {
            userReadLock.unlock();
        }
        return exists;
    }

    private static Set<String> toLowerCaseStrings(Collection<String> strings) {
        Set<String> lowerCaseStrings = new HashSet<>();
        for (String s : strings) {
            lowerCaseStrings.add(s.toLowerCase(Locale.ROOT));
        }
        return lowerCaseStrings;
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
            Collection<String> toSearch, @NotNull Set<@NotNull String> tokens, boolean ignoreCase) {
        Utils.requireAllNotNull(tokens, "Tokens set cannot be null", "Tokens cannot be null");

        Set<String> tokensToUse = ignoreCase ? toLowerCaseStrings(tokens) : tokens;

        Set<String> matches = new HashSet<>();
        for (String ts : toSearch) {
            if (matches(ignoreCase ? ts.toLowerCase(Locale.ROOT) : ts, tokensToUse)) {
                matches.add(ts);
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
    public @NotNull Set<@NotNull Tag> searchTags(@NotNull Set<@NotNull String> tokens) {
        Set<Tag> matchedTags;
        tagReadLock.lock();
        try {
            Set<String> matchedNames = findMatches(savedTags.keySet(), tokens, true);
            matchedTags = getValuesOf(matchedNames, savedTags);
        } finally {
            tagReadLock.unlock();
        }
        return matchedTags;
    }

    @Override
    public @NotNull Set<@NotNull Ingredient> searchIngredients(
            @NotNull Set<@NotNull String> tokens) {
        Set<Ingredient> matchedIngredients;
        ingredientReadLock.lock();
        try {
            Set<String> matchedNames = findMatches(savedIngredients.keySet(), tokens, true);
            matchedIngredients = getValuesOf(matchedNames, savedIngredients);
        } finally {
            ingredientReadLock.unlock();
        }
        return matchedIngredients;
    }

    private static boolean matchesPresentationName(
            Recipe recipe, @NotNull Set<@NotNull String> tokens, boolean ignoreCase) {
        Set<String> tokensToUse = ignoreCase ? toLowerCaseStrings(tokens) : tokens;

        String presName = recipe.getPresentationName();
        if (presName != null) {
            presName = ignoreCase ? presName.toLowerCase(Locale.ROOT) : presName;
            return matches(presName, tokensToUse);
        }
        return false;
    }

    @Override
    public @NotNull Set<@NotNull Recipe> searchRecipes(@NotNull Set<@NotNull String> tokens) {
        Set<Recipe> matchedRecipes;
        final boolean ignoreCase = true;
        recipeReadLock.lock();
        try {
            Set<String> matchedNames = findMatches(savedRecipes.keySet(), tokens, ignoreCase);
            matchedRecipes = getValuesOf(matchedNames, savedRecipes);
            Set<Recipe> matchedPresentationRecipes =
                    savedRecipes.values().stream()
                            .filter((recipe) -> matchesPresentationName(recipe, tokens, ignoreCase))
                            .collect(Collectors.toSet());

            matchedRecipes.addAll(matchedPresentationRecipes);
        } finally {
            recipeReadLock.unlock();
        }
        return matchedRecipes;
    }

    @Override
    public @NotNull Set<@NotNull User> searchUsers(@NotNull Set<@NotNull String> tokens) {
        Set<User> matchedUsers;
        userReadLock.lock();
        try {
            Set<String> matchedNames = findMatches(savedUsers.keySet(), tokens, true);
            matchedUsers = getValuesOf(matchedNames, savedUsers);
        } finally {
            userReadLock.unlock();
        }
        return matchedUsers;
    }

    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        Utils.requireAllNotNull(
                tags, "Tag collection cannot be null", "Elements of tags cannot be null");
        Utils.nullCheckTagNames(tags);

        tagWriteLock.lock();
        try {
            for (Tag tag : tags) {
                savedTags.put(tag.getName(), tag);
            }
        } finally {
            tagWriteLock.unlock();
        }
    }

    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        Utils.requireAllNotNull(
                ingredients,
                "Ingredient collection cannot be null",
                "Elements of ingredients cannot be null");
        Utils.nullCheckIngredientNames(ingredients);

        ingredientWriteLock.lock();
        try {
            for (Ingredient ingredient : ingredients) {
                savedIngredients.put(ingredient.getName(), ingredient);
            }
        } finally {
            ingredientWriteLock.unlock();
        }
    }

    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        Utils.requireAllNotNull(
                recipes, "Recipe collection cannot be null", "Elements of recipes cannot be null");
        Utils.nullCheckRecipeNames(recipes);

        recipeWriteLock.lock();
        try {
            for (Recipe recipe : recipes) {
                savedRecipes.put(recipe.getName(), recipe);
            }
        } finally {
            recipeWriteLock.unlock();
        }
    }

    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        Utils.requireAllNotNull(
                users, "User collection cannot be null", "Elements of users cannot be null");
        Utils.nullCheckUserNames(users);

        userWriteLock.lock();
        try {
            for (User user : users) {
                savedUsers.put(user.getUsername(), user);
            }
        } finally {
            userWriteLock.unlock();
        }
    }

    @Override
    public @NotNull String generateUniqueRecipeName(@Nullable String presentationName) {
        String baseName;

        recipeReadLock.lock();
        try {
            if (presentationName == null) {
                baseName = "";
            } else {
                baseName = presentationName.trim().replaceAll("\\s+", "-");
                if (!recipeNameExists(baseName)) {
                    return baseName;
                }
            }

            for (long i = 0; ; i++) {
                String generatedName = baseName + i;
                if (!recipeNameExists(generatedName)) {
                    return generatedName;
                }
            }
        } finally {
            recipeReadLock.unlock();
        }
    }
}
