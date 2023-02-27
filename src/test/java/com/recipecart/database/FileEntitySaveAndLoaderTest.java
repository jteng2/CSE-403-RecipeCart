/* (C)2023 */
package com.recipecart.database;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.recipecart.entities.*;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.Presets;
import com.recipecart.testutil.TestData;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class FileEntitySaveAndLoaderTest {
    private static final long SEED = 0xdeadbeefL;
    private static final int NUM_TIMES_TO_AUTOSAVE = 3;

    private static void randomSaveOperation(EntitySaver saver, Random rand) {
        int operationIndex = rand.nextInt(2);
        // only add tags or ingredients, since adding recipe requires adding the tags/ingredients
        // it uses for good (de)serialization
        int entityIndex = rand.nextInt(TestData.NUM_PARAM_COMBOS);
        switch (operationIndex) {
            case 0:
                saver.updateTags(Collections.singleton(Presets.tag(entityIndex)));
                break;
            case 1:
                saver.updateIngredients(Collections.singleton(Presets.ingredient(entityIndex)));
                break;
        }
    }

    private static void populateStorage(EntitySaver saver) {
        saver.updateTags(List.of((Tag[]) TestData.getTags()));
        saver.updateIngredients(List.of((Ingredient[]) TestData.getIngredients()));
        saver.updateRecipes(List.of((Recipe[]) TestData.getRecipes()));
        saver.updateUsers(List.of((User[]) TestData.getUsers()));
    }

    private static void assertBytesMatch(
            FileEntitySaveAndLoader expected, byte[] serialized, boolean checkFieldsOnly)
            throws IOException, ClassNotFoundException {
        FileEntitySaveAndLoader saveAndLoader = new FileEntitySaveAndLoader();
        ByteArrayInputStream inStream = new ByteArrayInputStream(serialized);
        saveAndLoader.load(inStream);

        if (checkFieldsOnly) {
            assertEquals(expected.getSavedTags(), saveAndLoader.getSavedTags());
            assertEquals(expected.getSavedIngredients(), saveAndLoader.getSavedIngredients());
            assertEquals(expected.getSavedRecipes(), saveAndLoader.getSavedRecipes());
            assertEquals(expected.getSavedUsers(), saveAndLoader.getSavedUsers());
        } else {
            assertEquals(expected, saveAndLoader);
        }
    }

    @Test
    void testSaveAndLoad() throws IOException, ClassNotFoundException {
        FileEntitySaveAndLoader expectedSaveAndLoader = new FileEntitySaveAndLoader();
        populateStorage(expectedSaveAndLoader);

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        expectedSaveAndLoader.save(outStream);
        byte[] serialized = outStream.toByteArray();

        assertBytesMatch(expectedSaveAndLoader, serialized, false);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10, 20})
    void testAutosave(int maxSaveCounter) throws IOException, ClassNotFoundException {
        final long seed = 0xdeadbeefL;
        Random rand = new Random(seed);

        BytesSave streamGetter = new BytesSave();
        FileEntitySaveAndLoader saveAndLoader =
                new FileEntitySaveAndLoader("", maxSaveCounter) {
                    @Override
                    public void save(@NotNull String filename) throws IOException {
                        save(streamGetter.getStream());
                    }
                };

        for (int i = 0; i < NUM_TIMES_TO_AUTOSAVE; i++) {
            for (int j = 0; j < maxSaveCounter; j++) {
                assertEquals(0, streamGetter.getNumTimesGetStreamCalled());
                randomSaveOperation(saveAndLoader, rand);
            }

            assertEquals(1, streamGetter.getNumTimesGetStreamCalled());
            byte[] serialized = streamGetter.getStreamForTester().toByteArray();
            assertBytesMatch(saveAndLoader, serialized, true);

            streamGetter.resetStream();
        }
    }

    private static class BytesSave {
        private int numTimesCalled = 0;
        private ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        public void resetStream() {
            numTimesCalled = 0;
            outStream = new ByteArrayOutputStream();
        }

        // used by FileEntitySaveAndLoader
        public ByteArrayOutputStream getStream() {
            numTimesCalled++;
            return outStream;
        }

        // used by test code
        public ByteArrayOutputStream getStreamForTester() {
            return outStream;
        }

        public int getNumTimesGetStreamCalled() {
            return numTimesCalled;
        }
    }
}
