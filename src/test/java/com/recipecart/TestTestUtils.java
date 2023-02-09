/* (C)2023 */
package com.recipecart;

import static com.recipecart.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.Arguments;

public class TestTestUtils {
    @Test
    void testNonStaircaseMatrix() {
        Supplier<Object[]> generator = () -> new Integer[] {1, 2, 3, 4, 5};
        int columns = 6;

        Object[][] matrix = generateMatrix(generator, columns, false);

        Integer[][] answer =
                new Integer[][] {
                    {1, 1, 1, 1, 1, 1},
                    {2, 2, 2, 2, 2, 2},
                    {3, 3, 3, 3, 3, 3},
                    {4, 4, 4, 4, 4, 4},
                    {5, 5, 5, 5, 5, 5}
                };

        assertArrayEquals(answer, matrix);
    }

    @Test
    void testStaircaseMatrix() {
        Supplier<Object[]> generator = () -> new Integer[] {1, 2, 3, 4, 5};
        int columns = 6;

        Object[][] matrix = generateMatrix(generator, columns, true);

        Integer[][] answer =
                new Integer[][] {
                    {1, 2, 3, 4, 5, 1},
                    {2, 3, 4, 5, 1, 2},
                    {3, 4, 5, 1, 2, 3},
                    {4, 5, 1, 2, 3, 4},
                    {5, 1, 2, 3, 4, 5}
                };

        assertArrayEquals(answer, matrix);
    }

    @Test
    void testHorizontalConcatMatrix() {
        Integer[][]
                left =
                        new Integer[][] {
                            {1, 2, 3},
                            {4, 5, 6},
                            {7, 8, 9}
                        },
                middle = new Integer[][] {{null}, {Integer.MAX_VALUE}, {Integer.MIN_VALUE}},
                right =
                        new Integer[][] {
                            {10, 11, 12, 13, 14},
                            {15, 16, 17, 18, 19},
                            {20, 21, 22, 23, 24}
                        };

        Object[][] combined = horizontalConcatMatrix(List.of(left, middle, right));
        Object[][] answer =
                new Integer[][] {
                    {1, 2, 3, null, 10, 11, 12, 13, 14},
                    {4, 5, 6, Integer.MAX_VALUE, 15, 16, 17, 18, 19},
                    {7, 8, 9, Integer.MIN_VALUE, 20, 21, 22, 23, 24}
                };

        assertArrayEquals(answer, combined);
    }

    @Test
    void testArgsFromMatrix() {
        Object[][] matrix =
                new Object[][] {
                    {null, "ab", 6, 1f, 3.0000000001},
                    {0, "\u1000", 1, -4.5f, new ArrayList<Integer>()},
                    {-0., Double.NaN, Integer.MIN_VALUE, Double.NEGATIVE_INFINITY, 5.4}
                };
        Stream<Arguments> params = argsFromMatrix(matrix);

        Stream<Arguments> answer =
                Stream.of(
                        Arguments.of(null, "ab", 6, 1f, 3.0000000001),
                        Arguments.of(0, "\u1000", 1, -4.5f, new ArrayList<Integer>()),
                        Arguments.of(
                                -0., Double.NaN, Integer.MIN_VALUE, Double.NEGATIVE_INFINITY, 5.4));

        assertArgumentStreamEquals(answer, params);
    }

    @Test
    void testGenerateArguments() {
        Supplier<Object[]> generator = () -> new Integer[] {1, 2, 3, 4, 5};
        Stream<Arguments> args = generateArguments(generator, 7, true);

        Stream<Arguments> answer =
                Stream.of(
                        Arguments.of(1, 2, 3, 4, 5, 1, 2),
                        Arguments.of(2, 3, 4, 5, 1, 2, 3),
                        Arguments.of(3, 4, 5, 1, 2, 3, 4),
                        Arguments.of(4, 5, 1, 2, 3, 4, 5),
                        Arguments.of(5, 1, 2, 3, 4, 5, 1));

        assertArgumentStreamEquals(answer, args);
    }

    static void assertArgumentStreamEquals(Stream<Arguments> s1, Stream<Arguments> s2) {
        Iterator<Arguments> iter1 = s1.iterator(), iter2 = s2.iterator();
        while (iter1.hasNext() && iter2.hasNext()) {
            assertArrayEquals(iter1.next().get(), iter2.next().get());
        }
        assertFalse(iter1.hasNext());
        assertFalse(iter2.hasNext());
    }
}
