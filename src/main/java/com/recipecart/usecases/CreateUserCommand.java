/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;

/** This class represents an action item for the use case of a new user being created. */
public final class CreateUserCommand extends SimpleCreateEntityCommand<User> {
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

    /** {@inheritDoc} */
    @Override
    protected String getOkEntityCreatedMessage() {
        return OK_USER_CREATED;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkInvalidEntityMessage() {
        return NOT_OK_INVALID_USER;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkEntityNameAlreadyTakenMessage() {
        return NOT_OK_USERNAME_TAKEN;
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityName(User entity) {
        return entity.getUsername();
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityClassName() {
        return User.class.getName();
    }

    /** {@inheritDoc} */
    @Override
    protected void updateEntities(EntitySaver saver, Collection<User> entities) {
        saver.updateUsers(entities);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.usernameExists(entityName);
    }
}
