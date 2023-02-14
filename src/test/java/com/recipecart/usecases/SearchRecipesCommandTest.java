/* (C)2023 */
package com.recipecart.usecases;

import static com.recipecart.testutil.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import com.recipecart.database.BadEntityLoader;
import com.recipecart.database.BadEntitySaver;
import com.recipecart.entities.Recipe;
import com.recipecart.storage.EntityStorage;
import com.recipecart.testutil.TestData;
import com.recipecart.testutil.TestUtils;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class SearchRecipesCommandTest {
    private static Stream<Arguments> getSearchRecipes() {
        return TestUtils.getSearchRecipes(getMockStorageGenerators());
    }

    private static Stream<Arguments> getTokensParams() {
        return generateArguments(TestData::getSetTagNoNulls);
    }

    private static Stream<Arguments> getInvalidTokensParams() {
        return generateArguments(TestData::getSetTagWithNulls);
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetSearchTerms(Set<String> tokens) {
        SearchRecipesCommand search = new SearchRecipesCommand(tokens);

        assertEquals(tokens, new HashSet<>(search.getSearchTerms()));
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetSearchTermsExceptions(Set<String> tokens) {
        SearchRecipesCommand search = new SearchRecipesCommand(tokens);

        assertThrows(UnsupportedOperationException.class, search.getSearchTerms()::clear);
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testGetMatchesBeforeExecution(Set<String> tokens) {
        SearchRecipesCommand search = new SearchRecipesCommand(tokens);

        assertThrows(IllegalStateException.class, search::getMatches);
    }

    @ParameterizedTest
    @MethodSource("getInvalidTokensParams")
    void testInvalidTokens(@Nullable Set<@Nullable String> invalidTokens) {
        assertThrows(NullPointerException.class, () -> new SearchRecipesCommand(invalidTokens));
    }

    @ParameterizedTest
    @MethodSource("getSearchRecipes")
    void testSearchForRecipe(
            EntityStorage storage, List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected) {
        storage.getSaver().updateRecipes(recipes);

        SearchRecipesCommand search = new SearchRecipesCommand(tokens);
        search.setStorageSource(storage);
        search.execute();

        assertTrue(search.isFinishedExecuting());
        assertTrue(search.isSuccessful());

        List<Recipe> matches = search.getMatches();
        assertNotNull(matches);
        assertEquals(expected.size(), matches.size());
        Set<Recipe> uniqueMatches = new HashSet<>(matches);
        assertEquals(expected, uniqueMatches);
        assertEquals(
                matches.size() == 0
                        ? SearchRecipesCommand.OK_NO_MATCHES_FOUND
                        : SearchRecipesCommand.OK_MATCHES_FOUND,
                search.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getTokensParams")
    void testUnsuccessfulSearch(Set<String> tokens) {
        SearchRecipesCommand search = new SearchRecipesCommand(tokens);
        search.setStorageSource(new EntityStorage(new BadEntitySaver(), new BadEntityLoader()));
        search.execute();

        assertTrue(search.isFinishedExecuting());
        assertFalse(search.isSuccessful());
        assertNull(search.getMatches());
        assertEquals(SearchRecipesCommand.NOT_OK_SEARCH_ERROR, search.getExecutionMessage());
    }

    @ParameterizedTest
    @MethodSource("getSearchRecipes")
    void testExceptionsAfterRecipeSearch(
            EntityStorage storage, List<Recipe> recipes, Set<String> tokens, Set<Recipe> expected) {
        storage.getSaver().updateRecipes(recipes);

        SearchRecipesCommand search = new SearchRecipesCommand(tokens);
        search.setStorageSource(storage);
        search.execute();

        assertThrows(IllegalStateException.class, search::execute);
        assertThrows(UnsupportedOperationException.class, search.getMatches()::clear);
    }
}
