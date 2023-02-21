/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class represents an action item for the use case for a user creating a new Recipe. */
public final class CreateRecipeCommand extends EntityCommand {
    public static final String
            OK_RECIPE_CREATED_WITH_GIVEN_NAME =
                    "Recipe creation successful: the given unique (non-presentation) recipe name"
                            + " was available.",
            OK_RECIPE_CREATED_NAME_ASSIGNED =
                    "Recipe creation successful: the recipe was assigned a new unique"
                            + " (non-presentation)  name",
            NOT_OK_INVALID_RECIPE = "Recipe creation unsuccessful: the recipe was invalid or null",
            NOT_OK_NAME_TAKEN =
                    "Recipe creation unsuccessful: the given unique (non-presentation) recipe name"
                            + " is already taken.";

    private final Recipe recipeToAdd;

    /**
     * Creates an action item for a user to create a Recipe. The given Recipe must have a non-null
     * presentation name and a non-null author's username for the command to execute successfully.
     *
     * @param recipeToAdd the Recipe that the user creates
     */
    public CreateRecipeCommand(@NotNull Recipe recipeToAdd) {
        this.recipeToAdd = recipeToAdd;
    }

    public Recipe getRecipeToAdd() {
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
     * unsuccessful. Also, if the Recipe's presentation name or author's username is null, then this
     * command's execution will be unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
