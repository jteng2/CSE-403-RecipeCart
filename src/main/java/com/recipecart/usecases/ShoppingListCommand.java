/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This abstract class represents an action item that represents a use case involving a user's
 * shopping list.
 */
public abstract class ShoppingListCommand extends EntityCommand {
    private final @NotNull String shopperUsername;

    ShoppingListCommand(@NotNull String shopperUsername) {
        this.shopperUsername = shopperUsername;
    }

    /**
     * @return the username of the user whose shopping list is involved with this command.
     */
    @NotNull public String getShopperUsername() {
        return shopperUsername;
    }

    /**
     * Returns the user's updated shopping list as a result of this command's execution.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) updated shopping list, if the command successfully updated it; null
     *     otherwise.
     */
    @Nullable public Map<Ingredient, Double> getResultShoppingList() {
        throw new NotImplementedException();
    }
}
