/* (C)2023 */
package com.recipecart.usages;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action item for the use case of a user adding ingredients to their
 * shopping list.
 */
public final class AddIngredientsToShoppingListCommand extends ShoppingListCommand {
    private final @NotNull Map<@NotNull String, @NotNull Double> ingredientsToAdd;

    /**
     * Creates the action item for a user to add ingredients to their shopping list.
     *
     * @param shopperUsername the associated username of the user
     * @param ingredientsToAdd the associated names of ingredients and their respective amounts to
     *     add
     */
    public AddIngredientsToShoppingListCommand(
            @NotNull String shopperUsername,
            @NotNull Map<@NotNull String, @NotNull Double> ingredientsToAdd) {
        super(shopperUsername);
        this.ingredientsToAdd = new HashMap<>(ingredientsToAdd);
    }

    /**
     * Have the ingredients be added to the user's shopping list.
     *
     * @throws IllegalArgumentException if the given username has no associated user, or if any of
     *     the ingredient names don't correspond to existing ingredients
     * @return a successful result if the ingredients were successfully added, that contains an
     *     updated shopping list of the user; an unsuccessful one otherwise. The return will be a
     *     ShoppingListCommand.ShoppingListResult.
     */
    @Override
    @NotNull Result execute() {
        throw new NotImplementedException();
    }

    @NotNull public Map<@NotNull String, @NotNull Double> getIngredientsToAdd() {
        return Collections.unmodifiableMap(ingredientsToAdd);
    }
}
