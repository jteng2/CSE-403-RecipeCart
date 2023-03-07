/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;

import com.recipecart.entities.User;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestUtils;
import java.util.*;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class SearchUsersCommandTest extends AbstractSearchCommandTest<User> {
    @Override
    protected Stream<Arguments> getSearchEntities() {
        return TestUtils.getSearchUsers(getMockStorageGenerators());
    }

    @Override
    protected void addEntitiesToStorage(Collection<User> entities, EntitySaver saver) {
        saver.updateUsers(entities);
    }

    @Override
    protected AbstractSearchCommand<User> getSearchEntityCommand(Set<String> tokens) {
        return new SearchUsersCommand(tokens);
    }
}
