/* (C)2023 */
package com.recipecart.usages;

import com.recipecart.entities.Recipe;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/** This class represents the use case of someone searching for recipes. */
public final class SearchRecipesCommand extends EntityCommand {
    private final @NotNull Collection<@NotNull String> searchTerms;

    /**
     * Creates the action item of searching for a recipe.
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchRecipesCommand(@NotNull Collection<@NotNull String> searchTerms) {
        this.searchTerms = new HashSet<>(searchTerms);
    }

    /**
     * Searches for recipes that match the given search terms. Only recipes that contain each search
     * term will be matched.
     *
     * @return A result containing a list of all Recipes matched. The result will be successful if
     *     one or more Recipes matched, unsuccessful otherwise. The return will be a
     *     SearchRecipesCommand.SearchResult.
     */
    @Override
    @NotNull Result execute() {
        throw new NotImplementedException();
    }

    @NotNull public Collection<@NotNull String> getSearchTerms() {
        return Collections.unmodifiableCollection(searchTerms);
    }

    /** This class represents the results of executing a SearchRecipesCommand. */
    public static class SearchResult extends Result {
        private final @NotNull List<@NotNull Recipe> matches;

        SearchResult(
                boolean success, @NotNull String message, @NotNull List<@NotNull Recipe> matches) {
            super(success, message);
            this.matches = matches;
        }

        /** @return the Recipes that matched when executing the SearchRecipesCommand. */
        @NotNull public List<@NotNull Recipe> getMatches() {
            return Collections.unmodifiableList(matches);
        }
    }
}
