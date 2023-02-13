/* (C)2023 */
package com.recipecart.testutil;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import com.recipecart.entities.*;
import com.recipecart.storage.EntityStorage;
import com.recipecart.utils.TwoTuple;
import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.provider.Arguments;

/** This class contains various general utility methods that assist in unit testing. */
public class TestUtils {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// General helper methods //////////////////////////////////////////

    // We want to make a new String reference instead of using the same reference here;
    // there would be a similar method for Integer also, but `new Integer()` is deprecated
    public static @Nullable String newString(String toCopy) {
        return toCopy == null ? null : new String(toCopy);
    }

    private static int[] arrayFromInt(int elem, int length) {
        int[] arr = new int[length];
        Arrays.fill(arr, elem);
        return arr;
    }

    // for all uses of this method, each Object in things is of type T
    @SuppressWarnings("unchecked")
    public static <T> List<T> convertToTypedList(Object[] things) {
        List<T> list = new ArrayList<>();
        for (Object o : things) {
            list.add((T) o);
        }
        return list;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////// Methods that deal with "matrices" (2D arrays) of Objects ///////////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Methods that deal with generating Streams of Arguments, to be fed into Parameterized Tests //

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////// Methods that behave like List.of/Set.of/Map.of, but allow nulls ///////////////////

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Methods for renaming entities ///////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Methods for renaming entities ///////////////////////////////////

    @SuppressWarnings("unused") // currently, newName is the only field of tag
    public static Tag renameTag(@NotNull Tag tag, String newName) {
        return new Tag(newName);
    }

    public static Ingredient renameIngredient(Ingredient ingredient, String toRename) {
        return new Ingredient(toRename, ingredient.getUnits(), ingredient.getImageUri());
    }

    public static Recipe renameRecipe(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setName(toRename).build();
    }

    public static Recipe renameRecipePresentationName(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setPresentationName(toRename).build();
    }

    public static Recipe renameRecipeFull(
            Recipe recipe, String toRename, String toRenamePresentation) {
        return new Recipe.Builder(recipe)
                .setName(toRename)
                .setPresentationName(toRenamePresentation)
                .build();
    }

    public static User renameUser(User User, String toRename) {
        return new User.Builder(User).setUsername(toRename).build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////// Methods for generating searching-test params //////////////////////////////

    public static List<String> getEntityNames() {
        return List.of("Chicken Adobo", "hella veggies", "null", "Mangoes", "Deleted User");
    }

    public static List<String> getPresentationNames() {
        return List.of(
                "Tasty Chicken Adobo with Rice",
                "Healthy Hearty Veggie Dish",
                "Address zero",
                "My favorite fruit",
                "");
    }

    public static List<Set<String>> getTokenSets() {
        return List.of(
                Set.of("Healthy", "Chicken"),
                Set.of("veggies", "fruit"),
                Set.of("adobo", "Null", "Mangoes", "user", "withrice", "Tasty", "address"),
                Set.of(),
                Set.of("Mangos, hellaveggies", "deleted", "zero"));
    }

    public static List<Set<String>> getExpectedEntityNameSets() {
        return List.of(
                Set.of("Chicken Adobo"),
                Set.of("hella veggies"),
                Set.of("Chicken Adobo", "null", "Mangoes", "Deleted User"),
                Set.of(),
                Set.of("Deleted User"));
    }

    public static List<Set<String>> getExpectedRecipeNameSets() {
        return List.of(
                Set.of("Chicken Adobo", "hella veggies"),
                Set.of("hella veggies", "Mangoes"),
                Set.of("Chicken Adobo", "null", "Mangoes", "Deleted User"),
                Set.of(),
                Set.of("Deleted User", "null"));
    }

    public static <T> Stream<Arguments> getSearchTestParams(
            List<T> entitiesOriginal,
            List<List<String>> newEntityNameLists,
            List<Set<String>> tokenSets,
            List<Set<String>> expectedEntityNameSets,
            List<Function<TwoTuple<T, String>, T>> entityRenamers,
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        // check if data structure sizes are consistent
        assertNotEquals(0, newEntityNameLists.size());
        assertEquals(newEntityNameLists.size(), entityRenamers.size());
        for (List<String> newEntityNames : newEntityNameLists) {
            assertEquals(entitiesOriginal.size(), newEntityNames.size());
        }
        assertEquals(tokenSets.size(), expectedEntityNameSets.size());

        // initialize sets
        List<Set<T>> expectedEntitySets = new ArrayList<>();
        for (int i = 0; i < expectedEntityNameSets.size(); i++) {
            expectedEntitySets.add(new HashSet<>());
        }

        List<T> entities = new ArrayList<>();
        for (int i = 0; i < entitiesOriginal.size(); i++) {
            // get entities from their names, with chain of renaming functions applied to them
            T newEntity = entitiesOriginal.get(i);
            for (int j = 0; j < newEntityNameLists.size(); j++) {
                String rename = newEntityNameLists.get(j).get(i);
                newEntity = entityRenamers.get(j).apply(new TwoTuple<>(newEntity, rename));
            }
            entities.add(newEntity);

            // get expected entities from their names
            // (element 0 of newEntityNameLists reserved for the "unique" names of the entities,
            // which are used for identifying entities)
            for (int j = 0; j < expectedEntitySets.size(); j++) {
                String newName = newEntityNameLists.get(0).get(i);
                if (expectedEntityNameSets.get(j).contains(newName)) {
                    expectedEntitySets.get(j).add(newEntity);
                }
            }
        }

        // turn these entities, etc. into arguments
        Stream.Builder<Arguments> paramsListsBuilder = Stream.builder();
        for (Supplier<EntityStorage> storageGenerator : entityStorageGenerators) {
            for (int i = 0; i < expectedEntitySets.size(); i++) {
                paramsListsBuilder.add(
                        Arguments.of(
                                storageGenerator.get(),
                                entities,
                                tokenSets.get(i),
                                expectedEntitySets.get(i)));
            }
        }
        return paramsListsBuilder.build();
    }

    public static <T> Stream<Arguments> getSearchTestParams(
            List<T> entitiesOriginal,
            List<String> newEntityNames,
            List<Set<String>> tokenSets,
            List<Set<String>> expectedEntityNameSets,
            Function<TwoTuple<T, String>, T> renameEntity,
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        return getSearchTestParams(
                entitiesOriginal,
                Collections.singletonList(newEntityNames),
                tokenSets,
                expectedEntityNameSets,
                Collections.singletonList(renameEntity),
                entityStorageGenerators);
    }

    public static Stream<Arguments> getSearchTags(
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getTags()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (TwoTuple<Tag, String> tagAndString) ->
                        renameTag(tagAndString.getFirst(), tagAndString.getSecond()),
                entityStorageGenerators);
    }

    public static Stream<Arguments> getSearchIngredients(
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getIngredients()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (TwoTuple<Ingredient, String> ingredientAndString) ->
                        renameIngredient(
                                ingredientAndString.getFirst(), ingredientAndString.getSecond()),
                entityStorageGenerators);
    }

