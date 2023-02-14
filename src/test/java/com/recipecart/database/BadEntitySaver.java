/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;
import org.jetbrains.annotations.NotNull;

/**
 * An EntitySaver that simulates something bad happening in the data access layer, that prevents the
 * method from successfully executing (ex. the database failing).
 */
public class BadEntitySaver implements EntitySaver {
    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        throw new RuntimeException();
    }

    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        throw new RuntimeException();
    }

    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        throw new RuntimeException();
    }

    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        throw new RuntimeException();
    }
}
