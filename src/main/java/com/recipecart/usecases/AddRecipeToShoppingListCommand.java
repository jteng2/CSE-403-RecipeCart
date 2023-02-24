/* (C)2023 */
package com.recipecart.usecases;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents an action item for the use case of a user adding a specific recipe's
 * ingredients to their shopping list.
 */
public final class AddRecipeToShoppingListCommand extends ShoppingListCommand {
    private final @NotNull String recipeName;
    private final boolean addOnlyMissingIngredients;

    /**
     * Creates the action item for a user to add ingredients to their shopping list.
     *
     * @param shopperUsername the associated username of the user
     * @param recipeName the associated (non-presentation) name of the recipe whose ingredients to
     *     add
     * @param addOnlyMissingIngredients if true, only ingredients the user doesn't have will be
     *     added; otherwise, add all the recipe's ingredients.
     */
    public AddRecipeToShoppingListCommand(
            @NotNull String shopperUsername,
            @NotNull String recipeName,
            boolean addOnlyMissingIngredients) {
        super(shopperUsername);
        this.recipeName = recipeName;
        this.addOnlyMissingIngredients = addOnlyMissingIngredients;
    }

    @NotNull public String getRecipeName() {
        return recipeName;
    }

    public boolean isAddOnlyMissingIngredients() {
        return addOnlyMissingIngredients;
    }

    /**
     * Have the recipe's ingredients be added to the user's shopping list. If the given username is
     * null or has no associated user, or if the given recipe name is null or has no associated
     * recipe, then this command's execution will be unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
