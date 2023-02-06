/* (C)2023 */
package com.recipecart.usages;

import com.recipecart.entities.Ingredient;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

/**
 * This abstract class represents an action item that represents a use case involving a user's
 * shopping list.
 */
public abstract class ShoppingListCommand extends EntityCommand {
    private final @NotNull String shopperUsername;

    ShoppingListCommand(@NotNull String shopperUsername) {
        this.shopperUsername = shopperUsername;
    }

    /** @return the username of the user whose shopping list is involved with this command. */
    @NotNull public String getShopperUsername() {
        return shopperUsername;
    }

    /** This class represents the results of executing a ShoppingListCommand. */
    public static class ShoppingListResult extends Result {
        private final @NotNull Map<Ingredient, Double> resultShoppingList;

        ShoppingListResult(
                boolean success,
                @NotNull String message,
                @NotNull Map<Ingredient, Double> resultShoppingList) {
            super(success, message);
            this.resultShoppingList = resultShoppingList;
        }

        @NotNull public Map<Ingredient, Double> getResultShoppingList() {
            return Collections.unmodifiableMap(resultShoppingList);
        }
    }
}
