/* (C)2023 */
package com.recipecart.entities;

import com.recipecart.utils.Utils;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an immutable recipe, that contains information for cooking the recipe and
 * information on the resulting food when the recipe is cooked.
 */
public final class Recipe {
    private final @Nullable String name; // EntityStorage's "unique identifier" for Recipe
    private final @Nullable String presentationName;
    private final @Nullable String authorUsername;
    private final @Nullable Integer prepTime;
    private final @Nullable Integer cookTime;
    private final @Nullable String imageUri;
    private final @Nullable Integer numServings;
    private final double avgRating;
    private final int numRatings;
    private final @NotNull List<@NotNull String> directions;
    private final @NotNull Set<@NotNull Tag> tags;
    private final @NotNull Map<@NotNull Ingredient, @NotNull Double> requiredIngredients;

    private Recipe(
            @Nullable String name,
            @Nullable String presentationName,
            @Nullable String authorUsername,
            @Nullable Integer prepTime,
            @Nullable Integer cookTime,
            @Nullable String imageUri,
            @Nullable Integer numServings,
            double avgRating,
            int numRatings,
            @NotNull List<@NotNull String> directions,
            @NotNull Set<@NotNull Tag> tags,
            @NotNull Map<@NotNull Ingredient, @NotNull Double> requiredIngredients) {
        this.name = name;
        this.presentationName = presentationName;
        this.authorUsername = authorUsername;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.imageUri = imageUri;
        this.numServings = numServings;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
        this.directions = directions;
        this.tags = tags;
        this.requiredIngredients = requiredIngredients;
    }

    /**
     * @return the "true" name of this Recipe
     */
    @Nullable public String getName() {
        return name;
    }

    /**
     * @return the name that appears for this Recipe
     */
    @Nullable public String getPresentationName() {
        return presentationName;
    }

    /**
     * @return the username of the user that created this recipe
     */
    @Nullable public String getAuthorUsername() {
        return authorUsername;
    }

    /**
     * @return the amount of time required to prepare to cook this Recipe, in minutes
     */
    @Nullable public Integer getPrepTime() {
        return prepTime;
    }

    /**
     * @return the amount of time required to cook this Recipe, in minutes
     */
    @Nullable public Integer getCookTime() {
        return cookTime;
    }

    /**
     * @return a URI leading to an image of this Recipe's finished product
     */
    @Nullable public String getImageUri() {
        return imageUri;
    }

    /**
     * @return the number of people this Recipe's finished product intends to serve
     */
    @Nullable public Integer getNumServings() {
        return numServings;
    }

    /**
     * @return this Recipe's average rating received by users
     */
    public double getAvgRating() {
        return avgRating;
    }

    /**
     * @return the number of users that have rated this Recipe
     */
    public int getNumRatings() {
        return numRatings;
    }

    /**
     * @return an unmodifiable list with directions to cook this Recipe
     */
    @NotNull public List<@NotNull String> getDirections() {
        return Collections.unmodifiableList(directions);
    }

