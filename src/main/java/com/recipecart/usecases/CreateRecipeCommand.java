/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.*;
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
                            + " (non-presentation) name",
            NOT_OK_INVALID_RECIPE = "Recipe creation unsuccessful: the recipe was invalid or null",
            NOT_OK_NAME_TAKEN =
                    "Recipe creation unsuccessful: the given unique (non-presentation) recipe name"
                            + " is already taken.";

    private final RecipeForm recipeFormToAdd;
    private Recipe createdRecipe = null;

    /**
     * Creates an action item for a user to create a Recipe. The given Recipe must have a non-null
     * presentation name and a non-null author's username for the command to execute successfully.
     *
     * @param recipeToAdd the Recipe that the user creates
     */
    public CreateRecipeCommand(@NotNull RecipeForm recipeToAdd) {
        this.recipeFormToAdd = recipeToAdd;
    }

    public RecipeForm getToAdd() {
        return recipeFormToAdd;
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
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return createdRecipe;
    }

    private void setCreatedRecipe(@NotNull Recipe createdRecipe) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the created recipe after command has executed");
        }
        if (this.createdRecipe != null) {
            throw new IllegalStateException("Can only set created recipe once");
        }
        Objects.requireNonNull(createdRecipe);
        this.createdRecipe = createdRecipe;
    }

    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        try {
            if (!isRecipeToAddValid()) {
                return NOT_OK_INVALID_RECIPE;
            }
            if (!isRecipeNameAvailable()) {
                return NOT_OK_NAME_TAKEN;
            }
        } catch (RuntimeException e) { // for data access layer failures
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isRecipeToAddValid() {
        return getToAdd() != null
                && getToAdd().getPresentationName() != null
                && areDirectionsValid()
                && areTagNamesValid()
                && areIngredientNamesValid()
                && isAuthorUsernameValid();
    }

    private boolean areDirectionsValid() {
        if (getToAdd().getDirections() == null) {
            return true;
        }
        for (String direction : getToAdd().getDirections()) {
            if (direction == null) {
                return false;
            }
        }
        return true;
    }

    private boolean areTagNamesValid() {
        if (getToAdd().getTags() == null) {
            return true;
        }
        assert getStorageSource() != null;
        for (String tagName : getToAdd().getTags()) {
            if (tagName == null || !getStorageSource().getLoader().tagNameExists(tagName)) {
                return false;
            }
        }
        return true;
    }

    private boolean areIngredientNamesValid() {
        if (getToAdd().getRequiredIngredients() == null) {
            return true;
        }
        assert getStorageSource() != null;
        for (Map.Entry<String, Double> entry : getToAdd().getRequiredIngredients().entrySet()) {
            if (entry.getKey() == null
                    || entry.getValue() == null
                    || !getStorageSource().getLoader().ingredientNameExists(entry.getKey())) {
                return false;
            }
        }
        return true;
    }

    private boolean isAuthorUsernameValid() {
        assert getStorageSource() != null;
        return getToAdd().getAuthorUsername() != null
                && getStorageSource().getLoader().usernameExists(getToAdd().getAuthorUsername());
    }

    private boolean isRecipeNameAvailable() {
        assert getStorageSource() != null;
        String recipeName = getToAdd().getName();
        // null recipe name means we can give the recipe any (unique) non-presentation name
        return recipeName == null || !getStorageSource().getLoader().recipeNameExists(recipeName);
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
        checkExecutionAlreadyDone();
        if (finishInvalidCommand()) {
            return;
        }

        Recipe created;
        boolean assignNewName = getToAdd().getName() == null;
        try {
            Recipe recipeToAdd = createRecipeFromForm();
            created = saveNewRecipe(recipeToAdd, assignNewName);
            addRecipeToUserAuthoredRecipes(created);
        } catch (RuntimeException e) { // for data access layer failures
            finishExecutingFromError(e);
            return;
        } catch (IOException e) {
            // we've verified that the tags/ingredients/users are valid
            finishExecutingImpossibleOutcome(e);
            return;
        }
        finishExecutingSuccessfulRecipeCreation(created, !assignNewName);
    }

    private Recipe createRecipeFromForm() throws IOException {
        Recipe.Builder recipeBuilder = createBaseRecipeBuilder();

        if (getToAdd().getDirections() != null) {
            recipeBuilder.setDirections(getToAdd().getDirections());
        }

        addLoadedTags(recipeBuilder);
        addLoadedIngredients(recipeBuilder);
        return recipeBuilder.build();
    }

    private Recipe.Builder createBaseRecipeBuilder() {
        return new Recipe.Builder()
                .setName(getToAdd().getName())
                .setPresentationName(getToAdd().getPresentationName())
                .setAuthorUsername(getToAdd().getAuthorUsername())
                .setPrepTime(getToAdd().getPrepTime())
                .setCookTime(getToAdd().getCookTime())
                .setImageUri(getToAdd().getImageUri())
                .setNumServings(getToAdd().getNumServings())
                .setAvgRating(getToAdd().getAvgRating())
                .setNumRatings(getToAdd().getNumRatings());
    }

    private void addLoadedTags(Recipe.Builder recipeBuilder) throws IOException {
        assert getStorageSource() != null;

        if (getToAdd().getTags() != null) {
            List<Tag> tags =
                    getStorageSource()
                            .getLoader()
                            .getTagsByNames(new ArrayList<>(getToAdd().getTags()));
            recipeBuilder.setTags(new HashSet<>(tags));
        }
    }

    private void addLoadedIngredients(Recipe.Builder recipeBuilder) throws IOException {
        assert getStorageSource() != null;

        if (getToAdd().getRequiredIngredients() != null) {
            List<Ingredient> ingredients =
                    getStorageSource()
                            .getLoader()
                            .getIngredientsByNames(
                                    new ArrayList<>(getToAdd().getRequiredIngredients().keySet()));
            Map<Ingredient, Double> requiredIngredients = new HashMap<>();

            for (Ingredient i : ingredients) {
                requiredIngredients.put(i, getToAdd().getRequiredIngredients().get(i.getName()));
            }
            recipeBuilder.setRequiredIngredients(requiredIngredients);
        }
    }

    private Recipe saveNewRecipe(Recipe baseRecipe, boolean assignNewName) {
        assert getStorageSource() != null;

        Recipe toAdd = getRecipeWithValidName(baseRecipe, assignNewName);
        getStorageSource().getSaver().updateRecipes(Collections.singletonList(toAdd));
        return toAdd;
    }

    private Recipe getRecipeWithValidName(Recipe baseRecipe, boolean assignNewName) {
        assert getStorageSource() != null;

        Recipe recipe;
        if (assignNewName) {
            String assignedName =
                    getStorageSource()
                            .getLoader()
                            .generateUniqueRecipeName(baseRecipe.getPresentationName());
            recipe = Utils.renameRecipe(baseRecipe, assignedName);
        } else {
            recipe = baseRecipe;
        }
        return recipe;
    }

    private void addRecipeToUserAuthoredRecipes(Recipe toAdd) throws IOException {
        assert getStorageSource() != null;
        assert toAdd.getAuthorUsername() != null;
        User user =
                getStorageSource()
                        .getLoader()
                        .getUsersByNames(List.of(toAdd.getAuthorUsername()))
                        .get(0);

        User updatedUser = addAuthoredRecipeToUser(toAdd, user);

        getStorageSource().getSaver().updateUsers(Collections.singletonList(updatedUser));
    }

    private User addAuthoredRecipeToUser(Recipe toAdd, User user) {
        List<Recipe> authoredRecipes = new ArrayList<>(user.getAuthoredRecipes());
        authoredRecipes.add(toAdd);
        return new User.Builder(user).setAuthoredRecipes(authoredRecipes).build();
    }

    private void finishExecutingSuccessfulRecipeCreation(
            Recipe createdRecipe, boolean nameIsOriginal) {
        setCreatedRecipe(createdRecipe);
        setExecutionMessage(
                nameIsOriginal
                        ? OK_RECIPE_CREATED_WITH_GIVEN_NAME
                        : OK_RECIPE_CREATED_NAME_ASSIGNED);
        beSuccessful();
        finishExecuting();
    }
}
