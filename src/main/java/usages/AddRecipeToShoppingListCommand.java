/* (C)2023 */
package usages;

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

    /**
     * Have the recipe's ingredients be added to the user's shopping list.
     *
     * @throws IllegalArgumentException if the given username has no associated user, or if the
     *     given recipe name has no associated recipe
     * @return a successful result if the ingredients were successfully added, that contains an
     *     updated shopping list of the user; an unsuccessful one otherwise. The return will be a
     *     ShoppingListCommand.ShoppingListResult.
     */
    @Override
    @NotNull Result execute() {
        throw new NotImplementedException();
    }

    @NotNull public String getRecipeName() {
        return recipeName;
    }

    public boolean isAddOnlyMissingIngredients() {
        return addOnlyMissingIngredients;
    }
}
