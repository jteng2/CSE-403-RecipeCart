/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.storage.EntityLoader;
import com.recipecart.utils.Utils;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents the use case of someone searching for RecipeCart entities that match some
 * given set of search terms.
 *
 * @param <T> the type of entity being searched for (ex. Recipe, etc.)
 */
public abstract class AbstractSearchCommand<T> extends EntityCommand {
    private final Set<String> searchTerms;
    private @Nullable Set<@NotNull T> matchingEntities = null;

    /**
     * Creates the action item of searching for an entity.
     *
     * @param searchTerms the search terms to use when searching.
     */
    public AbstractSearchCommand(Set<String> searchTerms) {
        this.searchTerms = Utils.allowNull(searchTerms, HashSet::new);
    }

    /**
     * @return an unmodifiable view of the search terms this command will do its search with.
     */
    public Set<String> getSearchTerms() {
        return Utils.allowNull(searchTerms, Collections::unmodifiableSet);
    }

    /**
     * Returns the entities that matched with the given search terms when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the entities that matched. If no entities matched, then the list will be empty. If
     *     the search's execution failed, then the list will be null.
     */
    @Nullable public Set<@NotNull T> getMatchingEntities() {
        if (!isFinishedExecuting()) {
            throw new IllegalStateException("Command hasn't finished executing yet");
        }
        return Utils.allowNull(matchingEntities, Collections::unmodifiableSet);
    }

    private void setMatchingEntities(@NotNull Set<@NotNull T> matches) {
        if (isFinishedExecuting()) {
            throw new IllegalStateException(
                    "Cannot set matching "
                            + getEntityClassName()
                            + "(s) after command has executed");
        }
        if (matchingEntities != null) {
            throw new IllegalStateException(
                    "Can only set matching " + getEntityClassName() + "(s) once");
        }
        Utils.requireAllNotNull(
                matches,
                "Cannot set (non-default) matching " + getEntityClassName() + "(s) to null",
                "Cannot have null " + getEntityClassName() + "(s) in matches");
        matchingEntities = matches;
    }

    /**
     * Returns a message describing if the search that happened when executing this command was
     * successful or not.
     *
     * @throws IllegalStateException if the search hasn't finished (or started) yet
     * @return the message describing the result of the search
     */
    @Override
    @NotNull public String getExecutionMessage() {
        return super.getExecutionMessage();
    }

    /** {@inheritDoc} */
    @Override
    protected String getInvalidCommandMessage() {
        String baseMessage = super.getInvalidCommandMessage();
        if (baseMessage != null) {
            return baseMessage;
        }
        if (!areSearchTermsValid()) {
            return getNotOkBadSearchTermsMessage();
        }
        return null;
    }

    private boolean areSearchTermsValid() {
        return getSearchTerms() != null
                && !getSearchTerms().isEmpty()
                && !getSearchTerms().contains(null);
    }

    /**
     * Searches for entities that match with the given search terms. Only entities that contain each
     * search term will be matched. The command will be successful if the search was successfully
     * executed, even if no entities that matched the search terms were found.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        checkExecutionAlreadyDone();
        if (finishInvalidCommand()) {
            return;
        }

        Set<T> matches;
        try {
            assert getStorageSource() != null; // storage source is always valid at this point
            matches = searchEntities(getStorageSource().getLoader());
        } catch (RuntimeException e) {
            finishExecutingFromError(e);
            return;
        }
        finishExecutingSuccessfulSearch(matches);
    }

    private void finishExecutingSuccessfulSearch(Set<T> matches) {
        setMatchingEntities(matches);
        setExecutionMessage(
                matches.size() == 0 ? getOkNoMatchesFoundMessage() : getOkMatchesFoundMessage());
        beSuccessful();
        finishExecuting();
    }

    /**
     * Searches the storage corresponding to the given loader, for entities this command's search
     * term matches with.
     *
     * @param loader the loader to do the search with
     * @return the matching entities based on the search done with the loader
     */
    protected abstract Set<T> searchEntities(EntityLoader loader);

    /**
     * @return the class name of the entities being searched for.
     */
    protected abstract String getEntityClassName();

    /**
     * @return a message saying that the command's execution was successful, and matching entities
     *     were found.
     */
    protected abstract String getOkMatchesFoundMessage();

    /**
     * @return a message saying that the command's execution was successful, but no matching
     *     entities were found.
     */
    protected abstract String getOkNoMatchesFoundMessage();

    /**
     * @return a message saying that the command's execution was unsuccessful due to the given
     *     search terms being invalid.
     */
    protected abstract String getNotOkBadSearchTermsMessage();
}
