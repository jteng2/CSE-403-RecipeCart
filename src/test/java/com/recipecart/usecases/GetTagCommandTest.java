/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GetTagCommandTest extends SimpleGetCommandTest<Tag> {
    @Override
    protected void addEntityToStorage(Tag entity, EntitySaver saver) {
        saver.updateTags(Collections.singletonList(entity));
    }

    @Override
    protected SimpleGetCommand<Tag> getGetEntityCommand(String name) {
        return new GetTagCommand(name);
    }

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
    protected String getName(Tag entity) {
        return entity.getName();
    }
}
