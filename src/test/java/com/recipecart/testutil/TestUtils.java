/* (C)2023 */
package com.recipecart.testutil;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.provider.Arguments;

/** This class contains various general utility methods that assist in unit testing. */
public class TestUtils {

    // We want to make a new String reference instead of using the same reference here;
    // there would be a similar method for Integer also, but `new Integer()` is deprecated
    public static @Nullable String newString(String toCopy) {
        return toCopy == null ? null : new String(toCopy);
    }

    // Generates a 2D array where each column corresponds to elements of arrays generated
    // by the given array generator.
    //
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

    // Horizontally stacks the given matrices (assumed to be non-jagged and non-null)
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

    // Convert rows of the given matrix to Arguments objects
    public static Stream<Arguments> argsFromMatrix(Object[][] matrix) {
        Stream.Builder<Arguments> builder = Stream.builder();
        for (Object[] row : matrix) {
            builder.add(Arguments.of(row));
        }
        return builder.build();
    }

    // Combines generateMatrix with argsFromMatrix
    public static Stream<Arguments> generateArguments(
            Supplier<Object[]> generator, int columns, boolean staircase) {
        Object[][] matrix = generateMatrix(generator, columns, staircase);
        return argsFromMatrix(matrix);
    }

    public static Stream<Arguments> generateArguments(Supplier<Object[]> generator, int columns) {
        return generateArguments(generator, columns, true);
    }

    public static Stream<Arguments> generateArguments(Supplier<Object[]> generator) {
        return generateArguments(generator, 1);
    }

    // Generates a 2D matrix, created from horizontally concatenating 2D matrices created from
    // generateMatrix, using respective elements of each parameter for each matrix
    private static Object[][] matrixFromGenerators(
            List<Supplier<Object[]>> generators, int[] numColumns, boolean[] staircase) {
        if (numColumns.length != generators.size() || numColumns.length != staircase.length) {
            throw new IllegalArgumentException();
        }

        List<Object[][]> matrices = new ArrayList<>();
        for (int i = 0; i < generators.size(); i++) {
            matrices.add(generateMatrix(generators.get(i), numColumns[i], staircase[i]));
        }

        return horizontalConcatMatrix(matrices);
    }

    // Combines matrixFromGenerators with argsFromMatrix
    public static Stream<Arguments> generateMultiArguments(
            List<Supplier<Object[]>> generators, int[] numColumns, boolean[] staircase) {
        Object[][] concatMatrix = matrixFromGenerators(generators, numColumns, staircase);
        return argsFromMatrix(concatMatrix);
    }

    // Either staircases or doesn't "staircase" all the arguments
    public static Stream<Arguments> generateMultiArguments(
            List<Supplier<Object[]>> generators, int[] numColumns, boolean staircaseEach) {
        boolean[] staircaseArray = new boolean[generators.size()];
        Arrays.fill(staircaseArray, staircaseEach);

        return generateMultiArguments(generators, numColumns, staircaseArray);
    }

    private static int[] arrayFromInt(int elem, int length) {
        int[] arr = new int[length];
        Arrays.fill(arr, elem);
        return arr;
    }

    // Number of columns will be the same for each matrix
    public static Stream<Arguments> generateMultiArguments(
            List<Supplier<Object[]>> generators, int numColumnsEach, boolean[] staircase) {
        return generateMultiArguments(
                generators, arrayFromInt(numColumnsEach, generators.size()), staircase);
    }

    public static Stream<Arguments> generateMultiArguments(
            List<Supplier<Object[]>> generators, int numColumnsEach, boolean staircaseEach) {
        return generateMultiArguments(
                generators, arrayFromInt(numColumnsEach, generators.size()), staircaseEach);
    }

    // Like List.of, but can take nulls
    @SafeVarargs
    public static <T> List<@Nullable T> listOfAllowNulls(@Nullable T... elems) {
        List<T> list = new ArrayList<>(elems.length);
        for (T elem : elems) {
            list.add(elem); // copy manually, since Arrays.asList and List.of cannot take nulls
        }
        return Collections.unmodifiableList(list);
    }

    // Like Set.of, but can take nulls
    @SafeVarargs
    public static <T> Set<@Nullable T> setOfAllowNulls(@Nullable T... elems) {
        Set<T> set = new HashSet<>(elems.length);
        for (T elem : elems) {
            set.add(elem); // copy manually, since Arrays.asList and List.of cannot take nulls
        }
        return Collections.unmodifiableSet(set);
    }

    // Like Map.of, but can take nulls
    public static <K, V> Map<@Nullable K, @Nullable V> mapOfAllowNulls(
            @Nullable K k1, @Nullable V v1) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1); // copy manually, since Arrays.asList and List.of cannot take nulls
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<@Nullable K, @Nullable V> mapOfAllowNulls(
            @Nullable K k1, @Nullable V v1, @Nullable K k2, @Nullable V v2) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<@Nullable K, @Nullable V> mapOfAllowNulls(
            @Nullable K k1,
            @Nullable V v1,
            @Nullable K k2,
            @Nullable V v2,
            @Nullable K k3,
            @Nullable V v3) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<@Nullable K, @Nullable V> mapOfAllowNulls(
            @Nullable K k1,
            @Nullable V v1,
            @Nullable K k2,
            @Nullable V v2,
            @Nullable K k3,
            @Nullable V v3,
            @Nullable K k4,
            @Nullable V v4) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        return Collections.unmodifiableMap(map);
    }

    public static <K, V> Map<@Nullable K, @Nullable V> mapOfAllowNulls(
            @Nullable K k1,
            @Nullable V v1,
            @Nullable K k2,
            @Nullable V v2,
            @Nullable K k3,
            @Nullable V v3,
            @Nullable K k4,
            @Nullable V v4,
            @Nullable K k5,
            @Nullable V v5) {
        Map<K, V> map = new HashMap<>();
        map.put(k1, v1);
        map.put(k2, v2);
        map.put(k3, v3);
        map.put(k4, v4);
        map.put(k5, v5);
        return Collections.unmodifiableMap(map);
    }
}
