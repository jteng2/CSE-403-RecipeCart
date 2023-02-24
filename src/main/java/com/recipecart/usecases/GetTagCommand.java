/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntityLoader;
import java.io.IOException;
import java.util.Collections;

/**
 * This class represents the action item for the use case of retrieving a tag from a given
 * EntityStorage.
 */
public final class GetTagCommand extends SimpleGetCommand<Tag> {
    public static final String OK_TAG_RETRIEVED = "Tag retrieval successful",
            NOT_OK_TAG_NOT_FOUND =
                    "Tag retrieval unsuccessful: a tag with the given name could not be found";

    /**
     * Creates the action item for retrieving a tag with the given name.
     *
     * @param name the name of the tag to retrieve.
     */
    GetTagCommand(String name) {
        super(name);
    }

    @Override
    protected String getOkEntityRetrievedMessage() {
        return OK_TAG_RETRIEVED;
    }

    @Override
    protected String getNotOkNotFoundMessage() {
        return NOT_OK_TAG_NOT_FOUND;
    }

    @Override
    protected String getEntityClassName() {
        return Tag.class.getName();
    }

    @Override
    protected boolean entityNameExists(EntityLoader loader, String entityName) {
        return loader.tagNameExists(entityName);
    }

    @Override
    protected Tag retrieveEntity(EntityLoader loader, String entityName) throws IOException {
        return loader.getTagsByNames(Collections.singletonList(entityName)).get(0);
    }
}
