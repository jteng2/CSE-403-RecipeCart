/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Tag;
import java.util.Collections;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class represents an action item for the use case of a new Tag being created. */
public class CreateTagCommand extends EntityCommand {
    public static final String OK_TAG_CREATED = "Tag creation successful",
            NOT_OK_INVALID_TAG = "Tag creation unsuccessful: the tag was invalid or null",
            NOT_OK_TAG_NAME_TAKEN = "Tag creation unsuccessful: the tag name is already taken";

    private final Tag toAdd;
    private Tag createdTag = null;

    /**
     * Creates an action item for a new Tag to be created and saved into the EntityStorage given to
     * this command.
     *
     * @param toAdd the Tag to add
     */
    public CreateTagCommand(Tag toAdd) {
        this.toAdd = toAdd;
    }

    /**
     * Creates an action item for a new Tag to be created and saved into the EntityStorage given to
     * this command.
     *
     * @param tagName the name of the Tag to add
     */
    public CreateTagCommand(String tagName) {
        this(new Tag(tagName));
    }

    /**
     * Returns the (output) Tag that was created and saved when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the (non-null) Tag that was created/saved, if the command successfully added it; null
     *     otherwise.
     */
    @Nullable public Tag getCreatedTag() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return createdTag;
    }

    private void setCreatedTag(@NotNull Tag createdTag) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set the created tag after command has executed");
        }
        if (this.createdTag != null) {
            throw new IllegalStateException("Can only set created tag once");
        }
        Objects.requireNonNull(createdTag);
        this.createdTag = createdTag;
    }

    public Tag getToAdd() {
        return toAdd;
    }

    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!isTagValid()) {
            return NOT_OK_INVALID_TAG;
        }
        try {
            if (!isTagNameAvailable()) {
                return NOT_OK_TAG_NAME_TAKEN;
            }
        } catch (RuntimeException e) { // for data access layer failures
            e.printStackTrace();
            return NOT_OK_ERROR;
        }
        return null;
    }

    private boolean isTagValid() {
        return getToAdd() != null && getToAdd().getName() != null;
    }

    private boolean isTagNameAvailable() {
        assert getStorageSource() != null;
        assert getToAdd().getName() != null;
        return !getStorageSource().getLoader().tagNameExists(getToAdd().getName());
    }

    /**
     * Has the Tag be created and saved. If the tag name is null or corresponds to an
     * already-existing tag, then this command's execution will be unsuccessful.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        checkExecutionAlreadyDone();
        if (finishInvalidCommand()) {
            return;
        }

        try {
            saveTag(getToAdd());
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        }

        finishExecutingSuccessfulTag(getToAdd());
    }

    private void saveTag(Tag toSave) {
        assert getStorageSource() != null;

        getStorageSource().getSaver().updateTags(Collections.singletonList(toSave));
    }

    private void finishExecutingSuccessfulTag(Tag created) {
        setCreatedTag(created);
        setExecutionMessage(OK_TAG_CREATED);
        beSuccessful();
        finishExecuting();
    }
}
