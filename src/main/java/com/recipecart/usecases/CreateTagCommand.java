/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntityLoader;
import com.recipecart.storage.EntitySaver;
import java.util.Collection;

/** This class represents an action item for the use case of a new tag being created. */
public final class CreateTagCommand extends SimpleCreateEntityCommand<Tag> {
    public static final String OK_TAG_CREATED = "Tag creation successful",
            NOT_OK_INVALID_TAG = "Tag creation unsuccessful: the tag was invalid or null",
            NOT_OK_TAG_NAME_TAKEN = "Tag creation unsuccessful: the tag name is already taken";

    /**
     * Creates the action item for creating a Tag and saving it into a given EntityStorage.
     *
     * @param toAdd the Tag to add
     */
    public CreateTagCommand(Tag toAdd) {
        super(toAdd);
    }

    /**
     * Creates the action item for creating a Tag and saving it into a given EntityStorage.
     *
     * @param tagName the name of the Tag to add
     */
    public CreateTagCommand(String tagName) {
        this(new Tag(tagName));
    }

    /** {@inheritDoc} */
    @Override
    protected String getOkEntityCreatedMessage() {
        return OK_TAG_CREATED;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkInvalidEntityMessage() {
        return NOT_OK_INVALID_TAG;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkEntityNameAlreadyTakenMessage() {
        return NOT_OK_TAG_NAME_TAKEN;
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityName(Tag entity) {
        return entity.getName();
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityClassName() {
        return Tag.class.toString();
    }

    /** {@inheritDoc} */
    @Override
    protected void updateEntities(EntitySaver saver, Collection<Tag> entities) {
        saver.updateTags(entities);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.tagNameExists(entityName);
    }
}
