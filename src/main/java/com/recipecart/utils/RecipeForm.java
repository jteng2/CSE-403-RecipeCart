/* (C)2023 */
package com.recipecart.utils;

import com.recipecart.entities.Recipe;
import java.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a version of Recipe where references to other entities are replaced with Strings
 * representing their (unique) names. Creating instances of this class is mainly done through
 * deserialization.
 */
public final class RecipeForm {
    private final String name; // EntityStorage's "unique identifier" for Recipe
    private final String presentationName;
    private final String authorUsername;
    private final Integer prepTime;
    private final Integer cookTime;
    private final String imageUri;
    private final Integer numServings;
    private final double avgRating;
    private final int numRatings;
    private final @Nullable List<String> directions;
    private final @Nullable Set<String> tags;
    private final @Nullable Map<String, Double> requiredIngredients;

    /**
     * Copies the fields of the given Recipe to this RecipeForm. For fields "tags" and
     * "requiredIngredients", tag names and ingredient names are copied instead.
     *
     * @param recipe the recipe whose fields to copy
     */
    public RecipeForm(Recipe recipe) {
        this(
                recipe.getName(),
                recipe.getPresentationName(),
                recipe.getAuthorUsername(),
                recipe.getPrepTime(),
                recipe.getCookTime(),
                recipe.getImageUri(),
                recipe.getNumServings(),
                recipe.getAvgRating(),
                recipe.getNumRatings(),
                recipe.getDirections(),
                Utils.fromTags(recipe.getTags()),
                Utils.fromIngredients(recipe.getRequiredIngredients()));
    }

    public RecipeForm(
            String name,
            String presentationName,
            String authorUsername,
            Integer prepTime,
            Integer cookTime,
            String imageUri,
            Integer numServings,
            double avgRating,
            int numRatings,
            @Nullable List<String> directions,
            @Nullable Set<String> tags,
            @Nullable Map<String, Double> requiredIngredients) {
        this.name = name;
        this.presentationName = presentationName;
        this.authorUsername = authorUsername;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.imageUri = imageUri;
        this.numServings = numServings;
        this.avgRating = avgRating;
        this.numRatings = numRatings;
        this.directions = Utils.allowNull(directions, ArrayList::new);
        this.tags = Utils.allowNull(tags, HashSet::new);
        this.requiredIngredients = Utils.allowNull(requiredIngredients, HashMap::new);
    }

    public String getName() {
        return name;
    }

    public String getPresentationName() {
        return presentationName;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public Integer getPrepTime() {
        return prepTime;
    }

    public Integer getCookTime() {
        return cookTime;
    }

    public String getImageUri() {
        return imageUri;
    }

    public Integer getNumServings() {
        return numServings;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    @Nullable public List<String> getDirections() {
        return Utils.allowNull(directions, Collections::unmodifiableList);
    }

    @Nullable public Set<String> getTagNames() {
        return Utils.allowNull(tags, Collections::unmodifiableSet);
    }

    @Nullable public Map<String, Double> getRequiredIngredients() {
        return Utils.allowNull(requiredIngredients, Collections::unmodifiableMap);
    }
}
