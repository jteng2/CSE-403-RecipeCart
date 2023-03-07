/* (C)2023 */
package com.recipecart.entities;

import com.recipecart.utils.Utils;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an immutable user, that contains various entity-related user information
 */
public final class User {
    private final @Nullable String username; // EntityStorage's "unique identifier" for User
    private final @Nullable String emailAddress;
    private final @NotNull List<@NotNull Recipe> authoredRecipes;
    private final @NotNull List<@NotNull Recipe> savedRecipes;
    private final @NotNull Map<@NotNull Recipe, @NotNull Double> ratedRecipes;
    private final @NotNull Set<@NotNull Ingredient> ownedIngredients;
    private final @NotNull Map<@NotNull Ingredient, @NotNull Double> shoppingList;

    private User(
            @Nullable String username,
            @Nullable String emailAddress,
            @NotNull List<@NotNull Recipe> authoredRecipes,
            @NotNull List<@NotNull Recipe> savedRecipes,
            @NotNull Map<@NotNull Recipe, @NotNull Double> ratedRecipes,
            @NotNull Set<@NotNull Ingredient> ownedIngredients,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> shoppingList) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.authoredRecipes = authoredRecipes;
        this.savedRecipes = savedRecipes;
        this.ratedRecipes = ratedRecipes;
        this.ownedIngredients = ownedIngredients;
        this.shoppingList = shoppingList;
    }

    /**
     * @return this User's username
     */
    @Nullable public String getUsername() {
        return username;
    }

    /**
     * @return an email address associated with this User
     */
    @Nullable public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * @return an unmodifiable list with the Recipes this User has authored
     */
    @NotNull public List<@NotNull Recipe> getAuthoredRecipes() {
        return Collections.unmodifiableList(authoredRecipes);
    }

    /**
     * @return an unmodifiable list with the Recipes this User has saved
     */
    @NotNull public List<@NotNull Recipe> getSavedRecipes() {
        return Collections.unmodifiableList(savedRecipes);
    }

    /**
     * @return an unmodifiable map with the Recipes this User has rated, with the associated ratings
     *     the User has given
     */
    @NotNull public Map<@NotNull Recipe, @NotNull Double> getRatedRecipes() {
        return Collections.unmodifiableMap(ratedRecipes);
    }

    /**
     * @return an unmodifiable set with Ingredients this User owns
     */
    @NotNull public Set<@NotNull Ingredient> getOwnedIngredients() {
        return Collections.unmodifiableSet(ownedIngredients);
    }

    /**
     * @return an unmodifiable map with Ingredients the User intends to buy, and associated amounts
     *     for each ingredient
     */
    @NotNull public Map<@NotNull Ingredient, @NotNull Double> getShoppingList() {
        return Collections.unmodifiableMap(shoppingList);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername())
                && Objects.equals(getEmailAddress(), user.getEmailAddress())
                && getAuthoredRecipes().equals(user.getAuthoredRecipes())
                && getSavedRecipes().equals(user.getSavedRecipes())
                && getRatedRecipes().equals(user.getRatedRecipes())
                && getOwnedIngredients().equals(user.getOwnedIngredients())
                && getShoppingList().equals(user.getShoppingList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getUsername(),
                getEmailAddress(),
                getAuthoredRecipes(),
                getSavedRecipes(),
                getRatedRecipes(),
                getOwnedIngredients(),
                getShoppingList());
    }

    /**
     * This class is for incrementally building a User field-by-field (instead of constructing a
     * User with all fields at once). Not all fields need to be specified when building the User.
     * Unspecified data structure fields default to empty immutable data structures. Unspecified
     * primitives default to respective primitive defaults. Unspecified non-data-structure objects
     * default to null.
     */
    public static class Builder {
        private @Nullable String username;
        private @Nullable String emailAddress;
        private @NotNull List<@NotNull Recipe> authoredRecipes;
        private @NotNull List<@NotNull Recipe> savedRecipes;
        private @NotNull Map<@NotNull Recipe, @NotNull Double> ratedRecipes;
        private @NotNull Set<@NotNull Ingredient> ownedIngredients;
        private @NotNull Map<@NotNull Ingredient, @NotNull Double> shoppingList;

        /** Initializes all fields to their defaults. */
        public Builder() {
            authoredRecipes = Collections.emptyList();
            savedRecipes = Collections.emptyList();
            ratedRecipes = Collections.emptyMap();
            ownedIngredients = Collections.emptySet();
            shoppingList = Collections.emptyMap();
        }

        /**
         * Initializes all fields to the same fields in the given User
         *
         * @param toCopy the User whose fields to copy
         */
        public Builder(User toCopy) {
            this();
            setUsername(toCopy.getUsername())
                    .setEmailAddress(toCopy.getEmailAddress())
                    .setAuthoredRecipes(toCopy.getAuthoredRecipes())
                    .setSavedRecipes(toCopy.getSavedRecipes())
                    .setRatedRecipes(toCopy.getRatedRecipes())
                    .setOwnedIngredients(toCopy.getOwnedIngredients())
                    .setShoppingList(toCopy.getShoppingList());
        }

        /**
         * @return a new User with fields specified via this Builder. Modifying data structures
         *     given to this Builder while specifying fields will not modify the returned User.
         */
        public User build() {
            return new User(
                    username,
                    emailAddress,
                    new ArrayList<>(authoredRecipes),
                    new ArrayList<>(savedRecipes),
                    new HashMap<>(ratedRecipes),
                    new HashSet<>(ownedIngredients),
                    new HashMap<>(shoppingList));
        }

        /**
         * Sets the username of the User that is to be built
         *
         * @param username the username
         * @return this
         */
        public Builder setUsername(@Nullable String username) {
            this.username = username;
            return this;
        }

        /**
         * Sets the known email address of the User that is to be built
         *
         * @param emailAddress the email address
         * @return this
         */
        public Builder setEmailAddress(@Nullable String emailAddress) {
            this.emailAddress = emailAddress;
            return this;
        }

        /**
         * Sets the authored recipes of the User that is to be built
         *
         * @param authoredRecipes the recipes the User authored; cannot be null or have null
         *     elements
         * @return this
         */
        public Builder setAuthoredRecipes(@NotNull List<@NotNull Recipe> authoredRecipes) {
            Utils.requireAllNotNull(
                    authoredRecipes,
                    "Authored Recipe list cannot be null",
                    "Individual authored Recipes cannot be null");
            this.authoredRecipes = authoredRecipes;
            return this;
        }

        /**
         * Sets the saved recipes of the User that is to be built
         *
         * @param savedRecipes the recipes the User saved; cannot be null or have null elements
         * @return this
         */
        public Builder setSavedRecipes(@NotNull List<@NotNull Recipe> savedRecipes) {
            Utils.requireAllNotNull(
                    savedRecipes,
                    "Saved Recipe list cannot be null",
                    "Individual saved Recipes cannot be null");
            this.savedRecipes = savedRecipes;
            return this;
        }

        /**
         * Sets the rated recipes of the User that is to be built
         *
         * @param ratedRecipes the recipes the User rated (with what ratings the User gave); cannot
         *     be null or have null elements
         * @return this
         */
        public Builder setRatedRecipes(
                @NotNull Map<@NotNull Recipe, @NotNull Double> ratedRecipes) {
            Utils.requireAllMapNotNull(
                    ratedRecipes,
                    "Rated Recipe map set cannot be null",
                    "Individual rated Recipes cannot be null",
                    "Recipe ratings cannot be null");
            this.ratedRecipes = ratedRecipes;
            return this;
        }

        /**
         * Sets the owned ingredients of the User that is to be built
         *
         * @param ownedIngredients the ingredients this User owns; cannot be null or have null
         *     elements
         * @return this
         */
        public Builder setOwnedIngredients(@NotNull Set<@NotNull Ingredient> ownedIngredients) {
            Utils.requireAllNotNull(
                    ownedIngredients,
                    "Owned Ingredient list cannot be null",
                    "Individual owned Ingredients cannot be null");
            this.ownedIngredients = ownedIngredients;
            return this;
        }

        /**
         * Sets the ingredients in the shopping list of the User that is to be built
         *
         * @param shoppingList the User's shopping list ingredients (with amounts); cannot be null
         *     or have null elements
         * @return this
         */
        public Builder setShoppingList(
                @NotNull Map<@NotNull Ingredient, @NotNull Double> shoppingList) {
            Utils.requireAllMapNotNull(
                    shoppingList,
                    "Shopping list cannot be null",
                    "Shopping list Ingredients cannot be null",
                    "Shopping list Ingredient amounts cannot be null");
            this.shoppingList = shoppingList;
            return this;
        }
    }
}
