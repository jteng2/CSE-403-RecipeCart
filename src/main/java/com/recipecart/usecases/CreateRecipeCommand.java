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
                            + " was available",
            OK_RECIPE_CREATED_NAME_ASSIGNED =
                    "Recipe creation successful: the recipe was assigned a new unique"
                            + " (non-presentation) name",
            NOT_OK_INVALID_RECIPE = "Recipe creation unsuccessful: the recipe was invalid or null",
            NOT_OK_RECIPE_RESOURCES_NOT_FOUND =
                    "Recipe creation unsuccessful: the user or ingredient name(s) doesn't"
                            + " correspond to an existing user or ingredient",
            NOT_OK_RECIPE_NAME_TAKEN =
                    "Recipe creation unsuccessful: the given unique (non-presentation) recipe name"
                            + " is already taken";

    private final RecipeForm recipeFormToAdd;
    private Recipe createdRecipe = null;
    private Set<Tag> createdTags = null;

    /**
     * Creates an action item for a user to create a Recipe.
     *
     * @param recipeToAdd the Recipe that the user creates
     */
    public CreateRecipeCommand(RecipeForm recipeToAdd) {
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

    /**
     * Returns any new Tags that were created and saved (that previously didn't exist in the
     * EntityStorage this command was given) in the process of creating this Recipe (i.e. the tags
     * whose names didn't yet have a corresponding tag in the storage)
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) Tags that were created, if this command successfully executed (this
     *     list can be empty); null otherwise
     */
    @Nullable public Set<@NotNull Tag> getCreatedTags() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return Utils.allowNull(createdTags, Collections::unmodifiableSet);
    }

    private void setCreatedTags(@NotNull Set<@NotNull Tag> tags) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the created recipe after command has executed");
        }
        if (this.createdTags != null) {
            throw new IllegalStateException("Can only set created recipe once");
        }
        Utils.requireAllNotNull(
                tags,
                "Cannot set created-tag set to null",
                "Cannot have null elements in the created-tag set");
        this.createdTags = tags;
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
            if (!doIngredientsExist()) {
                return NOT_OK_RECIPE_RESOURCES_NOT_FOUND;
            }
            if (!isRecipeNameAvailable()) {
                return NOT_OK_RECIPE_NAME_TAKEN;
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
        return getToAdd().getDirections() == null || !getToAdd().getDirections().contains(null);
    }

    private boolean areTagNamesValid() {
        return getToAdd().getTagNames() == null || !getToAdd().getTagNames().contains(null);
    }

    private boolean areIngredientNamesValid() {
        return getToAdd().getRequiredIngredients() == null
                || (!getToAdd().getRequiredIngredients().containsKey(null)
                        && !getToAdd().getRequiredIngredients().containsValue(null));
    }

    private boolean doIngredientsExist() {
        assert getStorageSource() != null;
        return getToAdd().getRequiredIngredients() == null
                || getToAdd().getRequiredIngredients().keySet().stream()
                        .allMatch(getStorageSource().getLoader()::ingredientNameExists);
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
     * Has the given user create the given Recipe, and has that Recipe saved. If the Recipe's
     * (non-presentation) name is null, a name will be assigned to it. If it's not null and the name
     * isn't taken, the Recipe will take that name. The command will be unsuccessful if:
     *
     * <ul>
     *   <li>The recipe name is non-null, but a recipe with that name already exists in the
     *       EntityStorage
     *   <li>If the Recipe's presentation name or author username is null
     *   <li>If any of the directions, tags, or keys/values of the required ingredients are null
     *   <li>If the author username doesn't correspond to an existing user in the EntityStorage this
     *       command was given
     *   <li>If any of the keys of the required ingredients don't correspond to an existing
     *       ingredient in the EntityStorage this command was given
     * </ul>
     *
     * If any of the Recipe's tags don't correspond to an existing tag in the EntityStorage this
     * command was given, then new tags will be created and saved for them.
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
        Set<Tag> createdTags = createMissingTags();
        if (createdTags == null) {
            finishExecutingFromError();
            return;
        }
        try {
            Recipe recipeToAdd = createRecipeFromForm();
            created = saveNewRecipe(recipeToAdd, assignNewName);
            addRecipeToAuthoredRecipesOfAuthor(created);
        } catch (RuntimeException e) { // for data access layer failures
            finishExecutingFromError(e);
            return;
        } catch (IOException e) {
            // we've verified that the tags/ingredients/users are valid
            finishExecutingImpossibleOutcome(e);
            return;
        }
        finishExecutingSuccessfulRecipeCreation(created, !assignNewName, createdTags);
    }

    // returns null if any missing tags don't get successfully created
    private @Nullable Set<Tag> createMissingTags() {
        if (getToAdd().getTagNames() != null) {
            assert getStorageSource() != null;

            Set<Tag> createdTags = new HashSet<>();
            getToAdd().getTagNames().stream()
                    .filter(this::tagIsMissing)
                    .forEach((tagName) -> createdTags.add(createTag(tagName)));

            if (createdTags.contains(null)) {
                return null;
            }
            return createdTags;
        }
        return Collections.emptySet();
    }

    private boolean tagIsMissing(String tagName) {
        assert getStorageSource() != null;
        return !getStorageSource().getLoader().tagNameExists(tagName);
    }

    private Tag createTag(String tagName) {
        assert getStorageSource() != null;
        CreateTagCommand tagCreation = new CreateTagCommand(tagName);
        tagCreation.setStorageSource(getStorageSource());
        tagCreation.execute();

        return tagCreation.getCreatedTag();
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

        if (getToAdd().getTagNames() != null) {
            List<Tag> tags =
                    getStorageSource()
                            .getLoader()
                            .getTagsByNames(new ArrayList<>(getToAdd().getTagNames()));
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

        Recipe toAdd = assignNewName ? generateRecipeWithValidName(baseRecipe) : baseRecipe;
        getStorageSource().getSaver().updateRecipes(Collections.singletonList(toAdd));
        return toAdd;
    }

    private Recipe generateRecipeWithValidName(Recipe baseRecipe) {
        assert getStorageSource() != null;
        String assignedName =
                getStorageSource()
                        .getLoader()
                        .generateUniqueRecipeName(baseRecipe.getPresentationName());
        return Utils.renameRecipe(baseRecipe, assignedName);
    }

    private void addRecipeToAuthoredRecipesOfAuthor(Recipe toAdd) throws IOException {
        assert getStorageSource() != null;
        User updatedUser = addRecipeToAuthoredRecipesOfUser(toAdd, getAuthorOfRecipe(toAdd));
        getStorageSource().getSaver().updateUsers(Collections.singletonList(updatedUser));
    }

    private User getAuthorOfRecipe(Recipe recipe) throws IOException {
        assert getStorageSource() != null;
        assert recipe.getAuthorUsername() != null;
        return getStorageSource()
                .getLoader()
                .getUsersByNames(Collections.singletonList(recipe.getAuthorUsername()))
                .get(0);
    }

    private User addRecipeToAuthoredRecipesOfUser(Recipe toAdd, User user) {
        List<Recipe> authoredRecipes = new ArrayList<>(user.getAuthoredRecipes());
        authoredRecipes.add(toAdd);
        return new User.Builder(user).setAuthoredRecipes(authoredRecipes).build();
    }

    private void finishExecutingSuccessfulRecipeCreation(
            Recipe createdRecipe, boolean nameIsOriginal, Set<Tag> createdTags) {
        setCreatedRecipe(createdRecipe);
        setCreatedTags(createdTags);
        setExecutionMessage(
                nameIsOriginal
                        ? OK_RECIPE_CREATED_WITH_GIVEN_NAME
                        : OK_RECIPE_CREATED_NAME_ASSIGNED);
        beSuccessful();
        finishExecuting();
    }
}
