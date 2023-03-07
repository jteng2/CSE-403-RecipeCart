/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;
import static com.recipecart.usecases.SearchRecipesCommand.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestUtils;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class SearchRecipesCommandTest extends AbstractSearchCommandTest<Recipe> {
    @Override
    protected Stream<Arguments> getSearchEntities() {
        return TestUtils.getSearchRecipes(getMockStorageGenerators());
    }

    @Override
    protected void addEntitiesToStorage(Collection<Recipe> entities, EntitySaver saver) {
        saver.updateRecipes(entities);
    }

    @Override
    protected AbstractSearchCommand<Recipe> getSearchEntityCommand(Set<String> tokens) {
        return new SearchRecipesCommand(tokens);
    }
}
