/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.utils.RecipeForm;

/**
 * This helper class contains static Java class representations of the (JSON) bodies of various HTTP
 * requests. The classes in this class follow the request bodies described in api_routes.md in the
 * top-level directory of this repository. In particular, the private field names are sensitive to
 * change since those must be the same as the keys of the JSON objects they follow.
 */
class RequestBodies {
    static class WithLoginRequired {
        private final String encryptedJwtToken;

        WithLoginRequired(String encryptedJwtToken) {
            this.encryptedJwtToken = encryptedJwtToken;
        }

        String getEncryptedJwtToken() {
            return encryptedJwtToken;
        }
    }

    /** Follows the "Create recipe" API route. */
    static class RecipeCreation extends WithLoginRequired {
        private final RecipeForm recipe;

        RecipeCreation(String encryptedJwtToken, RecipeForm recipe) {
            super(encryptedJwtToken);
            this.recipe = recipe;
        }

        RecipeForm getRecipeForm() {
            return recipe;
        }
    }

    /** Follows the "Create user" API route. */
    static class UserCreation {
        private final String username, emailAddress;

        UserCreation(String username, String emailAddress) {
            this.username = username;
            this.emailAddress = emailAddress;
        }

        String getUsername() {
            return username;
        }

        String getEmailAddress() {
            return emailAddress;
        }
    }

    /** Follows the "Create ingredient" API route. */
    static class IngredientCreation {
        private final String name, units, imageUri;

        IngredientCreation(String name, String units, String imageUri) {
            this.name = name;
            this.units = units;
            this.imageUri = imageUri;
        }

        String getName() {
            return name;
        }

        String getUnits() {
            return units;
        }

        String getImageUri() {
            return imageUri;
        }
    }

    /** Follows the "Create tag" API route. */
    static class TagCreation {
        private final String name;

        TagCreation(String name) {
            this.name = name;
        }

        String getName() {
            return name;
        }
    }
}
