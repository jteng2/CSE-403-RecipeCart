/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.User;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an action item for the use case of a user adding a specific recipe's
 * ingredients to their shopping list.
 */
public final class AddRecipeToShoppingListCommand extends ShoppingListCommand {
    public static final String
            NOT_OK_INVALID_RECIPE =
                    "Shopping list update unsuccessful: the given recipe name is null or invalid",
            NOT_OK_RECIPE_NOT_FOUND =
                    "Shopping list update unsuccessful: the given recipe name doesn't correspond to"
                            + " an existing recipe";

    private final String recipeName;
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
            String shopperUsername, String recipeName, boolean addOnlyMissingIngredients) {
        super(shopperUsername);
        this.recipeName = recipeName;
        this.addOnlyMissingIngredients = addOnlyMissingIngredients;
    }

    /**
     * @return the name of the recipe whose ingredients are to be added to the shopping list.
     */
    public String getRecipeName() {
        return recipeName;
    }

    /**
     * @return true if the command will only add recipe ingredients, that the user doesn't currently
     *     have, to their shopping list; false otherwise.
     */
    public boolean isAddOnlyMissingIngredients() {
        return addOnlyMissingIngredients;
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isRecipeValid()) {
            return NOT_OK_INVALID_RECIPE;
        }
        try {
            if (!doesRecipeExist()) {
                return NOT_OK_RECIPE_NOT_FOUND;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isRecipeValid() {
        return getRecipeName() != null;
    }

    private boolean doesRecipeExist() {
        assert getStorageSource() != null;
        return getStorageSource().getLoader().recipeNameExists(getRecipeName());
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
        super.execute();
    }

    /**
     * Add the ingredients of the recipe, that was given into this command, into the user's shopping
     * list.
     *
     * @return the updated shopping list
     * @throws IOException (shouldn't happen, since entity names are checked before retrieval)
     */
    @Override
    protected Map<Ingredient, Double> performShoppingListUpdate() throws IOException {
        User shopper = getShopper();
        Map<Ingredient, Double> updatedShoppingList = getUpdatedShoppingList(shopper);
        saveUpdatedShoppingList(shopper, updatedShoppingList);
        return updatedShoppingList;
    }

    private Map<Ingredient, Double> getUpdatedShoppingList(User shopper) throws IOException {
        Map<Ingredient, Double> shoppingListUpdate = getRecipe().getRequiredIngredients();
        if (isAddOnlyMissingIngredients()) {
            shoppingListUpdate = new HashMap<>(shoppingListUpdate);
            shoppingListUpdate.keySet().removeAll(shopper.getOwnedIngredients());
        }
        return Utils.addMaps(shopper.getShoppingList(), shoppingListUpdate);
    }

    private Recipe getRecipe() throws IOException {
        assert getStorageSource() != null;
        return getStorageSource()
                .getLoader()
                .getRecipesByNames(Collections.singletonList(getRecipeName()))
                .get(0);
    }
}
