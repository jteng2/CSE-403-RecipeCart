/* (C)2023 */
package usages;

import entities.Recipe;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class represents an action item for the use case for a user creating a new Recipe. */
public final class CreateRecipeCommand extends EntityCommand {
    private final @NotNull String creatorUsername;
    private final @NotNull Recipe recipe;

    /**
     * Creates an action item for a user to create a Recipe.
     *
     * @param creatorUsername the username of the user that creates the Recipe
     * @param recipe the Recipe that the user creates
     */
    public CreateRecipeCommand(@NotNull String creatorUsername, @NotNull Recipe recipe) {
        this.creatorUsername = creatorUsername;
        this.recipe = recipe;
    }

    /**
     * Has the given user create the given Recipe. If the Recipe's (non-presentation) name is null,
     * a name will be assigned to it. If it's not null and the name isn't taken, the Recipe will
     * take that name. If it's not null and the name is taken, the command will fail.
     *
     * @throws IllegalArgumentException if the given username has no associated user.
     * @return a successful Result with the Recipe that was added (with its assigned name) if the
     *     Recipe was successfully added, an unsuccessful Result with a null Recipe otherwise. The
     *     return will be a CreateRecipeCommand.CreateRecipeResult.
     */
    @Override
    @NotNull Result execute() {
        throw new NotImplementedException();
    }

    @NotNull public String getCreatorUsername() {
        return creatorUsername;
    }

    @NotNull public Recipe getRecipe() {
        return recipe;
    }

    /** This class represents the results of executing a CreateRecipeCommand. */
    public static class CreateRecipeResult extends Result {
        private final @Nullable Recipe createdRecipe;

        CreateRecipeResult(
                boolean success, @NotNull String message, @Nullable Recipe createdRecipe) {
            super(success, message);
            this.createdRecipe = createdRecipe;
        }

        /** @return the Recipes that was created and saved when executing CreateRecipeCommand. */
        @Nullable public Recipe getCreatedRecipe() {
            return createdRecipe;
        }
    }
}
