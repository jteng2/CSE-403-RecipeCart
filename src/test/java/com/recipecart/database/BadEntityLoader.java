/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * An EntityLoader that simulates something bad happening in the data access layer, that prevents
 * the method from successfully executing (ex. the database failing).
 */
public class BadEntityLoader implements EntityLoader {
    @Override
    public @NotNull List<@NotNull Tag> getTagsByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        throw new RuntimeException();
    }

    @Override
    public @NotNull List<@NotNull Ingredient> getIngredientsByNames(
            @NotNull List<@NotNull String> names) throws IOException {
        throw new RuntimeException();
    }

    @Override
    public @NotNull List<@NotNull Recipe> getRecipesByNames(@NotNull List<@NotNull String> names)
            throws IOException {
        throw new RuntimeException();
    }

    @Override
    public @NotNull List<@NotNull User> getUsersByNames(@NotNull List<@NotNull String> usernames)
            throws IOException {
        throw new RuntimeException();
    }

    @Override
    public boolean tagNameExists(@NotNull String name) {
        throw new RuntimeException();
    }

    @Override
    public boolean ingredientNameExists(@NotNull String name) {
        throw new RuntimeException();
    }

    @Override
    public boolean recipeNameExists(@NotNull String name) {
        throw new RuntimeException();
    }

    @Override
    public boolean usernameExists(@NotNull String name) {
        throw new RuntimeException();
    }

    @Override
    public @NotNull Set<@NotNull Tag> searchTags(@NotNull Set<@NotNull String> tokens) {
        throw new RuntimeException();
    }

    @Override
    public @NotNull Set<@NotNull Ingredient> searchIngredients(
            @NotNull Set<@NotNull String> tokens) {
        throw new RuntimeException();
    }

    @Override
    public @NotNull Set<@NotNull Recipe> searchRecipes(
            @NotNull Set<@NotNull String> tokens) {
        throw new RuntimeException();
    }

    @Override
    public @NotNull Set<@NotNull User> searchUsers(@NotNull Set<@NotNull String> tokens) {
        throw new RuntimeException();
    }
}