    /**
     * @return an unmodifiable set with Tags associated with this Recipe
     */
    @NotNull public Set<@NotNull Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * @return an unmodifiable map with Ingredients and associated amounts required for this Recipe
     */
    @NotNull public Map<@NotNull Ingredient, @NotNull Double> getRequiredIngredients() {
        return Collections.unmodifiableMap(requiredIngredients);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Recipe recipe = (Recipe) o;
        return Double.compare(recipe.getAvgRating(), getAvgRating()) == 0
                && getNumRatings() == recipe.getNumRatings()
                && Objects.equals(getName(), recipe.getName())
                && Objects.equals(getPresentationName(), recipe.getPresentationName())
                && Objects.equals(getAuthorUsername(), recipe.getAuthorUsername())
                && Objects.equals(getPrepTime(), recipe.getPrepTime())
                && Objects.equals(getCookTime(), recipe.getCookTime())
                && Objects.equals(getImageUri(), recipe.getImageUri())
                && Objects.equals(getNumServings(), recipe.getNumServings())
                && getDirections().equals(recipe.getDirections())
                && getTags().equals(recipe.getTags())
                && getRequiredIngredients().equals(recipe.getRequiredIngredients());
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                getName(),
                getPresentationName(),
                getAuthorUsername(),
                getPrepTime(),
                getCookTime(),
                getImageUri(),
                getNumServings(),
                getAvgRating(),
                getNumRatings(),
                getDirections(),
                getTags(),
                getRequiredIngredients());
    }

    /**
     * This class is for incrementally building a Recipe field-by-field (instead of constructing a
     * Recipe with all fields at once). Not all fields need to be specified when building the
     * Recipe. Unspecified data structure fields default to empty immutable data structures.
     * Unspecified primitives default to respective primitive defaults. Unspecified
     * non-data-structure objects default to null.
     */
    public static class Builder {
        private @Nullable String name;
        private @Nullable String presentationName;
        private @Nullable String authorUsername;
        private @Nullable Integer prepTime;
        private @Nullable Integer cookTime;
        private @Nullable String imageUri;
        private @Nullable Integer numServings;
        private double avgRating = 0;
        private int numRatings = 0;
        private @NotNull List<@NotNull String> directions;
        private @NotNull Set<@NotNull Tag> tags;
        private @NotNull Map<@NotNull Ingredient, @NotNull Double> requiredIngredients;

        /** Initializes all fields to their defaults. */
        public Builder() {
            directions = Collections.emptyList();
            tags = Collections.emptySet();
            requiredIngredients = Collections.emptyMap();
        }

        /**
         * Initializes all fields to the same fields in the given Recipe
         *
         * @param toCopy the Recipe whose fields to copy
         */
        public Builder(Recipe toCopy) {
            this();
            setName(toCopy.getName())
                    .setPresentationName(toCopy.getPresentationName())
                    .setAuthorUsername(toCopy.getAuthorUsername())
                    .setPrepTime(toCopy.getPrepTime())
                    .setCookTime(toCopy.getCookTime())
                    .setImageUri(toCopy.getImageUri())
                    .setNumServings(toCopy.getNumServings())
                    .setAvgRating(toCopy.getAvgRating())
                    .setNumRatings(toCopy.getNumRatings())
                    .setDirections(toCopy.getDirections())
                    .setTags(toCopy.getTags())
                    .setRequiredIngredients(toCopy.getRequiredIngredients());
        }

        /**
         * @return a new Recipe with fields specified via this Builder. Modifying data structures
         *     given to this Builder while specifying fields will not modify the returned Recipe.
         */
        public Recipe build() {
            return new Recipe(
                    name,
                    presentationName,
                    authorUsername,
                    prepTime,
                    cookTime,
                    imageUri,
                    numServings,
                    avgRating,
                    numRatings,
                    new ArrayList<>(directions),
                    new HashSet<>(tags),
                    new HashMap<>(requiredIngredients));
        }

        /**
         * Sets the "true" name of the Recipe that is to be built
         *
         * @param name the "true" name
         * @return this
         */
        public Builder setName(@Nullable String name) {
            this.name = name;
            return this;
        }

        /**
         * Sets the name that appears for the Recipe that is to be built
         *
         * @param presentationName the presentation name
         * @return this
         */
        public Builder setPresentationName(@Nullable String presentationName) {
            this.presentationName = presentationName;
            return this;
        }

        /**
         * Sets the username of the author of the Recipe that is to be built
         *
         * @param authorUsername the author's username
         * @return this
         */
        public Builder setAuthorUsername(@Nullable String authorUsername) {
            this.authorUsername = authorUsername;
            return this;
        }

        /**
         * Sets the prep time of the Recipe that is to be built
         *
         * @param prepTime the prep time, in minutes
         * @return this
         */
        public Builder setPrepTime(@Nullable Integer prepTime) {
            this.prepTime = prepTime;
            return this;
        }

        /**
         * Sets the cook time for the Recipe that is to be built
         *
         * @param cookTime the cook time, in minutes
         * @return this
         */
        public Builder setCookTime(@Nullable Integer cookTime) {
            this.cookTime = cookTime;
            return this;
        }

        /**
         * Sets the URI of the image of the Recipe that is to be built
         *
         * @param imageUri the image URI
         * @return this
         */
        public Builder setImageUri(@Nullable String imageUri) {
            this.imageUri = imageUri;
            return this;
        }

        /**
         * Sets the number of servings of the Recipe that is to be built
         *
         * @param numServings the number of servings
         * @return this
         */
        public Builder setNumServings(@Nullable Integer numServings) {
            this.numServings = numServings;
            return this;
        }

        /**
         * Sets the average rating of the Recipe that is to be built
         *
         * @param avgRating the average rating
         * @return this
         */
        public Builder setAvgRating(double avgRating) {
            this.avgRating = avgRating;
            return this;
        }

        /**
         * Sets the number of ratings of the Recipe that is to be built
         *
         * @param numRatings the total number of ratings
         * @return this
         */
        public Builder setNumRatings(int numRatings) {
            this.numRatings = numRatings;
            return this;
        }

        /**
         * Sets the directions of the Recipe that is to be built
         *
         * @param directions the directions to make the recipe; cannot be null or have null elements
         * @return this
         */
        public Builder setDirections(@NotNull List<@NotNull String> directions) {
            Utils.requireAllNotNull(
                    directions,
                    "Directions list cannot be null",
                    "Individual directions cannot be null");
            this.directions = directions;
            return this;
        }

        /**
         * Sets the associated tags of the Recipe that is to be built
         *
         * @param tags the associated tags of the Recipe; cannot be null or have null elements
         * @return this
         */
        public Builder setTags(@NotNull Set<@NotNull Tag> tags) {
            Utils.requireAllNotNull(
                    tags, "Tags set cannot be null", "Individual Tags cannot be null");
            this.tags = tags;
            return this;
        }

        /**
         * Sets the ingredients required for the Recipe that is to be built
         *
         * @param requiredIngredients the ingredients (with amounts) to make the recipe; cannot be
         *     null or have null elements
         * @return this
         */
        public Builder setRequiredIngredients(
                @NotNull Map<@NotNull Ingredient, @NotNull Double> requiredIngredients) {
            Utils.requireAllMapNotNull(
                    requiredIngredients,
                    "Required Ingredient map cannot be null",
                    "Individual Ingredients cannot be null",
                    "Ingredient amounts cannot be null");
            this.requiredIngredients = requiredIngredients;
            return this;
        }
    }
}
