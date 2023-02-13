/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.entities.*;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import spark.Response;

/**
 * This helper class is for modifying Response objects based on the various outputs (of various
 * types) of EntityCommands.
 */
final class ResponseTranslator {
    // Each of these helper methods takes an existing Response and another object and modifies
    // the Response according to the object. The given object is assumed to be from the output
    // of an EntityCommand.

    static void messageToResponse(@NotNull Response response, @NotNull String message) {
        throw new NotImplementedException();
    }

    static Response shoppingListToResponse(
            @NotNull Response response,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> shoppingList) {
        throw new NotImplementedException();
    }

    static Response createdRecipeToResponse(
            @NotNull Response response, @NotNull Recipe createdRecipe) {
        throw new NotImplementedException();
    }

    static Response searchedRecipesToResponse(
            @NotNull Response response, @NotNull List<@NotNull Recipe> foundRecipes) {
        throw new NotImplementedException();
    }
}
