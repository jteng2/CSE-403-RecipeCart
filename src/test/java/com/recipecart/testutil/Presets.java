/* (C)2023 */
package com.recipecart.testutil;

import static com.recipecart.testutil.TestData.NUM_PARAM_COMBOS;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

/**
 * This class contains methods to generate preset Arguments for creating various entities for
 * parameterized tests, as well as methods to generate preset entities that can be easily created
 * for testing.
 */
public class Presets {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    // ParameterizedTest argument generators:
    // - each Arguments in a Stream is a list of parameters for a ParameterizedTest
    // - the Arguments here give everything needed to construct/build a given entity
    // - numTags/numIngredients/etc. is for how many Tags/Ingredients/etc. you want parameters for
    //    - ex. if you set that value to 2, the Arguments will have twice as many parameters
    //       than if you'd set the value to 1, since there's two entities to construct

    private static Supplier<Object[]> getIdSupplier(boolean valid) {
        return valid ? TestData::getNotNullStrings : TestData::getNullStrings;
    }

    public static Supplier<Stream<Arguments>> tagArgs(
            int numTags, boolean staircase, boolean valid) {
        return () ->
                TestUtils.generateMultiArguments(List.of(getIdSupplier(valid)), numTags, staircase);
    }

    public static Supplier<Stream<Arguments>> tagArgs(int numTags, boolean staircase) {
        return tagArgs(numTags, staircase, true);
    }

    public static Supplier<Stream<Arguments>> tagArgs(int numTags) {
        return tagArgs(numTags, true);
    }

    public static Supplier<Stream<Arguments>> invalidTagArgs(int numTags, boolean staircase) {
        return tagArgs(numTags, staircase, false);
    }

    public static Supplier<Stream<Arguments>> invalidTagArgs(int numTags) {
        return invalidTagArgs(numTags, true);
    }