    public static Stream<Arguments> getSearchRecipes(
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        Function<TwoTuple<Recipe, String>, Recipe> recipeRenamer =
                (TwoTuple<Recipe, String> recipeAndString) ->
                        renameRecipe(recipeAndString.getFirst(), recipeAndString.getSecond());
        Function<TwoTuple<Recipe, String>, Recipe> recipePresentationRenamer =
                (TwoTuple<Recipe, String> recipeAndString) ->
                        renameRecipePresentationName(
                                recipeAndString.getFirst(), recipeAndString.getSecond());

        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getRecipes()),
                List.of(getEntityNames(), getPresentationNames()),
                getTokenSets(),
                getExpectedRecipeNameSets(),
                List.of(recipeRenamer, recipePresentationRenamer),
                entityStorageGenerators);
    }

    public static Stream<Arguments> getSearchUsers(
            List<Supplier<EntityStorage>> entityStorageGenerators) {
        return getSearchTestParams(
                TestUtils.convertToTypedList(TestData.getUsers()),
                getEntityNames(),
                getTokenSets(),
                getExpectedEntityNameSets(),
                (TwoTuple<User, String> userAndString) ->
                        renameUser(userAndString.getFirst(), userAndString.getSecond()),
                entityStorageGenerators);
    }
}
