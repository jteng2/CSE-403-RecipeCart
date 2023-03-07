/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestUtils;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class SearchIngredientsCommandTest extends AbstractSearchCommandTest<Ingredient> {
    @Override
    protected Stream<Arguments> getSearchEntities() {
        return TestUtils.getSearchIngredients(getMockStorageGenerators());
    }

    @Override
    protected void addEntitiesToStorage(Collection<Ingredient> entities, EntitySaver saver) {
        saver.updateIngredients(entities);
    }

    @Override
    protected AbstractSearchCommand<Ingredient> getSearchEntityCommand(Set<String> tokens) {
        return new SearchIngredientsCommand(tokens);
    }
}
