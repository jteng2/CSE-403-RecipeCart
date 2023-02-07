/* (C)2023 */
package com.recipecart.usages;

import com.recipecart.entities.Recipe;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class represents an action item for the use case for a user creating a new Recipe. */
public final class CreateRecipeCommand extends EntityCommand {
    private final @NotNull String creatorUsername;
    private final @NotNull Recipe recipeToAdd;

    /**
     * Creates an action item for a user to create a Recipe.
     *
     * @param creatorUsername the username of the user that creates the Recipe
     * @param recipeToAdd the Recipe that the user creates
     */
    public CreateRecipeCommand(@NotNull String creatorUsername, @NotNull Recipe recipeToAdd) {
        this.creatorUsername = creatorUsername;
        this.recipeToAdd = recipeToAdd;
    }

    @NotNull public String getCreatorUsername() {
        return creatorUsername;
    }

    @NotNull public Recipe getRecipeToAdd() {
        return recipeToAdd;
    }

    /**
     * Returns the (output) Recipe that was created and saved when executing this command. The
     * returned Recipe may not be the same as the one given (in particular, the name may be
     * different if the given one's name was set to null).
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) Recipe that was created/saved, if the command successfully added it;
     *     null otherwise.
     */
    @Nullable public Recipe getCreatedRecipe() {
        throw new NotImplementedException();
    }

    /**
     * Has the given user create the given Recipe. If the Recipe's (non-presentation) name is null,
     * a name will be assigned to it. If it's not null and the name isn't taken, the Recipe will
     * take that name. If it's not null and the name is taken, this command's execution will be
     * unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     * @throws IllegalArgumentException if the given username has no associated user.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
