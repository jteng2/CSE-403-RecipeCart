/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;

import com.recipecart.entities.User;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class GetUserCommandTest extends SimpleGetCommandTest<User> {
    @Override
    protected void addEntityToStorage(User entity, EntitySaver saver) {
        saver.updateUsers(Collections.singletonList(entity));
    }

    @Override
    protected SimpleGetCommand<User> getGetEntityCommand(String name) {
        return new GetUserCommand(name);
    }

    @Override
    protected Stream<Arguments> getEntity() {
        return TestUtils.generateArguments(TestData::getUsers);
    }

    @Override
    protected Stream<Arguments> getStorageWithEntity() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(), Collections.singletonList(TestData::getUsers));
    }

    @Override
    protected String getName(User entity) {
        return entity.getUsername();
    }
}
