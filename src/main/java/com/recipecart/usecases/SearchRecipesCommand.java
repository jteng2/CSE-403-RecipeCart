/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import com.recipecart.utils.Utils;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class represents the use case of someone searching for recipes. */
public final class SearchRecipesCommand extends EntityCommand {
    public static final String
            OK_MATCHES_FOUND = "Search successful: Recipes that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching Recipes were found",
            NOT_OK_SEARCH_ERROR = "Search unsuccessful: something went wrong during searching";

    private final @NotNull Collection<@NotNull String> searchTerms;

    /**
     * Creates the action item of searching for a recipe.
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchRecipesCommand(@NotNull Collection<@NotNull String> searchTerms) {
        Utils.requireAllNotNull(
                searchTerms,
                "Search term collection cannot be null",
                "Individual search terms cannot be null");
        this.searchTerms = new HashSet<>(searchTerms);
    }

    @NotNull public Collection<@NotNull String> getSearchTerms() {
        return Collections.unmodifiableCollection(searchTerms);
    }

    /**
     * Returns the recipes that matched with the given search terms when executing this command.
     *
     * @throws IllegalStateException if this command instance hasn't finished executing yet.
     * @return the Recipes that matched. If no recipes matched, then the list will be empty. If the
     *     search's execution failed, then the list will be null.
     */
    @Nullable public List<@NotNull Recipe> getMatches() {
        throw new NotImplementedException();
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
        throw new NotImplementedException();
    }

    /**
     * Searches for recipes that match with the given search terms. Only recipes that contain each
     * search term will be matched. The command will be successful if the search was successfully
     * executed, even if no recipes that matched the search terms were found.
     *
     * @throws IllegalStateException if this method has been called before on this command instance.
     */
    @Override
    public void execute() {
        throw new NotImplementedException();
    }
}
