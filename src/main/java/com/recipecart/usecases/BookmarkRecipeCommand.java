/* (C)2023 */
package com.recipecart.usecases;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/** This class represents an action item for the use case of a user bookmarking a recipe. */
public final class BookmarkRecipeCommand extends EntityCommand {
    public static final String OK_RECIPE_BOOKMARKED = "Recipe bookmarking successful.",
            NOT_OK_INVALID_RECIPE_NAME =
                    "Recipe bookmarking unsuccessful: the given recipe name was null or invalid.",
            NOT_OK_INVALID_USERNAME =
                    "Recipe bookmarking unsuccessful: the username of the bookmark-er was null or"
                            + " invalid.";

    private final @NotNull String bookmarkerUsername;
    private final @NotNull String recipeName;

    /**
     * Creates an action item for a username to bookmark a recipe.
     *
     * @param bookmarkerUsername the associated username of the given user
     * @param recipeName the associated (non-presentation) name of the given recipe
     */
    public BookmarkRecipeCommand(@NotNull String bookmarkerUsername, @NotNull String recipeName) {
        this.bookmarkerUsername = bookmarkerUsername;
        this.recipeName = recipeName;
    }

    @NotNull public String getBookmarkerUsername() {
        return bookmarkerUsername;
    }

    @NotNull public String getRecipeName() {
        return recipeName;
    }

    /**
     * Has the user bookmark the recipe. The command will be unsuccessful if the given username has
     * no associated user, the given recipe name has no associated recipe, or if the user already
     * bookmarked the recipe.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
