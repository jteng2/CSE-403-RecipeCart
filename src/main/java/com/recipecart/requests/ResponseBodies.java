/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.UserForm;
import com.recipecart.utils.Utils;
import java.util.*;
import org.jetbrains.annotations.NotNull;

/**
 * This helper class contains static Java class representations of the (JSON) bodies of various HTTP
 * responses. The classes in this class follow the response bodies described in api_routes.md in the
 * top-level directory of this repository. In particular, the private field names are sensitive to
 * change since those must be the same as the keys of the JSON objects they follow.
 */
class ResponseBodies {
    /**
     * This is the base response, which its subclasses build upon. However, this class itself
     * follows these API routes:
     *
     * <ul>
     *   <li>"Create user"
     *   <li>"Create ingredient"
     *   <li>"Create tag"
     *   <li>"Bookmark recipe"
     *   <li>"Add ingredients to shopping list"
     *   <li>"Add recipe ingredients to shopping list"
     * </ul>
     */
    static class WithMessage {
        private final @NotNull String message;

        WithMessage(@NotNull String message) {
            this.message = message;
        }

        @NotNull String getMessage() {
            return message;
        }
    }

    static class SearchResponse<T> extends WithMessage {
        private final List<T> matches;

        SearchResponse(String message, Collection<T> matches) {
            super(message);
            this.matches = Utils.allowNull(matches, ArrayList::new);
        }

        List<T> getMatches() {
            return Utils.allowNull(matches, Collections::unmodifiableList);
        }
    }

    /** Follows the "Search for recipe" API route. */
    static class RecipeSearch extends SearchResponse<RecipeForm> {
        RecipeSearch(String message, Collection<Recipe> matches) {
            super(message, Utils.allowNull(matches, Utils::fromRecipes));
        }
    }

    /** Follows the "Search for ingredient" API route. */
    static class IngredientSearch extends SearchResponse<Ingredient> {
        IngredientSearch(String message, Collection<Ingredient> matches) {
            super(message, matches);
        }
    }

    /** Follows the "Search for user" API route. */
    static class UserSearch extends SearchResponse<UserForm> {
        UserSearch(String message, Collection<User> matches) {
            super(message, Utils.allowNull(matches, Utils::fromUsers));
        }
    }

    /** Follows the "Search for tag" API route. */
    static class TagSearch extends SearchResponse<Tag> {
        TagSearch(String message, Collection<Tag> matches) {
            super(message, matches);
        }
    }

    /** Follows the "Create recipe" API route. */
    static class RecipeCreation extends WithMessage {
        private final String assignedName;
        private final Set<String> createdTags;

        RecipeCreation(String message, String assignedName, Set<String> createdTags) {
            super(message);
            this.assignedName = assignedName;
            this.createdTags = createdTags;
        }

        String getAssignedName() {
            return assignedName;
        }

        Set<String> getCreatedTags() {
            return createdTags;
        }
    }

    /** Follows the "Get tag" API route. */
    static class TagRetrieval extends WithMessage {
        private final Tag tag;

        TagRetrieval(@NotNull String message, Tag retrievedTag) {
            super(message);
            this.tag = retrievedTag;
        }

        Tag getTag() {
            return tag;
        }
    }

    /** Follows the "Get ingredient" API route. */
    static class IngredientRetrieval extends WithMessage {
        private final Ingredient ingredient;

        IngredientRetrieval(@NotNull String message, Ingredient retrievedIngredient) {
            super(message);
            this.ingredient = retrievedIngredient;
        }

        Ingredient getIngredient() {
            return ingredient;
        }
    }

    /** Follows the "Get recipe" API route. */
    static class RecipeRetrieval extends WithMessage {
        private final RecipeForm recipe;

        RecipeRetrieval(@NotNull String message, RecipeForm retrievedRecipeForm) {
            super(message);
            this.recipe = retrievedRecipeForm;
        }

        RecipeRetrieval(@NotNull String message, Recipe retrievedRecipe) {
            this(message, (RecipeForm) Utils.allowNull(retrievedRecipe, RecipeForm::new));
        }

        RecipeForm getRecipe() {
            return recipe;
        }
    }

    /** Follows the "Get user" API route. */
    static class UserRetrieval extends WithMessage {
        private final UserForm user;

        UserRetrieval(@NotNull String message, UserForm retrievedUserForm) {
            super(message);
            this.user = retrievedUserForm;
        }

        UserRetrieval(@NotNull String message, User retrievedUser) {
            this(message, (UserForm) Utils.allowNull(retrievedUser, UserForm::new));
        }

        UserForm getUser() {
            return user;
        }
    }
}
