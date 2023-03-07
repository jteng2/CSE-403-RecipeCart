/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;
import static com.recipecart.testutil.TestUtils.getMockStorageArrayGenerators;
import static com.recipecart.usecases.Command.NOT_OK_ERROR;
import static com.recipecart.usecases.EntityCommand.NOT_OK_BAD_STORAGE;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.storage.EntitySaver;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class AbstractSearchCommandTest<T> {
    private AbstractSearchCommand<T> getAndExecuteCommand(
            Set<String> tokens, @Nullable EntityStorage storage, Collection<T> entitiesToStore) {
        AbstractSearchCommand<T> search = getSearchEntityCommand(tokens);
        if (storage != null) {
            if (!entitiesToStore.isEmpty()) {
                addEntitiesToStorage(entitiesToStore, storage.getSaver());
            }
            search.setStorageSource(storage);
        }

        search.execute();

        return search;
    }

    private AbstractSearchCommand<T> getAndExecuteCommand(
            Set<String> tokens, EntityStorage storage) {
        return getAndExecuteCommand(tokens, storage, Collections.emptyList());
    }

    private static <T> void assertUnsuccessfulExecution(
            AbstractSearchCommand<T> search, String message) {
        assertTrue(search.isFinishedExecuting());
        assertFalse(search.isSuccessful());
        assertNull(search.getMatchingEntities());
        assertEquals(message, search.getExecutionMessage());
    }

    private static <T> void assertSuccessfulExecution(
            AbstractSearchCommand<T> search, Set<T> expected) {
        assertTrue(search.isFinishedExecuting());
        assertTrue(search.isSuccessful());

        Set<T> matches = search.getMatchingEntities();
        assertNotNull(matches);
        assertEquals(expected.size(), matches.size());
        assertEquals(expected, matches);
        assertEquals(
                matches.size() == 0
                        ? search.getOkNoMatchesFoundMessage()
                        : search.getOkMatchesFoundMessage(),
                search.getExecutionMessage());
    }

    private static Stream<Arguments> getTokensParams() {
        return generateArguments(TestData::getNonEmptySetStringNoNulls);
    }

    private static Stream<Arguments> getInvalidTokensParams() {
        return generateArgumentsCombos(
                getMockStorageArrayGenerators(),
                Collections.singletonList(TestData::getInvalidSearchTermSet));
    }

    protected abstract Stream<Arguments> getSearchEntities();

    protected abstract void addEntitiesToStorage(Collection<T> entities, EntitySaver saver);

    protected abstract AbstractSearchCommand<T> getSearchEntityCommand(Set<String> tokens);

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetSearchTerms(Set<String> tokens) {
        AbstractSearchCommand<T> search = getSearchEntityCommand(tokens);

        assertEquals(tokens, new HashSet<>(search.getSearchTerms()));
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetSearchTermsExceptions(@NotNull Set<String> tokens) {
        AbstractSearchCommand<T> search = getSearchEntityCommand(tokens);

        assertThrows(UnsupportedOperationException.class, search.getSearchTerms()::clear);
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetMatchesBeforeExecution(Set<String> tokens) {
        AbstractSearchCommand<T> search = getSearchEntityCommand(tokens);

        assertThrows(IllegalStateException.class, search::getMatchingEntities);
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testNullStorageSource(Set<String> tokens) {
        AbstractSearchCommand<T> search = getAndExecuteCommand(tokens, null);

        assertUnsuccessfulExecution(search, NOT_OK_BAD_STORAGE);
    }

    @ParameterizedTest
    @MethodSource("getInvalidTokensParams")
    void testInvalidTokens(EntityStorage storage, @Nullable Set<@Nullable String> invalidTokens) {
        AbstractSearchCommand<T> search = getAndExecuteCommand(invalidTokens, storage);

        assertUnsuccessfulExecution(search, search.getNotOkBadSearchTermsMessage());
    }

    @ParameterizedTest
    @MethodSource("getSearchEntities")
    void testSearchForEntity(
            EntityStorage storage, List<T> entities, Set<String> tokens, Set<T> expected) {
        AbstractSearchCommand<T> search = getAndExecuteCommand(tokens, storage, entities);

        assertSuccessfulExecution(search, expected);
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testSearchWithError(Set<String> tokens) {
        EntityStorage badStorage = new EntityStorage(new BadEntitySaver(), new BadEntityLoader());
        AbstractSearchCommand<T> search = getAndExecuteCommand(tokens, badStorage);

        assertUnsuccessfulExecution(search, NOT_OK_ERROR);
    }

    @ParameterizedTest
    @MethodSource("getSearchEntities")
    void testExceptionsAfterEntitySearch(
            EntityStorage storage, List<T> entities, Set<String> tokens, Set<T> expected) {
        AbstractSearchCommand<T> search = getAndExecuteCommand(tokens, storage, entities);

        assertThrows(IllegalStateException.class, search::execute);
        assertNotNull(search.getMatchingEntities());
        assertThrows(UnsupportedOperationException.class, search.getMatchingEntities()::clear);
    }
}
