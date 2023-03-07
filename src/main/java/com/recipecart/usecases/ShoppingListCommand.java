/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.User;
import com.recipecart.storage.EntitySaver;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This abstract class represents an action item that represents a use case involving a user's
 * shopping list.
 */
public abstract class ShoppingListCommand extends EntityCommand {
    public static final String OK_SHOPPING_LIST_UPDATED = "Shopping list update successful",
            NOT_OK_INVALID_USERNAME =
                    "Shopping list update unsuccessful: the given username is null or invalid",
            NOT_OK_USER_NOT_FOUND =
                    "Shopping list update unsuccessful: the given username doesn't correspond to an"
                            + " existing user";

    private final String shopperUsername;
    private Map<Ingredient, Double> resultShoppingList = null;

    ShoppingListCommand(String shopperUsername) {
        this.shopperUsername = shopperUsername;
    }

    /**
     * @return the username of the user whose shopping list is involved with this command.
     */
    public String getShopperUsername() {
        return shopperUsername;
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
        try {
            if (!doesUserExist()) {
                return NOT_OK_USER_NOT_FOUND;
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isUsernameValid() {
        return getShopperUsername() != null;
    }

    private boolean doesUserExist() {
        assert getStorageSource() != null;
        return getStorageSource().getLoader().usernameExists(getShopperUsername());
    }

    /**
     * Updates the shopping list according to the spec of the implementing class. If the given
     * username is null or doesn't correspond to an existing user in the given EntityStorage, this
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

        Map<Ingredient, Double> updatedShoppingList;
        try {
            updatedShoppingList = performShoppingListUpdate();
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        } catch (IOException e) {
            finishExecutingImpossibleOutcome(e);
            return;
        }

        finishExecutingSuccessfulShoppingListUpdate(updatedShoppingList);
    }

    /**
     * Performs the actual shopping list updating according to the spec of the implementing class.
     *
     * @return the updated shopping list
     * @throws IOException (shouldn't happen, since the implementing command should check beforehand
     *     if the entities it references by name exist)
     */
    protected abstract Map<Ingredient, Double> performShoppingListUpdate() throws IOException;

    private static User getUserWithUpdatedShoppingList(
            User user, Map<Ingredient, Double> shoppingListUpdate) {
        return new User.Builder(user).setShoppingList(shoppingListUpdate).build();
    }

    /**
     * Saves the given user into the storage, except with their shopping list updated to the given
     * one.
     *
     * @param shopper the user whose shopping list is to be updated; remains unmodified
     * @param updatedShoppingList the new shopping list to give the user
     */
    protected void saveUpdatedShoppingList(
            User shopper, Map<Ingredient, Double> updatedShoppingList) {
        User updatedShopper = getUserWithUpdatedShoppingList(shopper, updatedShoppingList);
        assert getStorageSource() != null;
        saveUser(updatedShopper, getStorageSource().getSaver());
    }

    /**
     * Returns the user's updated shopping list as a result of this command's execution.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) updated shopping list, if the command successfully updated it; null
     *     otherwise.
     */
    @Nullable public Map<Ingredient, Double> getResultShoppingList() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return Utils.allowNull(resultShoppingList, Collections::unmodifiableMap);
    }

    /**
     * @return the saved User associated with the username given to this command.
     */
    protected User getShopper() throws IOException {
        assert getStorageSource() != null;
        return getStorageSource()
                .getLoader()
                .getUsersByNames(Collections.singletonList(getShopperUsername()))
                .get(0);
    }

    private static void saveUser(User user, EntitySaver saver) {
        saver.updateUsers(Collections.singleton(user));
    }

    /**
     * Sets this command's output state so that its result shopping list is the given one.
     *
     * @param resultShoppingList the shopping list to put in the command's output
     * @throws IllegalStateException if this method has been called before, or if it's called after
     *     finishExecuting() was called.
     */
    protected void setResultShoppingList(
            @NotNull Map<@NotNull Ingredient, @NotNull Double> resultShoppingList) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set shopping list after command finished executing");
        }
        if (this.resultShoppingList != null) {
            throw new IllegalStateException("Can only set the shopping list once");
        }
        Utils.requireAllMapNotNull(
                resultShoppingList,
                "Shopping list cannot be null",
                "Shopping list ingredients cannot be null",
                "Shopping list amounts cannot be null");
        this.resultShoppingList = resultShoppingList;
    }

    /**
     * Performs necessary steps to set the command's visible state to where its execution is
     * finished and successful and has the proper output shopping list.
     *
     * @param resultShoppingList the resulting shopping list, to set the command's output shopping
     *     list to.
     */
    protected void finishExecutingSuccessfulShoppingListUpdate(
            Map<Ingredient, Double> resultShoppingList) {
        setResultShoppingList(resultShoppingList);
        setExecutionMessage(OK_SHOPPING_LIST_UPDATED);
        beSuccessful();
        finishExecuting();
    }
}
