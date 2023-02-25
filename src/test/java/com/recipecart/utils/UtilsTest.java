/* (C)2023 */
package com.recipecart.utils;

import static com.recipecart.utils.Utils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class UtilsTest {
    private static Stream<Arguments> getMapSum() {
        return Stream.of(
                Arguments.of(Map.of("a", 1.0), Map.of("b", 2.0), Map.of("a", 1.0, "b", 2.0)),
                Arguments.of(
                        Map.of("a", 1.0, "b", 2.0),
                        Map.of("b", 3.0, "c", 4.0),
                        Map.of("a", 1.0, "b", 5.0, "c", 4.0)),
                Arguments.of(
                        Map.of("1", 10.0, "2", 20.0),
                        Map.of("1", 30.0, "2", 40.0),
                        Map.of("1", 40.0, "2", 60.0)));
    }

    @ParameterizedTest
    @MethodSource("getMapSum")
    void testAddMaps(
            Map<Object, Double> map1, Map<Object, Double> map2, Map<Object, Double> expected) {
        assertEquals(expected, addMaps(map1, map2));
    }
}
