/* (C)2023 */
package com.recipecart.usecases;

import com.recipecart.entities.User;
import com.recipecart.storage.EntityLoader;
import java.util.Set;

/** This class represents the use case of a user searching for users using given search terms. */
public final class SearchUsersCommand extends AbstractSearchCommand<User> {
    public static final String
            OK_MATCHES_FOUND = "Search successful: users that matched were found",
            OK_NO_MATCHES_FOUND = "Search successful: but no matching users were found",
            NOT_OK_BAD_SEARCH_TERMS = "Search unsuccessful: user search terms were not well-formed";

    /**
     * Creates the action item of searching for a User(s).
     *
     * @param searchTerms the search terms to use when searching.
     */
    public SearchUsersCommand(Set<String> searchTerms) {
        super(searchTerms);
    }

    @Override
    protected Set<User> searchEntities(EntityLoader loader) {
        return loader.searchUsers(getSearchTerms());
    }

    @Override
    protected String getEntityClassName() {
        return User.class.getName();
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
