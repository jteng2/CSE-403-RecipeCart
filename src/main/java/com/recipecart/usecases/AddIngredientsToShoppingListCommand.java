/* (C)2023 */
package com.recipecart.usecases;

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

    @NotNull public Map<@NotNull String, @NotNull Double> getIngredientsToAdd() {
        return Collections.unmodifiableMap(ingredientsToAdd);
    }

    /**
     * Have the ingredients be added to the user's shopping list. If the given username is null or
     * has no associated user, or if any of the ingredient names are null don't correspond to
     * existing ingredients, or if any of the ingredient values are null, then this command's
     * execution will be unsuccessful
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
