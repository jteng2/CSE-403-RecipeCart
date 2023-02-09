/* (C)2023 */
package com.recipecart.entities;

import static com.recipecart.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.TestUtils;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class IngredientTest {

    static Stream<Arguments> constructorParams() {
        return generateArguments(TestUtils::getStrings, 3, true);
    }

    static Stream<Arguments> twoConstructorParams() {
        return generateMultiArguments(
                List.of(TestUtils::getStrings, TestUtils::getStrings),
                new int[] {3, 3},
                new boolean[] {true, true});
    }

    @ParameterizedTest
    @MethodSource("constructorParams")
    void testState(String p1, String p2, String p3) {
        Ingredient i1 = new Ingredient(p1, p2, p3);

        assertEquals(i1.getName(), p1);
        assertEquals(i1.toString(), p1);
        assertEquals(i1.getUnits(), p2);
        assertEquals(i1.getImageUri(), p3);
    }

    @ParameterizedTest
    @MethodSource("twoConstructorParams")
    void testEquality(
            String i1p1, String i1p2, String i1p3, String i2p1, String i2p2, String i2p3) {
        Ingredient i1 = new Ingredient(i1p1, i1p2, i1p3), i2 = new Ingredient(i2p1, i2p2, i2p3);

        assertEquals(i1.getName(), i2.getName());
        assertEquals(i1.toString(), i2.toString());
        assertEquals(i1.getUnits(), i2.getUnits());
        assertEquals(i1.getImageUri(), i2.getImageUri());
        assertEquals(i1.hashCode(), i2.hashCode());
        assertEquals(i1, i2);
    }

    @ParameterizedTest
    @MethodSource("twoConstructorParams")
    void testInequality(
            String i1p1, String i2p1, String i1p2, String i2p2, String i1p3, String i2p3) {
        Ingredient i1 = new Ingredient(i1p1, i1p2, i1p3), i2 = new Ingredient(i2p1, i2p2, i2p3);

        assertNotEquals(i1.getName(), i2.getName());
        assertNotEquals(i1.toString(), i2.toString());
        assertNotEquals(i1.getUnits(), i2.getUnits());
        assertNotEquals(i1.getImageUri(), i2.getImageUri());
        assertNotEquals(i1, i2);
    }
}
