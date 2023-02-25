/* (C)2023 */
package com.recipecart.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.User;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.provider.Arguments;

public class ShoppingListCommandTest<T extends ShoppingListCommand> {
    private EntityStorage storage;

    @BeforeEach
    void initStorage() {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        storage = new EntityStorage(saveAndLoader, saveAndLoader);
    }

    protected static Stream<Arguments> getUser() {
        return TestUtils.generateArguments(TestData::getUsers);
    }

    protected EntityStorage getStorage() {
        return storage;
    }

    protected static <T extends ShoppingListCommand> void assertSuccessfulExecution(
            T command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertTrue(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());
        assertNotNull(command.getResultShoppingList());
    }

    protected static <T extends ShoppingListCommand> void assertUnsuccessfulExecution(
            T command, String message) {
        assertTrue(command.isFinishedExecuting());
        assertFalse(command.isSuccessful());
        assertEquals(message, command.getExecutionMessage());
        assertNull(command.getResultShoppingList());
    }

    protected void assertShoppingListGood(T command) throws IOException {
        User shopper =
                getStorage()
                        .getLoader()
                        .getUsersByNames(Collections.singletonList(command.getShopperUsername()))
                        .get(0);
        assertEquals(command.getResultShoppingList(), shopper.getShoppingList());
    }
}
