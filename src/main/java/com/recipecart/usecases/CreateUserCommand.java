/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;

/** This class represents an action item for the use case of a new user being created. */
public class CreateUserCommand extends SimpleCreateEntityCommand<User> {
    public static final String OK_USER_CREATED = "User creation successful",
            NOT_OK_INVALID_USER = "User creation unsuccessful: the user was invalid or null",
            NOT_OK_USERNAME_TAKEN = "User creation unsuccessful: the username is already taken";

    /**
     * Creates the action item for creating a new User and saving it into a given EntityStorage.
     * This user starts off with nothing (i.e. no saved recipes, etc.).
     *
     * @param username the username of the user to create
     * @param emailAddress an email address of the user to create
     */
    public CreateUserCommand(String username, String emailAddress) {
        super(new User.Builder().setUsername(username).setEmailAddress(emailAddress).build());
    }

    @Override
    protected String getOkEntityCreatedMessage() {
        return OK_USER_CREATED;
    }

    @Override
    protected String getNotOkInvalidEntityMessage() {
        return NOT_OK_INVALID_USER;
    }

    @Override
    protected String getNotOkEntityNameAlreadyTakenMessage() {
        return NOT_OK_USERNAME_TAKEN;
    }

    @Override
    protected String getEntityName(User entity) {
        return entity.getUsername();
    }

    @Override
    protected String getEntityClassName() {
        return User.class.getName();
    }

    @Override
    protected void updateEntities(EntitySaver saver, Collection<User> entities) {
        saver.updateUsers(entities);
    }

    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.usernameExists(entityName);
    }
}
