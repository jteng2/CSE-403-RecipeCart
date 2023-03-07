/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntityLoader;
import java.util.*;

/** This class represents the use case of someone searching for recipes. */
public final class SearchRecipesCommand extends AbstractSearchCommand<Recipe> {
    public static final String
            OK_MATCHES_FOUND = "Search successful: recipes that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching recipes were found",
            NOT_OK_BAD_SEARCH_TERMS = "Search unsuccessful: search terms were not well-formed";

    /**
     * Creates the action item of searching for a Recipe(s).
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchRecipesCommand(Set<String> searchTerms) {
        super(searchTerms);
    }

    @Override
    protected Set<Recipe> searchEntities(EntityLoader loader) {
        return loader.searchRecipes(getSearchTerms());
    }

    @Override
    protected String getEntityClassName() {
        return Recipe.class.getName();
    }

    @Override
    protected String getOkMatchesFoundMessage() {
        return OK_MATCHES_FOUND;
    }

    @Override
    protected String getOkNoMatchesFoundMessage() {
        return OK_NO_MATCHES_FOUND;
    }

    @Override
    protected String getNotOkBadSearchTermsMessage() {
        return NOT_OK_BAD_SEARCH_TERMS;
    }
}
