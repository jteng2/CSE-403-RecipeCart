/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import com.recipecart.entities.User;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.Nullable;

/** This class represents an action item for the use case of a user bookmarking a recipe. */
public final class BookmarkRecipeCommand extends EntityCommand {
    public static final String OK_RECIPE_BOOKMARKED = "Recipe bookmarking successful",
            NOT_OK_INVALID_RECIPE_NAME =
                    "Recipe bookmarking unsuccessful: the given recipe name was null or invalid",
            NOT_OK_INVALID_USERNAME =
                    "Recipe bookmarking unsuccessful: the username of the bookmark-er was null or"
                            + " invalid",
            NOT_OK_RECIPE_NOT_FOUND =
                    "Recipe bookmarking unsuccessful: the given recipe name doesn't correspond to"
                            + " an existing recipe",
            NOT_OK_USER_NOT_FOUND =
                    "Recipe bookmarking unsuccessful: the given username doesn't correspond to an"
                            + " existing user",
            NOT_OK_RECIPE_ALREADY_BOOKMARKED =
                    "Recipe bookmarking unsuccessful: the given user already has the given recipe"
                            + " bookmarked";

    private final String bookmarkerUsername;
    private final String recipeName;

    /**
     * Creates an action item for a username to bookmark a recipe.
     *
     * @param bookmarkerUsername the associated username of the given user
     * @param recipeName the associated (non-presentation) name of the given recipe
     */
    public BookmarkRecipeCommand(String bookmarkerUsername, String recipeName) {
        this.bookmarkerUsername = bookmarkerUsername;
        this.recipeName = recipeName;
    }

    /**
     * @return the username of the user that will bookmark the recipe.
     */
    @Nullable public String getBookmarkerUsername() {
        return bookmarkerUsername;
    }

    /**
     * @return the (non-presentation) name of the recipe that will be bookmarked.
     */
    @Nullable public String getRecipeName() {
        return recipeName;
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isUsernameValid()) {
            return NOT_OK_INVALID_USERNAME;
        }
        if (!isRecipeNameValid()) {
            return NOT_OK_INVALID_RECIPE_NAME;
        }
        try {
            if (!doesUserExist()) {
                return NOT_OK_USER_NOT_FOUND;
            }
            if (!doesRecipeExist()) {
                return NOT_OK_RECIPE_NOT_FOUND;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isUsernameValid() {
        return getBookmarkerUsername() != null;
    }

    private boolean isRecipeNameValid() {
        return getRecipeName() != null;
    }

    private boolean doesUserExist() {
        assert getStorageSource() != null;
        return getStorageSource().getLoader().usernameExists(getBookmarkerUsername());
    }

    private boolean doesRecipeExist() {
        assert getStorageSource() != null;
        return getStorageSource().getLoader().recipeNameExists(getRecipeName());
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
        checkExecutionAlreadyDone();
        if (finishInvalidCommand()) {
            return;
        }

        try {
            if (!performBookmarking()) {
                return;
            }
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        } catch (IOException e) {
            finishExecutingImpossibleOutcome(e);
            return;
        }

        finishExecutingRecipeSuccessfullyBookmarked();
    }

    private boolean performBookmarking() throws IOException {
        User bookmarker = getUser();
        Recipe toBookmark = getRecipe();

        if (isRecipeAlreadyBookmarked(bookmarker, toBookmark)) {
            finishExecutingRecipeAlreadyBookmarked();
            return false;
        }

        User updatedBookmarker = getUserWithRecipeBookmarked(bookmarker, toBookmark);
        saveUser(updatedBookmarker);
        return true;
    }

    private User getUser() throws IOException {
        assert getStorageSource() != null;
        return getStorageSource()
                .getLoader()
                .getUsersByNames(Collections.singletonList(getBookmarkerUsername()))
                .get(0);
    }

    private Recipe getRecipe() throws IOException {
        assert getStorageSource() != null;
        return getStorageSource()
                .getLoader()
                .getRecipesByNames(Collections.singletonList(getRecipeName()))
                .get(0);
    }

    private boolean isRecipeAlreadyBookmarked(User user, Recipe recipe) {
        return user.getSavedRecipes().contains(recipe);
    }

    private User getUserWithRecipeBookmarked(User user, Recipe recipe) {
        List<Recipe> savedRecipes = new ArrayList<>(user.getSavedRecipes());
        savedRecipes.add(recipe);
        return new User.Builder(user).setSavedRecipes(savedRecipes).build();
    }

    private void saveUser(User user) {
        assert getStorageSource() != null;
        getStorageSource().getSaver().updateUsers(Collections.singleton(user));
    }

    private void finishExecutingRecipeAlreadyBookmarked() {
        setExecutionMessage(NOT_OK_RECIPE_ALREADY_BOOKMARKED);
        finishExecuting();
    }

    private void finishExecutingRecipeSuccessfullyBookmarked() {
        setExecutionMessage(OK_RECIPE_BOOKMARKED);
        beSuccessful();
        finishExecuting();
    }
}
