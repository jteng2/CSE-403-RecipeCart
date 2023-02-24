/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Collections;

/**
 * This class represents the action item for the use case of retrieving a user from a given
 * EntityStorage.
 */
public class GetUserCommand extends SimpleGetCommand<User> {
    public static final String OK_USER_RETRIEVED = "User retrieval successful",
            NOT_OK_USER_NOT_FOUND =
                    "User retrieval unsuccessful: a user with the given name could not be found";

    /**
     * Creates the action item for retrieving a user with the given name.
     *
     * @param name the name of the user to retrieve.
     */
    GetUserCommand(String name) {
        super(name);
    }

    @Override
    protected String getOkEntityRetrievedMessage() {
        return OK_USER_RETRIEVED;
    }

    @Override
    protected String getNotOkNotFoundMessage() {
        return NOT_OK_USER_NOT_FOUND;
    }

    @Override
    protected String getEntityClassName() {
        return User.class.getName();
    }

    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.usernameExists(entityName);
    }

    @Override
    protected User retrieveEntity(EntityLoader loader, String entityName) throws IOException {
        return loader.getUsersByNames(Collections.singletonList(entityName)).get(0);
    }
}
