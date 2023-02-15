/* (C)2023 */
package com.recipecart.entities;

import static com.recipecart.testutil.Presets.*;
import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class IngredientTest {

    static Stream<Arguments> constructorParams() {
        return ingredientArgs(1).get();
    }

    static Stream<Arguments> twoUnequalConstructorParams() {
        return ingredientArgs(2).get();
    }

    @ParameterizedTest
    @MethodSource("constructorParams")
    void testState(@NotNull String p1, @Nullable String p2, @Nullable String p3) {
        Ingredient i1 = new Ingredient(p1, p2, p3);

        assertEquals(i1.getName(), p1);
        assertEquals(i1.toString(), p1);
        assertEquals(i1.getUnits(), p2);
        assertEquals(i1.getImageUri(), p3);
    }

    @ParameterizedTest
    @MethodSource("constructorParams")
    void testEquality(@NotNull String i1p1, @Nullable String i1p2, @Nullable String i1p3) {

        Ingredient i1 = new Ingredient(i1p1, i1p2, i1p3),
                i2 = new Ingredient(newString(i1p1), newString(i1p2), newString(i1p3));

        assertEquals(i1, i2);
        assertEquals(i1.getName(), i2.getName());
        assertEquals(i1.toString(), i2.toString());
        assertEquals(i1.getUnits(), i2.getUnits());
        assertEquals(i1.getImageUri(), i2.getImageUri());
        assertEquals(i1.hashCode(), i2.hashCode());
    }

    @ParameterizedTest
    @MethodSource("twoUnequalConstructorParams")
    void testInequality(
            @NotNull String i1p1,
            @NotNull String i2p1,
            @Nullable String i1p2,
            @Nullable String i2p2,
            @Nullable String i1p3,
            @Nullable String i2p3) {
        Ingredient i1 = new Ingredient(i1p1, i1p2, i1p3), i2 = new Ingredient(i2p1, i2p2, i2p3);

        assertNotEquals(i1.getName(), i2.getName());
        assertNotEquals(i1.toString(), i2.toString());
        assertNotEquals(i1.getUnits(), i2.getUnits());
        assertNotEquals(i1.getImageUri(), i2.getImageUri());
        assertNotEquals(i1, i2);
    }
}
