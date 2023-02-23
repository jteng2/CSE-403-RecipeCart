/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.Utils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;

/**
 * This helper class contains static Java class representations of the (JSON) bodies of various HTTP
 * responses. The classes in this class follow the response bodies described in api_routes.md in the
 * top-level directory of this repository. In particular, the private field names are sensitive to
 * change since those must be the same as the keys of the JSON objects they follow.
 */
class ResponseBodies {
    static class WithMessage {
        private final @NotNull String message;

        WithMessage(@NotNull String message) {
            this.message = message;
        }

        @NotNull String getMessage() {
            return message;
        }
    }

    /** Follows the "Search for recipe" API route. */
    static class RecipeSearch extends WithMessage {
        private final List<RecipeForm> matches;

        RecipeSearch(String message, Collection<RecipeForm> matches) {
            super(message);
            this.matches = Utils.allowNull(matches, ArrayList::new);
        }

        List<RecipeForm> getMatches() {
            return Collections.unmodifiableList(matches);
        }
    }

    /** Follows the "Create recipe" API route. */
    static class RecipeCreation extends WithMessage {
        private final String assignedName;

        RecipeCreation(String message, String assignedName) {
            super(message);
            this.assignedName = assignedName;
        }

        String getAssignedName() {
            return assignedName;
        }
    }
}
