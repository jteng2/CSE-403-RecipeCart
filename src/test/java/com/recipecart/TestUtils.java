/* (C)2023 */
package com.recipecart;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class TestUtils {
    public static Object[] getStrings() {
        return new String[] {
            "",
            "abc",
            "Hello World",
            """
8*H
\t(#WRU*H\u8123(*\uD83D\uDE33QH#i238rh9
https://google.com
<script>alert("Boo!");</script>""",
            null
        };
    }

    public static Object[] getIntegers() {
        return new Integer[] {0, 1, -16, 12345, null, Integer.MAX_VALUE, Integer.MIN_VALUE};
    }

    public static Object[] getInts() {
        return new Object[] {0, 1, -16, 12345, Integer.MAX_VALUE, Integer.MIN_VALUE};
    }

    // We define a "staircase array" to be a 2D array, where each column is the same as
    // the last column, except each element is shifted down one row (and the bottom element
    // goes back to the top). Staircase arrays are generated when staircase is set to true.
    // Example:
    // [[1, 2, 3],
    //  [2, 3, 4],
    //  [3, 4, 1],
    //  [4, 1, 2]]
    public static Object[][] generateMatrix(
            Supplier<Object[]> columnGenerator, int numColumns, boolean staircase) {
        int numRows = columnGenerator.get().length;

        Object[][] tuples = new Object[numRows][numColumns];

        for (int j = 0; j < numColumns; j++) {
            Object[] elems = columnGenerator.get();
            for (int i = 0; i < numRows; i++) {
                tuples[i][j] = elems[staircase ? (i + j) % numRows : i];
            }
        }

        return tuples;
    }

    // horizontally stack given arrays (assumed to be non-jagged and non-null)
    public static Object[][] horizontalConcatMatrix(List<Object[][]> matrices) {
        if (matrices.size() == 0 || matrices.get(0).length == 0) {
            return new Object[0][0];
        }

        int numRows = matrices.get(0).length;
        int numColumns = 0;
        for (Object[][] o : matrices) {
            if (matrices.get(0).length != o.length) {
                throw new IllegalArgumentException("Arrays don't have equal heights");
            }
            numColumns += o[0].length;
        }

        Object[][] stacked = new Object[numRows][numColumns];
        int columnOffset = 0;
        for (Object[][] o : matrices) {
            for (int i = 0; i < o.length; i++) {
                System.arraycopy(o[i], 0, stacked[i], columnOffset, o[i].length);
            }
            columnOffset += o[0].length;
        }

        return stacked;
    }

    // converts a 2D array to a stream of arguments (each of which takes a row of the matrix)
    public static Stream<Arguments> argsFromMatrix(Object[][] matrix) {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (Object[] row : matrix) {
            builder.add(Arguments.of(row));
        }
        return builder.build();
    }

    public static Stream<Arguments> generateArguments(
            Supplier<Object[]> generator, int columns, boolean staircase) {
        Object[][] matrix = generateMatrix(generator, columns, staircase);
        return argsFromMatrix(matrix);
    }

    public static Stream<Arguments> generateMultiArguments(
            List<Supplier<Object[]>> generators, int[] numColumns, boolean[] staircase) {
        if (numColumns.length != generators.size() || numColumns.length != staircase.length) {
            throw new IllegalArgumentException();
        }

        List<Object[][]> matrices = new ArrayList<>();
        for (int i = 0; i < generators.size(); i++) {
            matrices.add(generateMatrix(generators.get(i), numColumns[i], staircase[i]));
        }

        Object[][] concatMatrix = horizontalConcatMatrix(matrices);
        return argsFromMatrix(concatMatrix);
    }
}
