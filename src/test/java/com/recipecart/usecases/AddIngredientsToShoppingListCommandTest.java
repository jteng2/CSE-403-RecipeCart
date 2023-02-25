/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.usecases.AddIngredientsToShoppingListCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.entities.Ingredient;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import com.recipecart.utils.Utils;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class AddIngredientsToShoppingListCommandTest
        extends ShoppingListCommandTest<AddIngredientsToShoppingListCommand> {

    private static Stream<Arguments> getUserAndIngredients() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getNonEmptyMapIngredientDoubleNoNulls),
                1,
                true);
    }

    private static Stream<Arguments> getIngredients() {
        return TestUtils.generateArguments(TestData::getNonEmptyMapIngredientDoubleNoNulls);
    }

    private static Stream<Arguments> getUserAndInvalidIngredients() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getMapIngredientDoubleWithNulls), 1, true);
    }

    private static <T> Map<String, T> ingredientsToNames(Map<Ingredient, T> ingredients) {
        Map<String, T> names = new HashMap<>();
        for (Map.Entry<Ingredient, T> entry : ingredients.entrySet()) {
            names.put(Utils.allowNull(entry.getKey(), Ingredient::getName), entry.getValue());
        }
        return names;
    }

    private static <T> Set<T> excludeNulls(Set<T> set) {
        Set<T> withoutNull = new HashSet<>(set);
        withoutNull.remove(null);
        return withoutNull;
    }

    private AddIngredientsToShoppingListCommand getAndExecuteCommand(
            User shopper,
            Map<Ingredient, Double> shoppingListUpdate,
            boolean saveUser,
            boolean saveIngredients,
            boolean setStorageSource) {
        if (saveUser && shopper != null) {
            getStorage().getSaver().updateUsers(Collections.singleton(shopper));
        }
        if (saveIngredients && shoppingListUpdate != null) {
            getStorage().getSaver().updateIngredients(excludeNulls(shoppingListUpdate.keySet()));
        }
        AddIngredientsToShoppingListCommand command =
                new AddIngredientsToShoppingListCommand(
                        Utils.allowNull(shopper, User::getUsername),
                        Utils.allowNull(
                                shoppingListUpdate,
                                AddIngredientsToShoppingListCommandTest::ingredientsToNames));
        if (setStorageSource) {
            command.setStorageSource(getStorage());
        }
        command.execute();

        return command;
    }

    private AddIngredientsToShoppingListCommand getAndExecuteCommand(
            User shopper, Map<Ingredient, Double> shoppingListUpdate) {
        return getAndExecuteCommand(shopper, shoppingListUpdate, true, true, true);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testState(User user, Map<Ingredient, Double> ingredients) {
        Map<String, Double> ingredientNames = ingredientsToNames(ingredients);
        AddIngredientsToShoppingListCommand command =
                new AddIngredientsToShoppingListCommand(user.getUsername(), ingredientNames);

        assertEquals(user.getUsername(), command.getShopperUsername());
        assertEquals(ingredientNames, command.getIngredientsToAdd());
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testGetResultShoppingListBeforeExecution(User user, Map<Ingredient, Double> ingredients) {
        Map<String, Double> ingredientNames = ingredientsToNames(ingredients);
        AddIngredientsToShoppingListCommand command =
                new AddIngredientsToShoppingListCommand(user.getUsername(), ingredientNames);

        assertThrows(IllegalStateException.class, command::getResultShoppingList);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testAddIngredientsToShoppingList(User user, Map<Ingredient, Double> ingredients)
            throws IOException {
        AddIngredientsToShoppingListCommand command = getAndExecuteCommand(user, ingredients);

        assertSuccessfulExecution(command, OK_SHOPPING_LIST_UPDATED);
        assertShoppingListGood(command);
    }

    @ParameterizedTest
    @MethodSource("getIngredients")
    void testNullUsername(Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command =
                getAndExecuteCommand(null, ingredients, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("getUser")
    void testNullIngredientMap(User user) {
        AddIngredientsToShoppingListCommand command =
                getAndExecuteCommand(user, null, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_INGREDIENTS);
    }

    @ParameterizedTest
    @MethodSource("getUserAndInvalidIngredients")
    void testInvalidIngredients(User user, Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command = getAndExecuteCommand(user, ingredients);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_INGREDIENTS);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testUserNotFound(User user, Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command =
                getAndExecuteCommand(user, ingredients, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_USER_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testIngredientsNotFound(User user, Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command =
                getAndExecuteCommand(user, ingredients, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_INGREDIENT_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testNullStorageSource(User user, Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command =
                getAndExecuteCommand(user, ingredients, true, true, false);

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testAddIngredientsToShoppingListWithError(User user, Map<Ingredient, Double> ingredients) {
        EntityStorage badStorage = new EntityStorage(new BadEntitySaver(), new BadEntityLoader());
        AddIngredientsToShoppingListCommand command =
                new AddIngredientsToShoppingListCommand(
                        user.getUsername(), ingredientsToNames(ingredients));
        command.setStorageSource(badStorage);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getUserAndIngredients")
    void testExceptionsAfterAddIngredientsToShoppingList(
            User user, Map<Ingredient, Double> ingredients) {
        AddIngredientsToShoppingListCommand command = getAndExecuteCommand(user, ingredients);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
