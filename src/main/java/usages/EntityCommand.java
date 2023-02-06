/* (C)2023 */
package usages;

import entities.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import storage.EntityStorage;

/** This abstract class represents an action item that represents a use case involving entities. */
public abstract class EntityCommand {
    private @Nullable EntityStorage storage = null;

    /**
     * Executes this EntityCommand, performing the use case represented by the implementing class.
     *
     * @return the results of executing this command
     */
    @NotNull abstract Result execute();

    /** @return the place this EntityCommand saves and loads entities during execution. */
    @Nullable public EntityStorage getStorageSource() {
        return storage;
    }

    /**
     * Sets the place where this EntityCommand will save and load entities while executing.
     *
     * @param storage the place to save/load entities
     */
    void setStorageSource(@Nullable EntityStorage storage) {
        this.storage = storage;
    }

    /** This class represents the immutable results from executing a EntityCommand. */
    public static class Result {
        private final boolean success;
        private final @NotNull List<Tag> tags;
        private final @NotNull List<Ingredient> ingredients;
        private final @NotNull List<Recipe> recipes;
        private final @NotNull List<User> users;

        private Result(
                boolean success,
                @NotNull List<Tag> tags,
                @NotNull List<Ingredient> ingredients,
                @NotNull List<Recipe> recipes,
                @NotNull List<User> users) {
            this.success = success;
            this.tags = tags;
            this.ingredients = ingredients;
            this.recipes = recipes;
            this.users = users;
        }

        /** @return true if the EntityCommand was executed successfully, false otherwise */
        public boolean isSuccess() {
            return success;
        }

        /**
         * @return an unmodifiable list with any Tags resulting from any querying this EntityCommand
         *     did. If the EntityCommand did no querying, an empty list is returned.
         */
        public List<Tag> getTags() {
            return Collections.unmodifiableList(tags);
        }

        /**
         * @return an unmodifiable list with any Ingredients resulting from any querying this
         *     EntityCommand did. If the EntityCommand did no querying, an empty list is returned.
         */
        public List<Ingredient> getIngredients() {
            return Collections.unmodifiableList(ingredients);
        }

        /**
         * @return an unmodifiable list with any Recipes resulting from any querying this
         *     EntityCommand did. If the EntityCommand did no querying, an empty list is returned.
         */
        public List<Recipe> getRecipes() {
            return Collections.unmodifiableList(recipes);
        }

        /**
         * @return an unmodifiable list with any Users resulting from any querying this
         *     EntityCommand did. If the EntityCommand did no querying, an empty list is returned.
         */
        public List<User> getUsers() {
            return Collections.unmodifiableList(users);
        }

        /**
         * This class is for incrementally building a Result field-by-field (instead of constructing
         * a User with all fields at once). Not all fields need to be specified when building the
         * Result. Unspecified data structure fields default to empty immutable data structures.
         * "success" default to false.
         */
        static class Builder {
            private boolean success;
            private @NotNull List<Tag> tags;
            private @NotNull List<Ingredient> ingredients;
            private @NotNull List<Recipe> recipes;
            private @NotNull List<User> users;

            /** Initializes all fields to their defaults. */
            Builder() {
                this.success = false;
                this.tags = Collections.emptyList();
                this.ingredients = Collections.emptyList();
                this.recipes = Collections.emptyList();
                this.users = Collections.emptyList();
            }

            /**
             * @return a new Result with fields specified via this Builder. Modifying data
             *     structures given to this Builder will not modify the returned Result.
             */
            Result build() {
                return new Result(
                        success,
                        new ArrayList<>(tags),
                        new ArrayList<>(ingredients),
                        new ArrayList<>(recipes),
                        new ArrayList<>(users));
            }

            Builder setSuccess(boolean success) {
                this.success = success;
                return this;
            }

            Builder setTags(@NotNull List<Tag> tags) {
                this.tags = tags;
                return this;
            }

            Builder setIngredients(@NotNull List<Ingredient> ingredients) {
                this.ingredients = ingredients;
                return this;
            }

            Builder setRecipes(@NotNull List<Recipe> recipes) {
                this.recipes = recipes;
                return this;
            }

            Builder setUsers(@NotNull List<User> users) {
                this.users = users;
                return this;
            }
        }
    }
}
