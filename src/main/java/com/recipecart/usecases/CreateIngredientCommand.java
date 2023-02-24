/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;

/** This class represents an action item for the use case of a new ingredient being created. */
public class CreateIngredientCommand extends SimpleCreateEntityCommand<Ingredient> {
    public static final String OK_INGREDIENT_CREATED = "Ingredient creation successful",
            NOT_OK_INVALID_INGREDIENT =
                    "Ingredient creation unsuccessful: the ingredient was invalid or null",
            NOT_OK_INGREDIENT_NAME_TAKEN =
                    "Ingredient creation unsuccessful: the ingredient name is already taken";

    /**
     * Creates the action item for creating an Ingredient and saving it into a given EntityStorage.
     *
     * @param toAdd the Ingredient to add
     */
    public CreateIngredientCommand(Ingredient toAdd) {
        super(toAdd);
    }

    /**
     * Creates the action item for creating an Ingredient and saving it into a given EntityStorage.
     *
     * @param ingredientName the name of the ingredient to add
     * @param units the units this ingredient uses
     * @param imageUri URI of an image of this ingredient
     */
    public CreateIngredientCommand(String ingredientName, String units, String imageUri) {
        this(new Ingredient(ingredientName, units, imageUri));
    }

    @Override
    protected String getOkEntityCreatedMessage() {
        return OK_INGREDIENT_CREATED;
    }

    @Override
    protected String getNotOkInvalidEntityMessage() {
        return NOT_OK_INVALID_INGREDIENT;
    }

    @Override
    protected String getNotOkEntityNameAlreadyTakenMessage() {
        return NOT_OK_INGREDIENT_NAME_TAKEN;
    }

    @Override
    protected String getEntityName(Ingredient entity) {
        return entity.getName();
    }

    @Override
    protected String getEntityClassName() {
        return Ingredient.class.toString();
    }

    @Override
    protected void updateEntities(EntitySaver saver, Collection<Ingredient> entities) {
        saver.updateIngredients(entities);
    }

    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.ingredientNameExists(entityName);
    }
}
