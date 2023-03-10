/* (C)2023 */
package com.recipecart.testutil;

import static com.recipecart.testutil.TestData.NUM_PARAM_COMBOS;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import java.util.stream.Stream;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class PresetsTest {
    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testTagPresets(int index) {
        Tag preset = Presets.tag(index);
        assertNotNull(preset);
        assertNotNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testIngredientPresets(int index) {
        Ingredient preset = Presets.ingredient(index);
        assertNotNull(preset);
        assertNotNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testRecipePresets(int index) {
        Recipe preset = Presets.recipe(index);
        assertNotNull(preset);
        assertNotNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testUserPresets(int index) {
        User preset = Presets.user(index);
        assertNotNull(preset);
        assertNotNull(preset.getUsername());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testInvalidTagPresets(int index) {
        Tag preset = Presets.invalidTag(index);
        assertNotNull(preset);
        assertNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testInvalidIngredientPresets(int index) {
        Ingredient preset = Presets.invalidIngredient(index);
        assertNotNull(preset);
        assertNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testInvalidRecipePresets(int index) {
        Recipe preset = Presets.invalidRecipe(index);
        assertNotNull(preset);
        assertNull(preset.getName());
    }

    @ParameterizedTest
    @MethodSource("possibleIndexes")
    void testInvalidUserPresets(int index) {
        User preset = Presets.invalidUser(index);
        assertNotNull(preset);
        assertNull(preset.getUsername());
    }

    static Stream<Integer> possibleIndexes() {
        Stream.Builder<Integer> builder = Stream.builder();
        for (int i = 0; i < NUM_PARAM_COMBOS; i++) {
            builder.add(i);
        }
        return builder.build();
    }
}
