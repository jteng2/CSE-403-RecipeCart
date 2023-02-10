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

    public static Supplier<Stream<Arguments>> tagArgs(int numTags, boolean staircase) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(TestData::getNotNullStrings), numTags, staircase);
    }

    public static Supplier<Stream<Arguments>> tagArgs(int numTags) {
        return tagArgs(numTags, true);
    }

    public static Supplier<Stream<Arguments>> ingredientArgs(
            int numIngredients, boolean staircase) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(TestData::getNotNullStrings, TestData::getStrings),
                        new int[] {numIngredients, numIngredients * 2},
                        staircase);
    }

    public static Supplier<Stream<Arguments>> ingredientArgs(int numIngredients) {
        return ingredientArgs(numIngredients, true);
    }

    public static Supplier<Stream<Arguments>> recipeArgs(int numRecipes, boolean staircase) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(
                                TestData::getNotNullStrings,
                                TestData::getStrings,
                                TestData::getIntegers,
                                TestData::getInts,
                                TestData::getPrimitiveDoubles,
                                TestData::getListStringNoNulls,
                                TestData::getSetTagNoNulls,
                                TestData::getMapIngredientDoubleNoNulls),
                        new int[] {
                            numRecipes,
                            numRecipes * 2,
                            numRecipes * 3,
                            numRecipes,
                            numRecipes,
                            numRecipes,
                            numRecipes,
                            numRecipes
                        },
                        staircase);
    }

    public static Supplier<Stream<Arguments>> recipeArgs(int numRecipes) {
        return recipeArgs(numRecipes, true);
    }

    public static Supplier<Stream<Arguments>> userArgs(int numUsers, boolean staircase) {
        return () ->
                TestUtils.generateMultiArguments(
                        List.of(
                                TestData::getNotNullStrings,
                                TestData::getStrings,
                                TestData::getListRecipeNoNulls,
                                TestData::getMapRecipeDoubleNoNulls,
                                TestData::getSetIngredientNoNulls,
                                TestData::getMapIngredientDoubleNoNulls),
                        new int[] {numUsers, numUsers, numUsers * 2, numUsers, numUsers, numUsers},
                        staircase);
    }

    public static Supplier<Stream<Arguments>> userArgs(int numUsers) {
        return userArgs(numUsers, true);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Preset creators:
    // - Each preset entity is generated deterministically, where entity fields are determined
    //    through the TestData data and ParameterizedTest argument generators
    //    - (ex. calling tag(1) will construct the same Tag every time unless above data changes)
    // - All generated presets are compatible with the rest of the business logic layer
    //    - (i.e. Tag/Ingredient/Recipe names and User usernames are not null)

    // Gets the (tagNum)'th preset Tag
    public static Tag tag(int tagNum) {
        checkPresetNum(tagNum);

        Object[] fields = getFields(tagArgs(1, true), tagNum);

        return new Tag((String) fields[0]);
    }

    // Gets the (ingredientNum)'th preset Ingredient
    public static Ingredient ingredient(int ingredientNum) {
        checkPresetNum(ingredientNum);

        Object[] fields = getFields(ingredientArgs(1, true), ingredientNum);

        return new Ingredient((String) fields[0], (String) fields[1], (String) fields[2]);
    }

    // Gets the (recipeNum)'th preset Recipe
    // For the unsafe casts in methods "recipe" and "user", the corresponding object's type has been
    // checked to be of the type to be cast to.
    @SuppressWarnings("unchecked")
    public static Recipe recipe(int recipeNum) {
        checkPresetNum(recipeNum);

        Object[] fields = getFields(recipeArgs(1, true), recipeNum);

        return new Recipe.Builder()
                .setName((String) fields[0])
                .setPresentationName((String) fields[1])
                .setImageUri((String) fields[2])
                .setPrepTime((Integer) fields[3])
                .setCookTime((Integer) fields[4])
                .setNumServings((Integer) fields[5])
                .setNumRatings((int) fields[6])
                .setAvgRating((double) fields[7])
                .setDirections((List<String>) fields[8])
                .setTags((Set<Tag>) fields[9])
                .setRequiredIngredients((Map<Ingredient, Double>) fields[10])
                .build();
    }

    // Gets the (userNum)'th preset User
    @SuppressWarnings("unchecked")
    public static User user(int userNum) {
        checkPresetNum(userNum);

        Object[] fields = getFields(userArgs(1, true), userNum);

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
