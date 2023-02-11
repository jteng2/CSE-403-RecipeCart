/* (C)2023 */
package com.recipecart.testutil;

import static com.recipecart.testutil.TestData.NUM_PARAM_COMBOS;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class TestDataTest {
    // all methods in TestData
    static Stream<Supplier<Object[]>> getGenerators() {
        return Stream.of(
                TestData::getStrings,
                TestData::getNotNullStrings,
                TestData::getIntegers,
                TestData::getInts,
                TestData::getPrimitiveDoubles,
                TestData::getTags,
                TestData::getIngredients,
                TestData::getRecipes,
                TestData::getUsers,
                TestData::getListStringNoNulls,
                TestData::getListStringWithNulls,
                TestData::getSetTagNoNulls,
                TestData::getSetTagWithNulls,
                TestData::getMapIngredientDoubleNoNulls,
                TestData::getMapIngredientDoubleWithNulls,
                TestData::getListRecipeNoNulls,
                TestData::getListRecipeWithNulls,
                TestData::getMapRecipeDoubleNoNulls,
                TestData::getMapRecipeDoubleWithNulls,
                TestData::getSetIngredientNoNulls,
                TestData::getSetIngredientWithNulls,
                TestData::getListTagNoNulls,
                TestData::getListTagWithNulls,
                TestData::getListIngredientNoNulls,
                TestData::getListIngredientWithNulls,
                TestData::getListUserNoNulls,
                TestData::getListUserWithNulls);
    }

    // all methods in TestData whose return-value elements are always non-null
    static Stream<Supplier<Object[]>> getNotNullGenerators() {
        return Stream.of(
                TestData::getNotNullStrings,
                TestData::getInts,
                TestData::getPrimitiveDoubles,
                TestData::getTags,
                TestData::getIngredients,
                TestData::getRecipes,
                TestData::getUsers,
                TestData::getListStringNoNulls,
                TestData::getSetTagNoNulls,
                TestData::getMapIngredientDoubleNoNulls,
                TestData::getListRecipeNoNulls,
                TestData::getMapIngredientDoubleNoNulls,
                TestData::getListRecipeNoNulls,
                TestData::getMapRecipeDoubleNoNulls,
                TestData::getSetIngredientNoNulls,
                TestData::getListTagNoNulls,
                TestData::getListIngredientNoNulls,
                TestData::getListUserNoNulls);
    }

    @ParameterizedTest
    @MethodSource("getGenerators")
    void testGenerator(Supplier<Object[]> generator) {
        Object[] generated = generator.get();

        assertNotNull(generated);
        assertEquals(NUM_PARAM_COMBOS, generated.length);

        // no 2 elems are equal
        Set<Object> uniqueElems = new HashSet<>(Arrays.asList(generated));
        assertEquals(generated.length, uniqueElems.size());
    }

    @ParameterizedTest
    @MethodSource("getNotNullGenerators")
    void testNotNullGenerators(Supplier<Object[]> generator) {
        Object[] generated = generator.get();

        for (Object o : generated) {
            assertNotNull(o);
        }
    }
}
