/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntityLoader;
import java.util.*;

/** This class represents the use case of a user searching for recipes using given search terms. */
public final class SearchRecipesCommand extends AbstractSearchCommand<Recipe> {
    public static final String
            OK_MATCHES_FOUND = "Search successful: recipes that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching recipes were found",
            NOT_OK_BAD_SEARCH_TERMS =
                    "Search unsuccessful: recipe search terms were not well-formed";

    /**
     * Creates the action item of searching for a Recipe(s).
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchRecipesCommand(Set<String> searchTerms) {
        super(searchTerms);
    }

    /** {@inheritDoc} */
    @Override
    protected Set<Recipe> searchEntities(EntityLoader loader) {
        return loader.searchRecipes(getSearchTerms());
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityClassName() {
        return Recipe.class.getName();
    }

    /** {@inheritDoc} */
    @Override
    protected String getOkMatchesFoundMessage() {
        return OK_MATCHES_FOUND;
    }

    /** {@inheritDoc} */
    @Override
    protected String getOkNoMatchesFoundMessage() {
        return OK_NO_MATCHES_FOUND;
    }

    /** {@inheritDoc} */
    @Override
    protected String getNotOkBadSearchTermsMessage() {
        return NOT_OK_BAD_SEARCH_TERMS;
    }
}
