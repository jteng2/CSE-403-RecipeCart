/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.usecases.*;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import spark.Request;

/**
 * This helper class is for translating JSON from requests from the front-end into various
 * EntityCommands.
 */
final class RequestTranslator {

    /**
     * @return the type of use case this request contains, or null if no use cases this class knows
     *     are detected.
     */
    @Nullable static RequestTranslator.UseCase detectUseCase(Request request) {
        throw new NotImplementedException();
    }

    // Each of these helper methods translates a body of an HTTP request (assumed to be in JSON
    // format) into the respective command. The request bodies are assumed to represent
    // the correct command.

    @NotNull static AddIngredientsToShoppingListCommand requestBodyToAddIngredientsToShoppingListCommand(
            @NotNull String requestBody) {
        throw new NotImplementedException();
    }

    @NotNull static AddRecipeToShoppingListCommand requestBodyToAddRecipeToShoppingListCommand(
            @NotNull String requestBody) {
        throw new NotImplementedException();
    }

    @NotNull static BookmarkRecipeCommand requestBodyToBookmarkRecipeCommand(@NotNull String requestBody) {
        throw new NotImplementedException();
    }

    @NotNull static CreateRecipeCommand requestBodyToCreateRecipeCommand(@NotNull String requestBody) {
        throw new NotImplementedException();
    }

    @NotNull static SearchRecipesCommand requestBodyToSearchRecipeCommand(@NotNull String requestBody) {
        throw new NotImplementedException();
    }

    /** These represent the various use cases in the Business Logic Layer. */
    @Nullable enum UseCase {
        ADD_INGREDIENTS_TO_SHOPPING_LIST,
        ADD_RECIPE_TO_SHOPPING_LIST,
        BOOKMARK_RECIPE,
        CREATE_RECIPE,
        SEARCH_RECIPES
    }
}
