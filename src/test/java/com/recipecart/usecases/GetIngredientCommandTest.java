/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GetIngredientCommandTest extends SimpleGetCommandTest<Ingredient> {
    @Override
    protected void addEntityToStorage(Ingredient entity, EntitySaver saver) {
        saver.updateIngredients(Collections.singletonList(entity));
    }

    @Override
    protected SimpleGetCommand<Ingredient> getGetEntityCommand(String name) {
        return new GetIngredientCommand(name);
    }

    @Override
    protected Stream<Arguments> getEntity() {
        return TestUtils.generateArguments(TestData::getIngredients);
    }

    @Override
    protected Stream<Arguments> getStorageWithEntity() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(),
                Collections.singletonList(TestData::getIngredients));
    }

    @Override
    protected String getName(Ingredient entity) {
        return entity.getName();
    }
}
