/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class CreateIngredientCommandTest extends SimpleCreateEntityCommandTest<Ingredient> {
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
    protected SimpleCreateEntityCommand<Ingredient> getCreateEntityCommand(Ingredient entity) {
        return new CreateIngredientCommand(entity);
    }

    @Override
    protected Ingredient renameToNullEntity(Ingredient entity) {
        return new Ingredient(null, entity.getUnits(), entity.getImageUri());
    }

    @Override
    protected void addEntitiesToStorage(Collection<Ingredient> entity, EntitySaver saver) {
        saver.updateIngredients(entity);
    }
}
