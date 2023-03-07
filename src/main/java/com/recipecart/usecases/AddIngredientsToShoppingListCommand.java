/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.User;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents an action item for the use case of a user adding ingredients to their
 * shopping list.
 */
public final class AddIngredientsToShoppingListCommand extends ShoppingListCommand {
    public static final String
            NOT_OK_INVALID_INGREDIENTS =
                    "Shopping list update unsuccessful: one of the given ingredient names is null"
                            + " or invalid",
            NOT_OK_INGREDIENT_NOT_FOUND =
                    "Shopping list update unsuccessful: one of the given ingredient names don't"
                            + " correspond to an existing ingredient";

    private final Map<String, Double> ingredientsToAdd;

    /**
     * Creates the action item for a user to add ingredients to their shopping list.
     *
     * @param shopperUsername the associated username of the user
     * @param ingredientsToAdd the associated names of ingredients and their respective amounts to
     *     add
     */
    public AddIngredientsToShoppingListCommand(
            String shopperUsername, Map<String, Double> ingredientsToAdd) {
        super(shopperUsername);
        this.ingredientsToAdd = Utils.allowNull(ingredientsToAdd, HashMap::new);
    }

    /**
     * @return an unmodifiable view of the ingredients (with amounts) to add to the shopping list.
     */
    public Map<String, Double> getIngredientsToAdd() {
        return Utils.allowNull(ingredientsToAdd, Collections::unmodifiableMap);
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isIngredientsMapValid()) {
            return NOT_OK_INVALID_INGREDIENTS;
        }
        try {
            if (!doIngredientsExist()) {
                return NOT_OK_INGREDIENT_NOT_FOUND;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isIngredientsMapValid() {
        return getIngredientsToAdd() != null
                && !getIngredientsToAdd().containsKey(null)
                && !getIngredientsToAdd().containsValue(null);
    }

    private boolean doIngredientsExist() {
        assert getStorageSource() != null;
        return getIngredientsToAdd().keySet().stream()
                .allMatch(getStorageSource().getLoader()::ingredientNameExists);
    }

    /**
     * Have the ingredients be added to the user's shopping list. If the given username is null or
     * has no associated user, or if any of the ingredient names are null don't correspond to
     * existing ingredients, or if any of the ingredient values are null, then this command's
     * execution will be unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        super.execute();
    }

    /**
     * Add the ingredients, that were given into this command, into the user's shopping list.
     *
     * @return the updated shopping list
     * @throws IOException (shouldn't happen, since entity names are checked before retrieval)
     */
    @Override
    protected Map<Ingredient, Double> performShoppingListUpdate() throws IOException {
        User shopper = getShopper();
        Map<Ingredient, Double> updatedShoppingList =
                Utils.addMaps(shopper.getShoppingList(), getIngredients());
        saveUpdatedShoppingList(shopper, updatedShoppingList);
        return updatedShoppingList;
    }

    private Map<Ingredient, Double> getIngredients() throws IOException {
        assert getStorageSource() != null;
        Map<Ingredient, Double> ingredients = new HashMap<>();
        for (Map.Entry<String, Double> entry : getIngredientsToAdd().entrySet()) {
            Ingredient ingredient =
                    getStorageSource()
                            .getLoader()
                            .getIngredientsByNames(Collections.singletonList(entry.getKey()))
                            .get(0);
            ingredients.put(ingredient, entry.getValue());
        }
        return ingredients;
    }
}
