/* (C)2023 */
package com.recipecart.entities;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.testutil.Presets;
import com.recipecart.testutil.TestData;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UserTest {
    static Stream<Arguments> builderParams() {
        return Presets.userArgs(1).get();
    }

    static Stream<Arguments> builderParamsDataStructuresOnly() {
        return generateMultiArguments(
                List.of(
                        TestData::getListRecipeNoNulls,
                        TestData::getMapRecipeDoubleNoNulls,
                        TestData::getSetIngredientNoNulls,
                        TestData::getMapIngredientDoubleNoNulls),
                new int[] {2, 1, 1, 1},
                true);
    }

    static Stream<Arguments> userParams() {
        return generateArguments(TestData::getUsers);
    }

    static Stream<Arguments> unequalBuilderParams() {
        return Presets.userArgs(2).get();
    }

    @Test
    void testDefaultBuilder() {
        User user = new User.Builder().build();

        assertNotNull(user);
        assertNull(user.getUsername());
        assertNull(user.getEmailAddress());
        assertNotNull(user.getAuthoredRecipes());
        assertEquals(0, user.getAuthoredRecipes().size());
        assertNotNull(user.getSavedRecipes());
        assertEquals(0, user.getSavedRecipes().size());
        assertNotNull(user.getRatedRecipes());
        assertEquals(0, user.getRatedRecipes().size());
        assertNotNull(user.getOwnedIngredients());
        assertEquals(0, user.getOwnedIngredients().size());
        assertNotNull(user.getShoppingList());
        assertEquals(0, user.getShoppingList().size());
    }

    @ParameterizedTest
    @MethodSource("builderParams")
    void testBuilder(
            @NotNull String s1,
            String s2,
            @NotNull List<@NotNull Recipe> lr1,
            @NotNull List<@NotNull Recipe> lr2,
            @NotNull Map<@NotNull Recipe, @NotNull Double> mrd1,
            @NotNull Set<@NotNull Ingredient> si1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> mid1) {
        User user =
                new User.Builder()
                        .setUsername(s1)
                        .setEmailAddress(s2)
                        .setAuthoredRecipes(lr1)
                        .setSavedRecipes(lr2)
                        .setRatedRecipes(mrd1)
                        .setOwnedIngredients(si1)
                        .setShoppingList(mid1)
                        .build();

        assertNotNull(user);
        assertEquals(s1, user.getUsername());
        assertEquals(s2, user.getEmailAddress());
        assertEquals(lr1, user.getAuthoredRecipes());
        assertEquals(lr2, user.getSavedRecipes());
        assertEquals(mrd1, user.getRatedRecipes());
        assertEquals(si1, user.getOwnedIngredients());
        assertEquals(mid1, user.getShoppingList());
    }

    @ParameterizedTest
    @MethodSource("userParams")
    void testBuilderUserCopier(User user1) {
        User user2 = new User.Builder(user1).build();

        assertUsersEquals(user1, user2);
    }

    private static void assertUsersEquals(User expectedUser, User actualUser) {
        assertEquals(expectedUser, actualUser);
        assertEquals(expectedUser.getUsername(), actualUser.getUsername());
        assertEquals(expectedUser.getEmailAddress(), actualUser.getEmailAddress());
        assertEquals(expectedUser.getAuthoredRecipes(), actualUser.getAuthoredRecipes());
        assertEquals(expectedUser.getSavedRecipes(), actualUser.getSavedRecipes());
        assertEquals(expectedUser.getRatedRecipes(), actualUser.getRatedRecipes());
        assertEquals(expectedUser.getOwnedIngredients(), actualUser.getOwnedIngredients());
        assertEquals(expectedUser.getShoppingList(), actualUser.getShoppingList());
        assertEquals(expectedUser.hashCode(), actualUser.hashCode());
    }

    @ParameterizedTest
    @MethodSource("builderParamsDataStructuresOnly")
    void testImmutability(
            List<Recipe> authoredRecipesOriginal,
            List<Recipe> savedRecipesOriginal,
            Map<Recipe, Double> ratedRecipesOriginal,
            Set<Ingredient> ownedIngredientsOriginal,
            Map<Ingredient, Double> shoppingListOriginal) {
        // this test relies on Tag, Ingredient, and Recipe being immutable;
        // if this ever changes, then this test becomes invalid

        List<Recipe> authoredRecipes = new ArrayList<>(authoredRecipesOriginal),
                savedRecipes = new ArrayList<>(savedRecipesOriginal);
        Map<Recipe, Double> ratedRecipes = new HashMap<>(ratedRecipesOriginal);
        Set<Ingredient> ownedIngredients = new HashSet<>(ownedIngredientsOriginal);
        Map<Ingredient, Double> shoppingList = new HashMap<>(shoppingListOriginal);

        User user =
                new User.Builder()
                        .setAuthoredRecipes(authoredRecipes)
                        .setSavedRecipes(savedRecipes)
                        .setRatedRecipes(ratedRecipes)
                        .setOwnedIngredients(ownedIngredients)
                        .setShoppingList(shoppingList)
                        .build();

        assertEquals(authoredRecipesOriginal, user.getAuthoredRecipes());
        assertEquals(savedRecipesOriginal, user.getSavedRecipes());
        assertEquals(ratedRecipesOriginal, user.getRatedRecipes());
        assertEquals(ownedIngredientsOriginal, user.getOwnedIngredients());
        assertEquals(shoppingListOriginal, user.getShoppingList());

        authoredRecipes.clear();
        savedRecipes.clear();
        ratedRecipes.clear();
        ownedIngredients.clear();
        shoppingList.clear();

        List<Recipe> userAuthoredRecipes = user.getAuthoredRecipes();
        assertEquals(authoredRecipesOriginal, userAuthoredRecipes);
        List<Recipe> userSavedRecipes = user.getSavedRecipes();
        assertEquals(savedRecipesOriginal, userSavedRecipes);
        Map<Recipe, Double> userRatedRecipes = user.getRatedRecipes();
        assertEquals(ratedRecipesOriginal, userRatedRecipes);
        Set<Ingredient> userOwnedIngredients = user.getOwnedIngredients();
        assertEquals(ownedIngredientsOriginal, userOwnedIngredients);
        Map<Ingredient, Double> userShoppingList = user.getShoppingList();
        assertEquals(shoppingListOriginal, userShoppingList);

        assertThrows(UnsupportedOperationException.class, userAuthoredRecipes::clear);
        assertThrows(UnsupportedOperationException.class, userSavedRecipes::clear);
        assertThrows(UnsupportedOperationException.class, userRatedRecipes::clear);
        assertThrows(UnsupportedOperationException.class, userOwnedIngredients::clear);
        assertThrows(UnsupportedOperationException.class, userShoppingList::clear);
    }

    @ParameterizedTest
    @MethodSource("builderParams")
    void testEquality(
            @NotNull String s1,
            String s2,
            @NotNull List<@NotNull Recipe> lr1,
            @NotNull List<@NotNull Recipe> lr2,
            @NotNull Map<@NotNull Recipe, @NotNull Double> mrd1,
            @NotNull Set<@NotNull Ingredient> si1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> mid1) {
        User
                user1 =
                        new User.Builder()
                                .setUsername(s1)
                                .setEmailAddress(s2)
                                .setAuthoredRecipes(lr1)
                                .setSavedRecipes(lr2)
                                .setRatedRecipes(mrd1)
                                .setOwnedIngredients(si1)
                                .setShoppingList(mid1)
                                .build(),
                user2 =
                        new User.Builder()
                                .setUsername(newString(s1))
                                .setEmailAddress(newString(s2))
                                .setAuthoredRecipes(new ArrayList<>(lr1))
                                .setSavedRecipes(new ArrayList<>(lr2))
                                .setRatedRecipes(new HashMap<>(mrd1))
                                .setOwnedIngredients(new HashSet<>(si1))
                                .setShoppingList(new HashMap<>(mid1))
                                .build();

        assertUsersEquals(user1, user2);
    }

    @ParameterizedTest
    @MethodSource("unequalBuilderParams")
    void testInequality(
            @NotNull String u1s1,
            @NotNull String u2s1,
            String u1s2,
            String u2s2,
            @NotNull List<@NotNull Recipe> u1lr1,
            @NotNull List<@NotNull Recipe> u2lr1,
            @NotNull List<@NotNull Recipe> u1lr2,
            @NotNull List<@NotNull Recipe> u2lr2,
            @NotNull Map<@NotNull Recipe, @NotNull Double> u1mrd1,
            @NotNull Map<@NotNull Recipe, @NotNull Double> u2mrd1,
            @NotNull Set<@NotNull Ingredient> u1si1,
            @NotNull Set<@NotNull Ingredient> u2si1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> u1mid1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> u2mid1) {
        User
                user1 =
                        new User.Builder()
                                .setUsername(u1s1)
                                .setEmailAddress(u1s2)
                                .setAuthoredRecipes(u1lr1)
                                .setSavedRecipes(u1lr2)
                                .setRatedRecipes(u1mrd1)
                                .setOwnedIngredients(u1si1)
                                .setShoppingList(u1mid1)
                                .build(),
                user2 =
                        new User.Builder()
                                .setUsername(u2s1)
                                .setEmailAddress(u2s2)
                                .setAuthoredRecipes(u2lr1)
                                .setSavedRecipes(u2lr2)
                                .setRatedRecipes(u2mrd1)
                                .setOwnedIngredients(u2si1)
                                .setShoppingList(u2mid1)
                                .build();

        assertNotEquals(user1, user2);
        assertNotEquals(user1.getUsername(), user2.getUsername());
        assertNotEquals(user1.getEmailAddress(), user2.getEmailAddress());
        assertNotEquals(user1.getAuthoredRecipes(), user2.getAuthoredRecipes());
        assertNotEquals(user1.getSavedRecipes(), user2.getSavedRecipes());
        assertNotEquals(user1.getRatedRecipes(), user2.getRatedRecipes());
        assertNotEquals(user1.getOwnedIngredients(), user2.getOwnedIngredients());
        assertNotEquals(user1.getShoppingList(), user2.getShoppingList());
    }

    @Test
    void testNullChecking() {
        User.Builder builder = new User.Builder();

        assertThrows(NullPointerException.class, () -> builder.setAuthoredRecipes(null));
        List<Recipe> authoredRecipes = new ArrayList<>();
        authoredRecipes.add(Presets.recipe(0));
        authoredRecipes.add(null);
        assertThrows(NullPointerException.class, () -> builder.setAuthoredRecipes(authoredRecipes));

        assertThrows(NullPointerException.class, () -> builder.setSavedRecipes(null));
        List<Recipe> savedRecipes = new ArrayList<>();
        savedRecipes.add(null);
        savedRecipes.add(Presets.recipe(1));
        assertThrows(NullPointerException.class, () -> builder.setSavedRecipes(savedRecipes));

        assertThrows(NullPointerException.class, () -> builder.setRatedRecipes(null));
        Map<Recipe, Double> ratedRecipes = new HashMap<>();
        ratedRecipes.put(Presets.recipe(0), 4.);
        ratedRecipes.put(null, 5.);
        assertThrows(NullPointerException.class, () -> builder.setRatedRecipes(ratedRecipes));
        ratedRecipes.remove(null);
        ratedRecipes.put(Presets.recipe(1), null);
        assertThrows(NullPointerException.class, () -> builder.setRatedRecipes(ratedRecipes));

        assertThrows(NullPointerException.class, () -> builder.setOwnedIngredients(null));
        Set<Ingredient> ingredients = new HashSet<>();
        ingredients.add(null);
        ingredients.add(Presets.ingredient(0));
        assertThrows(NullPointerException.class, () -> builder.setOwnedIngredients(ingredients));

        assertThrows(NullPointerException.class, () -> builder.setShoppingList(null));
        Map<Ingredient, Double> shoppingList = new HashMap<>();
        shoppingList.put(Presets.ingredient(0), 5.);
        shoppingList.put(null, 5.);
        assertThrows(NullPointerException.class, () -> builder.setShoppingList(shoppingList));
        shoppingList.remove(null);
        shoppingList.put(Presets.ingredient(1), null);
        assertThrows(NullPointerException.class, () -> builder.setShoppingList(shoppingList));
    }
}
