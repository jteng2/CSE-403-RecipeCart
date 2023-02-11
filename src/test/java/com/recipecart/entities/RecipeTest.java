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

public class RecipeTest {
    static Stream<Arguments> builderParams() {
        return Presets.recipeArgs(1).get();
    }

    static Stream<Arguments> builderParamsDataStructuresOnly() {
        return generateMultiArguments(
                List.of(
                        TestData::getListStringNoNulls,
                        TestData::getSetTagNoNulls,
                        TestData::getMapIngredientDoubleNoNulls),
                1,
                true);
    }

    static Stream<Arguments> recipeParams() {
        return generateArguments(TestData::getRecipes);
    }

    static Stream<Arguments> unequalBuilderParams() {
        return Presets.recipeArgs(2).get();
    }

    @Test
    void testDefaultBuilder() {
        Recipe recipe = new Recipe.Builder().build();

        assertNotNull(recipe);
        assertNull(recipe.getName());
        assertNull(recipe.getPresentationName());
        assertNull(recipe.getPrepTime());
        assertNull(recipe.getCookTime());
        assertNull(recipe.getImageUri());
        assertNull(recipe.getNumServings());
        assertEquals(0.0, recipe.getAvgRating());
        assertEquals(0, recipe.getNumRatings());
        assertNotNull(recipe.getDirections());
        assertEquals(0, recipe.getDirections().size());
        assertNotNull(recipe.getTags());
        assertEquals(0, recipe.getTags().size());
        assertNotNull(recipe.getRequiredIngredients());
        assertEquals(0, recipe.getRequiredIngredients().size());
    }

    @ParameterizedTest
    @MethodSource("builderParams")
    void testBuilder(
            @NotNull String s1,
            String s2,
            String s3,
            Integer i1,
            Integer i2,
            Integer i3,
            int pi1,
            double pd1,
            @NotNull List<@NotNull String> ls1,
            @NotNull Set<@NotNull Tag> st1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> mid1) {
        Recipe recipe =
                new Recipe.Builder()
                        .setName(s1)
                        .setPresentationName(s2)
                        .setImageUri(s3)
                        .setPrepTime(i1)
                        .setCookTime(i2)
                        .setNumServings(i3)
                        .setNumRatings(pi1)
                        .setAvgRating(pd1)
                        .setDirections(ls1)
                        .setTags(st1)
                        .setRequiredIngredients(mid1)
                        .build();

        assertNotNull(recipe);
        assertEquals(s1, recipe.getName());
        assertEquals(s2, recipe.getPresentationName());
        assertEquals(s3, recipe.getImageUri());
        assertEquals(i1, recipe.getPrepTime());
        assertEquals(i2, recipe.getCookTime());
        assertEquals(i3, recipe.getNumServings());
        assertEquals(pi1, recipe.getNumRatings());
        assertEquals(pd1, recipe.getAvgRating());
        assertEquals(ls1, recipe.getDirections());
        assertEquals(st1, recipe.getTags());
        assertEquals(mid1, recipe.getRequiredIngredients());
    }

    @ParameterizedTest
    @MethodSource("recipeParams")
    void testBuilderRecipeCopier(Recipe recipe1) {
        Recipe recipe2 = new Recipe.Builder(recipe1).build();

        assertRecipesEquals(recipe1, recipe2);
    }

    private static void assertRecipesEquals(Recipe recipe1, Recipe recipe2) {
        assertEquals(recipe1, recipe2);
        assertEquals(recipe1.getName(), recipe2.getName());
        assertEquals(recipe1.getPresentationName(), recipe2.getPresentationName());
        assertEquals(recipe1.getImageUri(), recipe2.getImageUri());
        assertEquals(recipe1.getPrepTime(), recipe2.getPrepTime());
        assertEquals(recipe1.getCookTime(), recipe2.getCookTime());
        assertEquals(recipe1.getNumServings(), recipe2.getNumServings());
        assertEquals(recipe1.getNumRatings(), recipe2.getNumRatings());
        assertEquals(recipe1.getAvgRating(), recipe2.getAvgRating());
        assertEquals(recipe1.getDirections(), recipe2.getDirections());
        assertEquals(recipe1.getTags(), recipe2.getTags());
        assertEquals(recipe1.getRequiredIngredients(), recipe2.getRequiredIngredients());
        assertEquals(recipe1.hashCode(), recipe2.hashCode());
    }

