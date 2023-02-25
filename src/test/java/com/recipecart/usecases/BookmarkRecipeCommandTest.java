/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.usecases.BookmarkRecipeCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.database.MockEntitySaveAndLoader;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class BookmarkRecipeCommandTest {
    private EntityStorage storage;

    @BeforeEach
    void initStorage() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        storage = new EntityStorage(saveAndLoader, saveAndLoader);
    }

    private static Stream<Arguments> getUserAndRecipe() {
        return TestUtils.generateMultiArguments(
                List.of(TestData::getUsers, TestData::getRecipes), 1, true);
    }

    private static Stream<Arguments> getUser() {
        return TestUtils.generateArguments(TestData::getUsers);
    }

    private static Stream<Arguments> getRecipe() {
        return TestUtils.generateArguments(TestData::getRecipes);
    }

    private BookmarkRecipeCommand getAndExecuteCommand(
            User bookmarker,
            Recipe toBookmark,
            boolean saveUser,
            boolean saveRecipe,
            boolean setStorageSource) {
        if (saveUser) {
            storage.getSaver().updateUsers(Collections.singleton(bookmarker));
        }
        if (saveRecipe) {
            storage.getSaver().updateRecipes(Collections.singleton(toBookmark));
        }
        BookmarkRecipeCommand command =
                new BookmarkRecipeCommand(
                        Utils.allowNull(bookmarker, User::getUsername),
                        Utils.allowNull(toBookmark, Recipe::getName));
        if (setStorageSource) {
            command.setStorageSource(storage);
        }
        command.execute();

        return command;
    }

    private BookmarkRecipeCommand getAndExecuteCommand(User bookmarker, Recipe toBookmark) {
        return getAndExecuteCommand(bookmarker, toBookmark, true, true, true);
    }

    private static void assertSuccessfulExecution(BookmarkRecipeCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());
    }

    private static void assertUnsuccessfulExecution(BookmarkRecipeCommand command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());
    }

    private void assertRecipeIsSaved(Recipe expected, String username) throws IOException {
        User savedUser =
                storage.getLoader().getUsersByNames(Collections.singletonList(username)).get(0);

        assertTrue(savedUser.getSavedRecipes().contains(expected));
    }

    private User withoutSavedRecipes(User baseUser) {
        return new User.Builder(baseUser).setSavedRecipes(Collections.emptyList()).build();
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testState(User user, Recipe recipe) {
        BookmarkRecipeCommand command =
                new BookmarkRecipeCommand(user.getUsername(), recipe.getName());

        assertEquals(user.getUsername(), command.getBookmarkerUsername());
        assertEquals(recipe.getName(), command.getRecipeName());
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testBookmarkRecipe(User baseUser, Recipe recipe) throws IOException {
        User user = withoutSavedRecipes(baseUser);
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe);

        assertSuccessfulExecution(command, OK_RECIPE_BOOKMARKED);
        assertRecipeIsSaved(recipe, user.getUsername());
    }

    @ParameterizedTest
    @MethodSource("getRecipe")
    void testNullUsername(Recipe recipe) {
        BookmarkRecipeCommand command = getAndExecuteCommand(null, recipe, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_USERNAME);
    }

    @ParameterizedTest
    @MethodSource("getUser")
    void testNullRecipeName(User user) {
        BookmarkRecipeCommand command = getAndExecuteCommand(user, null, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_INVALID_RECIPE_NAME);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testUserNotFound(User user, Recipe recipe) {
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe, false, true, true);

        assertUnsuccessfulExecution(command, NOT_OK_USER_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testRecipeNotFound(User user, Recipe recipe) {
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe, true, false, true);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_NOT_FOUND);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testRecipeAlreadyBookmarked(User baseUser, Recipe recipe) {
        User user =
                new User.Builder(baseUser)
                        .setSavedRecipes(Collections.singletonList(recipe))
                        .build();
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe);

        assertUnsuccessfulExecution(command, NOT_OK_RECIPE_ALREADY_BOOKMARKED);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testNullStorageSource(User baseUser, Recipe recipe) {
        User user = withoutSavedRecipes(baseUser);
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe, true, true, false);

        assertUnsuccessfulExecution(command, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testBookmarkRecipeWithError(User baseUser, Recipe recipe) {
        User user = withoutSavedRecipes(baseUser);
        EntityStorage storage = new EntityStorage(new BadEntitySaver(), new BadEntityLoader());
        BookmarkRecipeCommand command =
                new BookmarkRecipeCommand(user.getUsername(), recipe.getName());
        command.setStorageSource(storage);
        command.execute();

        assertUnsuccessfulExecution(command, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getUserAndRecipe")
    void testExceptionsAfterBookmarkRecipe(User baseUser, Recipe recipe) {
        User user = withoutSavedRecipes(baseUser);
        BookmarkRecipeCommand command = getAndExecuteCommand(user, recipe);

        assertThrows(IllegalStateException.class, command::execute);
    }
}
