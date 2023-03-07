/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Ingredient;
import com.recipecart.storage.EntityLoader;
import java.util.Set;

/**
 * This class represents the use case of a user searching for ingredients using given search terms.
 */
public final class SearchIngredientsCommand extends AbstractSearchCommand<Ingredient> {
    public static final String
            OK_MATCHES_FOUND = "Search successful: ingredients that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching ingredients were found",
            NOT_OK_BAD_SEARCH_TERMS =
                    "Search unsuccessful: ingredient search terms were not well-formed";

    /**
     * Creates the action item of searching for an Ingredient(s).
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchIngredientsCommand(Set<String> searchTerms) {
        super(searchTerms);
    }

    @Override
    protected Set<Ingredient> searchEntities(EntityLoader loader) {
        return loader.searchIngredients(getSearchTerms());
    }

    @Override
    protected String getEntityClassName() {
        return Ingredient.class.getName();
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
