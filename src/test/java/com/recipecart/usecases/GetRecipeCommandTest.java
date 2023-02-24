/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GetRecipeCommandTest extends SimpleGetCommandTest<Recipe> {
    @Override
    protected void addEntityToStorage(Recipe entity, EntitySaver saver) {
        saver.updateRecipes(Collections.singletonList(entity));
    }

    @Override
    protected SimpleGetCommand<Recipe> getGetEntityCommand(String name) {
        return new GetRecipeCommand(name);
    }

    @Override
    protected Stream<Arguments> getEntity() {
        return TestUtils.generateArguments(TestData::getRecipes);
    }

    @Override
    protected Stream<Arguments> getStorageWithEntity() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(), Collections.singletonList(TestData::getRecipes));
    }

    @Override
    protected String getName(Recipe entity) {
        return entity.getName();
    }
}
