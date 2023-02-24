/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Collections;

/**
 * This class represents the action item for the use case of retrieving a recipe from a given
 * EntityStorage.
 */
public final class GetRecipeCommand extends SimpleGetCommand<Recipe> {
    public static final String OK_RECIPE_RETRIEVED = "Recipe retrieval successful",
            NOT_OK_RECIPE_NOT_FOUND =
                    "Recipe retrieval unsuccessful: a recipe with the given name could not be"
                            + " found";

    /**
     * Creates the action item for retrieving a recipe with the given name.
     *
     * @param name the name of the recipe to retrieve.
     */
    public GetRecipeCommand(String name) {
        super(name);
    }

    @Override
    protected String getOkEntityRetrievedMessage() {
        return OK_RECIPE_RETRIEVED;
    }

    @Override
    protected String getNotOkNotFoundMessage() {
        return NOT_OK_RECIPE_NOT_FOUND;
    }

    @Override
    protected String getEntityClassName() {
        return Recipe.class.getName();
    }

    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.recipeNameExists(entityName);
    }

    @Override
    protected Recipe retrieveEntity(EntityLoader loader, String entityName) throws IOException {
        return loader.getRecipesByNames(Collections.singletonList(entityName)).get(0);
    }
}
