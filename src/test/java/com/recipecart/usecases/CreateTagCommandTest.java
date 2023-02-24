/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class CreateTagCommandTest extends SimpleCreateEntityCommandTest<Tag> {
    @Override
    protected Stream<Arguments> getEntity() {
        return TestUtils.generateArguments(TestData::getTags);
    }

    @Override
    protected Stream<Arguments> getStorageWithEntity() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(), Collections.singletonList(TestData::getTags));
    }

    @Override
    protected SimpleCreateEntityCommand<Tag> getCreateEntityCommand(Tag entity) {
        return new CreateTagCommand(entity);
    }

    @Override
    protected Tag renameToNullEntity(Tag entity) {
        return new Tag(null);
    }

    @Override
    protected void addEntitiesToStorage(Collection<Tag> entity, EntitySaver saver) {
        saver.updateTags(entity);
    }
}
