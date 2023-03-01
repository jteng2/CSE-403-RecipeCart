/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.usecases.AddRecipeToShoppingListCommand.*;
import static com.recipecart.usecases.Command.NOT_OK_ERROR;
import static com.recipecart.usecases.EntityCommand.NOT_OK_BAD_STORAGE;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AddRecipeToShoppingListCommandTest
        extends ShoppingListCommandTest<AddRecipeToShoppingListCommand> {
    private static Stream<Arguments> getUserAndRecipe() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getRecipes), 1, true);
    }

    private static Stream<Arguments> getRecipe() {
        return TestUtils.generateArguments(TestData::getRecipes);
    }

    private AddRecipeToShoppingListCommand getAndExecuteCommand(
            User shopper,
            Recipe recipe,
            boolean addOnlyMissingIngredients,
            boolean saveUser,
            boolean saveRecipe,
            boolean setStorageSource) {
        if (saveUser && shopper != null) {
            getStorage().getSaver().updateUsers(Collections.singleton(shopper));
        }
        if (saveRecipe && recipe != null) {
            getStorage().getSaver().updateRecipes(Collections.singleton(recipe));
        }
        AddRecipeToShoppingListCommand command =
                new AddRecipeToShoppingListCommand(
                        Utils.allowNull(shopper, User::getUsername),
                        Utils.allowNull(recipe, Recipe::getName),
                        addOnlyMissingIngredients);

        if (setStorageSource) {
            command.setStorageSource(getStorage());
        }
        command.execute();

        return command;
    }

    private AddRecipeToShoppingListCommand getAndExecuteCommand(
            User shopper, Recipe recipe, boolean addOnlyMissingIngredients) {
        return getAndExecuteCommand(shopper, recipe, addOnlyMissingIngredients, true, true, true);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testState(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                new AddRecipeToShoppingListCommand(user.getUsername(), recipe.getName(), false);

        assertEquals(user.getUsername(), command.getShopperUsername());
        assertEquals(recipe.getName(), command.getRecipeName());
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testGetResultShoppingListBeforeExecution(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                new AddRecipeToShoppingListCommand(user.getUsername(), recipe.getName(), false);

        assertThrows(IllegalStateException.class, command::getResultShoppingList);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testAddRecipeToShoppingList(User user, Recipe recipe) throws IOException {
        AddRecipeToShoppingListCommand command = getAndExecuteCommand(user, recipe, false);

        assertSuccessfulExecution(command, OK_SHOPPING_LIST_UPDATED);
        assertShoppingListGood(command);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testAddRecipeToShoppingListMissingIngredientsOnly(User user, Recipe recipe)
            throws IOException {
        AddRecipeToShoppingListCommand command = getAndExecuteCommand(user, recipe, true);

        assertSuccessfulExecution(command, OK_SHOPPING_LIST_UPDATED);
        assertShoppingListGood(command);
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testNullUsername(Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                getAndExecuteCommand(null, recipe, false, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("getUser")
    void testNullRecipe(User user) {
        AddRecipeToShoppingListCommand command =
                getAndExecuteCommand(user, null, false, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testUserNotFound(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                getAndExecuteCommand(user, recipe, false, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_USER_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testRecipeNotFound(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                getAndExecuteCommand(user, recipe, false, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testNullStorageSource(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command =
                getAndExecuteCommand(user, recipe, false, true, true, false);

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testAddRecipeToShoppingListWithError(User user, Recipe recipe) {
        EntityStorage badStorage = new EntityStorage(new BadEntitySaver(), new BadEntityLoader());
        AddRecipeToShoppingListCommand command =
                new AddRecipeToShoppingListCommand(user.getUsername(), recipe.getName(), false);
        command.setStorageSource(badStorage);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testExceptionsAfterAddRecipeToShoppingList(User user, Recipe recipe) {
        AddRecipeToShoppingListCommand command = getAndExecuteCommand(user, recipe, false);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
