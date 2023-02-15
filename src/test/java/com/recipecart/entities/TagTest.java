/* (C)2023 */
package com.recipecart.entities;

import static com.recipecart.testutil.Presets.tagArgs;
import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class TagTest {
    static Stream<Arguments> constructorParams() {
        return tagArgs(1).get();
    }

    static Stream<Arguments> twoUnequalConstructorParams() {
        return tagArgs(2).get();
    }

    @ParameterizedTest(name = "Tag({0})'s name is {0}")
    @MethodSource("constructorParams")
    void testState(@NotNull String s) {
        Tag t1 = new Tag(s);

        assertEquals(t1.getName(), s);
        assertEquals(t1.toString(), s);
    }

    @ParameterizedTest(name = "Tag({0}) equals Tag({0})")
    @MethodSource("constructorParams")
    void testEquality(@NotNull String s1) {
        Tag t1 = new Tag(s1), t2 = new Tag(newString(s1));

        assertEquals(t1, t2);
        assertEquals(t1.getName(), t2.getName());
        assertEquals(t1.toString(), t2.toString());
        assertEquals(t1.hashCode(), t2.hashCode());
    }

    @ParameterizedTest(name = "Tag({0}) does not equal Tag({1})")
    @MethodSource("twoUnequalConstructorParams")
    void testInequality(@NotNull String s1, @Nullable String s2) {
        Tag t1 = new Tag(s1), t2 = new Tag(s2);

        assertNotEquals(t1, t2);
        assertNotEquals(t1.getName(), t2.getName());
        assertNotEquals(t1.toString(), t2.toString());
    }
}