    @ParameterizedTest
    @MethodSource("builderParamsDataStructuresOnly")
    void testImmutability(
            List<String> directionsOriginal,
            Set<Tag> tagsOriginal,
            Map<Ingredient, Double> requiredOriginal) {
        // this test relies on Tag and Ingredient being immutable;
        // if this ever changes, then this test becomes invalid

        List<String> directions = new ArrayList<>(directionsOriginal);
        Set<Tag> tags = new HashSet<>(tagsOriginal);
        Map<Ingredient, Double> required = new HashMap<>(requiredOriginal);

        Recipe recipe =
                new Recipe.Builder()
                        .setDirections(directions)
                        .setTags(tags)
                        .setRequiredIngredients(required)
                        .build();

        assertEquals(directionsOriginal, recipe.getDirections());
        assertEquals(tagsOriginal, recipe.getTags());
        assertEquals(requiredOriginal, recipe.getRequiredIngredients());

        directions.add("4");
        tags.clear();
        required.put(Presets.ingredient(0), 2.0);

        List<String> recipeDirections = recipe.getDirections();
        assertEquals(directionsOriginal, recipeDirections);
        Set<Tag> recipeTags = recipe.getTags();
        assertEquals(tagsOriginal, recipeTags);
        Map<Ingredient, Double> recipeRequired = recipe.getRequiredIngredients();
        assertEquals(requiredOriginal, recipeRequired);

        assertThrows(UnsupportedOperationException.class, recipeDirections::clear);
        assertThrows(UnsupportedOperationException.class, recipeTags::clear);
        assertThrows(UnsupportedOperationException.class, recipeRequired::clear);
    }

    @ParameterizedTest
    @MethodSource("builderParams")
    void testEquality(
            @NotNull String s1,
            String s2,
            String s3,
            Integer i1,
            Integer i2,
            Integer i3,
            int pi1,
            double pd1,
            @NotNull List<@NotNull String> ls1,
            @NotNull Set<@NotNull Tag> st1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> mid1) {
        Recipe
                recipe1 =
                        new Recipe.Builder()
                                .setName(s1)
                                .setPresentationName(s2)
                                .setImageUri(s3)
                                .setPrepTime(i1)
                                .setCookTime(i2)
                                .setNumServings(i3)
                                .setNumRatings(pi1)
                                .setAvgRating(pd1)
                                .setDirections(ls1)
                                .setTags(st1)
                                .setRequiredIngredients(mid1)
                                .build(),
                recipe2 =
                        new Recipe.Builder()
                                .setName(newString(s1))
                                .setPresentationName(newString(s2))
                                .setImageUri(newString(s3))
                                .setPrepTime(i1)
                                .setCookTime(i2)
                                .setNumServings(i3)
                                .setNumRatings(pi1)
                                .setAvgRating(pd1)
                                .setDirections(new ArrayList<>(ls1))
                                .setTags(new HashSet<>(st1))
                                .setRequiredIngredients(new HashMap<>(mid1))
                                .build();

        assertRecipesEquals(recipe1, recipe2);
    }

    @ParameterizedTest
    @MethodSource("unequalBuilderParams")
    void testInequality(
            @NotNull String r1s1,
            @NotNull String r2s1,
            String r1s2,
            String r2s2,
            String r1s3,
            String r2s3,
            Integer r1i1,
            Integer r2i1,
            Integer r1i2,
            Integer r2i2,
            Integer r1i3,
            Integer r2i3,
            int r1pi1,
            int r2pi1,
            double r1pd1,
            double r2pd1,
            @NotNull List<@NotNull String> r1ls1,
            @NotNull List<@NotNull String> r2ls1,
            @NotNull Set<@NotNull Tag> r1st1,
            @NotNull Set<@NotNull Tag> r2st1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> r1mid1,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> r2mid1) {
        Recipe
                recipe1 =
                        new Recipe.Builder()
                                .setName(r1s1)
                                .setPresentationName(r1s2)
                                .setImageUri(r1s3)
                                .setPrepTime(r1i1)
                                .setCookTime(r1i2)
                                .setNumServings(r1i3)
                                .setNumRatings(r1pi1)
                                .setAvgRating(r1pd1)
                                .setDirections(r1ls1)
                                .setTags(r1st1)
                                .setRequiredIngredients(r1mid1)
                                .build(),
                recipe2 =
                        new Recipe.Builder()
                                .setName(newString(r2s1))
                                .setPresentationName(newString(r2s2))
                                .setImageUri(newString(r2s3))
                                .setPrepTime(r2i1)
                                .setCookTime(r2i2)
                                .setNumServings(r2i3)
                                .setNumRatings(r2pi1)
                                .setAvgRating(r2pd1)
                                .setDirections(r2ls1)
                                .setTags(r2st1)
                                .setRequiredIngredients(r2mid1)
                                .build();

        assertNotEquals(recipe1, recipe2);
        assertNotEquals(recipe1.getName(), recipe2.getName());
        assertNotEquals(recipe1.getPresentationName(), recipe2.getPresentationName());
        assertNotEquals(recipe1.getImageUri(), recipe2.getImageUri());
        assertNotEquals(recipe1.getPrepTime(), recipe2.getPrepTime());
        assertNotEquals(recipe1.getCookTime(), recipe2.getCookTime());
        assertNotEquals(recipe1.getNumServings(), recipe2.getNumServings());
        assertNotEquals(recipe1.getNumRatings(), recipe2.getNumRatings());
        assertNotEquals(recipe1.getAvgRating(), recipe2.getAvgRating());
        assertNotEquals(recipe1.getDirections(), recipe2.getDirections());
        assertNotEquals(recipe1.getTags(), recipe2.getTags());
        assertNotEquals(recipe1.getRequiredIngredients(), recipe2.getRequiredIngredients());
    }
}
