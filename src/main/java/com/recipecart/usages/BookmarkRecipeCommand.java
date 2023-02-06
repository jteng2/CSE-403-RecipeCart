/* (C)2023 */
package com.recipecart.usages;

import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/** This class represents an action item for the use case of a user bookmarking a recipe. */
public final class BookmarkRecipeCommand extends EntityCommand {
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

    /**
     * Has the user bookmark the recipe. If the user already bookmarked the recipe, this command
     * will fail.
     *
     * @throws IllegalArgumentException if the given username has no associated user, or the given
     *     recipe name has no associated recipe.
     * @return a successful Result if the recipe was successfully saved, an unsuccessful one
     *     otherwise. The return will be a EntityCommand.Result.
     */
    @Override
    @NotNull Result execute() {
        throw new NotImplementedException();
    }

    @NotNull public String getBookmarkerUsername() {
        return bookmarkerUsername;
    }

    @NotNull public String getRecipeName() {
        return recipeName;
    }
}
