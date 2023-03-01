/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.generateArgumentsCombos;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.recipecart.entities.User;
import com.recipecart.storage.EntitySaver;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.params.provider.Arguments;

public class CreateUserCommandTest extends SimpleCreateEntityCommandTest<User> {
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
    protected SimpleCreateEntityCommand<User> getCreateEntityCommand(User entity) {
        if (entity == null) {
            return new CreateUserCommand(null, null);
        }
        return new CreateUserCommand(entity.getUsername(), entity.getEmailAddress());
    }

    @Override
    protected User renameToNullEntity(User entity) {
        return new User.Builder().setEmailAddress(entity.getEmailAddress()).build();
    }

    @Override
    protected void addEntitiesToStorage(Collection<User> entity, EntitySaver saver) {
        saver.updateUsers(entity);
    }

    @Override
    protected void assertGoodEntity(User baseEntity, User outputEntity) {
        assertEquals(baseEntity.getUsername(), outputEntity.getUsername());
        assertEquals(baseEntity.getEmailAddress(), outputEntity.getEmailAddress());
        assertTrue(outputEntity.getAuthoredRecipes().isEmpty());
        assertTrue(outputEntity.getSavedRecipes().isEmpty());
        assertTrue(outputEntity.getRatedRecipes().isEmpty());
        assertTrue(outputEntity.getOwnedIngredients().isEmpty());
        assertTrue(outputEntity.getShoppingList().isEmpty());
    }
}