    public static Supplier<Stream<Arguments>> ingredientArgs(
            int numIngredients, boolean staircase, boolean valid) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(getIdSupplier(valid), TestData::getStrings),
                        new int[] {numIngredients, numIngredients * 2},
                        staircase);
    }

    public static Supplier<Stream<Arguments>> ingredientArgs(
            int numIngredients, boolean staircase) {
        return ingredientArgs(numIngredients, staircase, true);
    }

    public static Supplier<Stream<Arguments>> ingredientArgs(int numIngredients) {
        return ingredientArgs(numIngredients, true);
    }

    public static Supplier<Stream<Arguments>> invalidIngredientArgs(
            int numIngredients, boolean staircase) {
        return ingredientArgs(numIngredients, staircase, false);
    }

    public static Supplier<Stream<Arguments>> invalidIngredientArgs(int numIngredients) {
        return invalidIngredientArgs(numIngredients, true);
    }

    public static Supplier<Stream<Arguments>> recipeArgs(
            int numRecipes, boolean staircase, boolean valid) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(
                                getIdSupplier(valid),
                                TestData::getStrings,
                                TestData::getIntegers,
                                TestData::getInts,
                                TestData::getPrimitiveDoubles,
                                TestData::getListStringNoNulls,
                                TestData::getSetTagNoNulls,
                                TestData::getMapIngredientDoubleNoNulls),
                        new int[] {
                            numRecipes,
                            numRecipes * 3,
                            numRecipes * 3,
                            numRecipes,
                            numRecipes,
                            numRecipes,
                            numRecipes,
                            numRecipes
                        },
                        staircase);
    }

    public static Supplier<Stream<Arguments>> recipeArgs(int numRecipes, boolean staircase) {
        return recipeArgs(numRecipes, staircase, true);
    }

    public static Supplier<Stream<Arguments>> recipeArgs(int numRecipes) {
        return recipeArgs(numRecipes, true);
    }

    public static Supplier<Stream<Arguments>> invalidRecipeArgs(int numRecipes, boolean staircase) {
        return recipeArgs(numRecipes, staircase, false);
    }

    public static Supplier<Stream<Arguments>> invalidRecipeArgs(int numRecipes) {
        return invalidRecipeArgs(numRecipes, true);
    }

    public static Supplier<Stream<Arguments>> userArgs(
            int numUsers, boolean staircase, boolean valid) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(
                                getIdSupplier(valid),
                                TestData::getStrings,
                                TestData::getListRecipeNoNulls,
                                TestData::getMapRecipeDoubleNoNulls,
                                TestData::getSetIngredientNoNulls,
                                TestData::getMapIngredientDoubleNoNulls),
                        new int[] {numUsers, numUsers, numUsers * 2, numUsers, numUsers, numUsers},
                        staircase);
    }

    public static Supplier<Stream<Arguments>> userArgs(int numUsers, boolean staircase) {
        return userArgs(numUsers, staircase, true);
    }

    public static Supplier<Stream<Arguments>> userArgs(int numUsers) {
        return userArgs(numUsers, true);
    }

    public static Supplier<Stream<Arguments>> invalidUserArgs(int numUsers, boolean staircase) {
        return userArgs(numUsers, staircase, false);
    }

    public static Supplier<Stream<Arguments>> invalidUserArgs(int numUsers) {
        return invalidUserArgs(numUsers, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Preset creators:
    // - Each preset entity is generated deterministically, where entity fields are determined
    //    through the TestData data and ParameterizedTest argument generators
    //    - (ex. calling tag(1) will construct the same Tag every time unless above data changes)
    // - All generated presets are compatible with the rest of the business logic layer
    //    - (i.e. Tag/Ingredient/Recipe names and User usernames are not null)

    // Gets the (tagNum)'th preset Tag
    public static Tag tag(int tagNum, boolean valid) {
        checkPresetNum(tagNum);

        Supplier<Stream<Arguments>> args = valid ? tagArgs(1, true) : invalidTagArgs(1, true);
        Object[] fields = getFields(args, tagNum);

        return new Tag((String) fields[0]);
    }

    public static Tag tag(int tagNum) {
        return tag(tagNum, true);
    }

    public static Tag invalidTag(int tagNum) {
        return tag(tagNum, false);
    }

    // Gets the (ingredientNum)'th preset Ingredient
    public static Ingredient ingredient(int ingredientNum, boolean valid) {
        checkPresetNum(ingredientNum);

        Supplier<Stream<Arguments>> args =
                valid ? ingredientArgs(1, true) : invalidIngredientArgs(1, true);
        Object[] fields = getFields(args, ingredientNum);

        return new Ingredient((String) fields[0], (String) fields[1], (String) fields[2]);
    }

    public static Ingredient ingredient(int ingredientNum) {
        return ingredient(ingredientNum, true);
    }

    public static Ingredient invalidIngredient(int ingredientNum) {
        return ingredient(ingredientNum, false);
    }

    // Gets the (recipeNum)'th preset Recipe
    // For the unsafe casts in methods "recipe" and "user", the corresponding object's type has been
    // checked to be of the type to be cast to.
    @SuppressWarnings("unchecked")
    public static Recipe recipe(int recipeNum, boolean valid) {
        checkPresetNum(recipeNum);

        Supplier<Stream<Arguments>> args = valid ? recipeArgs(1, true) : invalidRecipeArgs(1, true);
        Object[] fields = getFields(args, recipeNum);

        return new Recipe.Builder()
                .setName((String) fields[0])
                .setPresentationName((String) fields[1])
                .setAuthorUsername((String) fields[2])
                .setImageUri((String) fields[3])
                .setPrepTime((Integer) fields[4])
                .setCookTime((Integer) fields[5])
                .setNumServings((Integer) fields[6])
                .setNumRatings((int) fields[7])
                .setAvgRating((double) fields[8])
                .setDirections((List<String>) fields[9])
                .setTags((Set<Tag>) fields[10])
                .setRequiredIngredients((Map<Ingredient, Double>) fields[11])
                .build();
    }

    public static Recipe recipe(int recipeNum) {
        return recipe(recipeNum, true);
    }

    public static Recipe invalidRecipe(int recipeNum) {
        return recipe(recipeNum, false);
    }

    // Gets the (userNum)'th preset User
    @SuppressWarnings("unchecked")
    public static User user(int userNum, boolean valid) {
        checkPresetNum(userNum);

        Supplier<Stream<Arguments>> args = valid ? userArgs(1, true) : invalidUserArgs(1, true);
        Object[] fields = getFields(args, userNum);

        return new User.Builder()
                .setUsername((String) fields[0])
                .setEmailAddress((String) fields[1])
                .setAuthoredRecipes((List<Recipe>) fields[2])
                .setSavedRecipes((List<Recipe>) fields[3])
                .setRatedRecipes((Map<Recipe, Double>) fields[4])
                .setOwnedIngredients((Set<Ingredient>) fields[5])
                .setShoppingList((Map<Ingredient, Double>) fields[6])
                .build();
    }

    public static User user(int userNum) {
        return user(userNum, true);
    }

    public static User invalidUser(int userNum) {
        return user(userNum, false);
    }

    // Converts the (comboNum)'th "element" of paramCombos.get() into its corresponding array of
    // parameters
    private static Object[] getFields(Supplier<Stream<Arguments>> paramCombos, int comboNum) {
        List<Arguments> paramComboList = paramCombos.get().collect(Collectors.toList());

        return paramComboList.get(comboNum).get();
    }

    private static void checkPresetNum(int presetNum) {
        if (presetNum >= NUM_PARAM_COMBOS) {
            throw new IllegalArgumentException("There are only " + NUM_PARAM_COMBOS + " presets");
        }
    }
}
