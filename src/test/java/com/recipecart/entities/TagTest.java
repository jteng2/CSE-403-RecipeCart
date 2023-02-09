/* (C)2023 */
package com.recipecart.entities;

import static com.recipecart.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.TestUtils;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TagTest {
    static Stream<Object> constructorParams() {
        return Stream.of(TestUtils.getStrings());
    }

    static Stream<Arguments> twoEqualConstructorParams() {
        return generateArguments(TestUtils::getStrings, 2, false);
    }

    static Stream<Arguments> twoUnequalConstructorParams() {
        return generateArguments(TestUtils::getStrings, 2, true);
    }

    @ParameterizedTest(name = "Tag({0})'s name is {0}")
    @MethodSource("constructorParams")
    void testState(String s) {
        Tag t1 = new Tag(s);

        assertEquals(t1.getName(), s);
        assertEquals(t1.toString(), s);
    }

    @ParameterizedTest(name = "Tag({0}) equals Tag({1})")
    @MethodSource("twoEqualConstructorParams")
    void testEquality(String s1, String s2) {
        Tag t1 = new Tag(s1), t2 = new Tag(s2);
        assertEquals(t1, t2);
        assertEquals(t1.getName(), t2.getName());
        assertEquals(t1.toString(), t2.toString());
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @ParameterizedTest(name = "Tag({0}) does not equal Tag({1})")
    @MethodSource("twoUnequalConstructorParams")
    void testInequality(String s1, String s2) {
        Tag t1 = new Tag(s1), t2 = new Tag(s2);
        assertNotEquals(t1, t2);
        assertNotEquals(t1.getName(), t2.getName());
        assertNotEquals(t1.toString(), t2.toString());
    }
}
