/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Collections;

/**
 * This class represents the action item for the use case of retrieving a ingredient from a given
 * EntityStorage.
 */
public final class GetIngredientCommand extends SimpleGetCommand<Ingredient> {
    public static final String OK_INGREDIENT_RETRIEVED = "Ingredient retrieval successful",
            NOT_OK_INGREDIENT_NOT_FOUND =
                    "Ingredient retrieval unsuccessful: a ingredient with the given name could not"
                            + " be found";

    /**
     * Creates the action item for retrieving an ingredient with the given name.
     *
     * @param name the name of the ingredient to retrieve.
     */
    public GetIngredientCommand(String name) {
        super(name);
    }

    /** {@inheritDoc} */
    @Override
    protected String getOkEntityRetrievedMessage() {
        return OK_INGREDIENT_RETRIEVED;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkNotFoundMessage() {
        return NOT_OK_INGREDIENT_NOT_FOUND;
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityClassName() {
        return Ingredient.class.getName();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.ingredientNameExists(entityName);
    }

    /** {@inheritDoc} */
    @Override
    protected Ingredient retrieveEntity(EntityLoader loader, String entityName) throws IOException {
        return loader.getIngredientsByNames(Collections.singletonList(entityName)).get(0);
    }
}
