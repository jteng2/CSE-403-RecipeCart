/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestUtils;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class SearchTagsCommandTest extends AbstractSearchCommandTest<Tag> {
    @Override
    protected Stream<Arguments> getSearchEntities() {
        return TestUtils.getSearchTags(getMockStorageGenerators());
    }

    @Override
    protected void addEntitiesToStorage(Collection<Tag> entities, EntitySaver saver) {
        saver.updateTags(entities);
    }

    @Override
    protected AbstractSearchCommand<Tag> getSearchEntityCommand(Set<String> tokens) {
        return new SearchTagsCommand(tokens);
    }
}
