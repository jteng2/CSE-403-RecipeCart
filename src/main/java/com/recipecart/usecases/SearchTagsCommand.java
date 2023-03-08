/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.Tag;
import com.recipecart.storage.EntityLoader;
import java.util.Set;

/** This class represents the use case of a user searching for tags using given search terms. */
public final class SearchTagsCommand extends AbstractSearchCommand<Tag> {
    public static final String OK_MATCHES_FOUND = "Search successful: tags that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching tags were found",
            NOT_OK_BAD_SEARCH_TERMS = "Search unsuccessful: tag search terms were not well-formed";

    /**
     * Creates the action item of searching for a Tag(s).
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchTagsCommand(Set<String> searchTerms) {
        super(searchTerms);
    }

    /** {@inheritDoc} */
    @Override
    protected Set<Tag> searchEntities(EntityLoader loader) {
        return loader.searchTags(getSearchTerms());
    }

    /** {@inheritDoc} */
    @Override
    protected String getEntityClassName() {
        return Tag.class.getName();
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
