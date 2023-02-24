/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CreateIngredientCommand extends EntityCommand {
    public static final String OK_INGREDIENT_CREATED = "Ingredient creation successful",
            NOT_OK_INVALID_INGREDIENT =
                    "Ingredient creation unsuccessful: the ingredient was invalid or null",
            NOT_OK_INGREDIENT_NAME_TAKEN =
                    "Ingredient creation unsuccessful: the ingredient name is already taken";

    private final Ingredient toAdd;
    private Ingredient createdIngredient;

    /**
     * Creates an action item for a new Ingredient to be created and saved into the EntityStorage
     * given to this command.
     *
     * @param toAdd the Ingredient to save
     */
    public CreateIngredientCommand(Ingredient toAdd) {
        this.toAdd = toAdd;
    }

    /**
     * Creates an action item for a new Ingredient to be created and saved into the EntityStorage
     * given to this command.
     *
     * @param ingredientName the new, unique name of the Ingredient to save
     * @param units the units of the Ingredient to save
     * @param imageUri the URI of an image of the Ingredient to save
     */
    public CreateIngredientCommand(String ingredientName, String units, String imageUri) {
        this(new Ingredient(ingredientName, units, imageUri));
    }

    /**
     * Returns the (output) Ingredient that was created/saved when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) Ingredient that was created/saved, if the command successfully added
     *     it; null otherwise.
     */
    @Nullable public Ingredient getCreatedIngredient() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return createdIngredient;
    }

    private void setCreatedIngredient(@NotNull Ingredient createdIngredient) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the created tag after command has executed");
        }
        if (this.createdIngredient != null) {
            throw new IllegalStateException("Can only set created tag once");
        }
        Objects.requireNonNull(createdIngredient);
        this.createdIngredient = createdIngredient;
    }

    public Ingredient getToAdd() {
        return toAdd;
    }

    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isIngredientValid()) {
            return NOT_OK_INVALID_INGREDIENT;
        }
        try {
            if (!isIngredientNameAvailable()) {
                return NOT_OK_INGREDIENT_NAME_TAKEN;
            }
        } catch (RuntimeException e) { // for data access layer failures
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isIngredientValid() {
        return getToAdd() != null && getToAdd().getName() != null;
    }

    private boolean isIngredientNameAvailable() {
        assert getStorageSource() != null;
        assert getToAdd().getName() != null;
        return !getStorageSource().getLoader().ingredientNameExists(getToAdd().getName());
    }

    /**
     * Has the Ingredient be (created and) saved. If the ingredient name is null or corresponds to
     * an already-existing ingredient, then this command's execution will be unsuccessful.
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
            saveIngredient(getToAdd());
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        }

        finishExecutingSuccessfulIngredientCreation(getToAdd());
    }

    private void saveIngredient(Ingredient toSave) {
        assert getStorageSource() != null;

        getStorageSource().getSaver().updateIngredients(Collections.singletonList(toSave));
    }

    private void finishExecutingSuccessfulIngredientCreation(Ingredient created) {
        setCreatedIngredient(created);
        setExecutionMessage(OK_INGREDIENT_CREATED);
        beSuccessful();
        finishExecuting();
    }
}
