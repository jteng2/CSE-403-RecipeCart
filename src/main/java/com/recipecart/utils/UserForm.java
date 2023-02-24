/* (C)2023 */
package com.recipecart.utils;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.User;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a version of User where references to other entities are replaced with Strings
 * representing their (unique) names. Creating instances of this class is mainly done through
 * deserialization.
 */
public class UserForm {
    private final String username; // EntityStorage's "unique identifier" for User
    private final String emailAddress;
    private final @Nullable List<@NotNull Recipe> authoredRecipes;
    private final @Nullable List<@NotNull Recipe> savedRecipes;
    private final @Nullable Map<@NotNull Recipe, @Nullable Double> ratedRecipes;
    private final @Nullable Set<@NotNull Ingredient> ownedIngredients;
    private final @Nullable Map<@NotNull Ingredient, @NotNull Double> shoppingList;

    /**
     * Copies the fields of the given User to this UserForm. For fields that contain entities such
     * as Recipe or Ingredient, their names are copied instead.
     *
     * @param user the user whose fields to copy
     */
    public UserForm(User user) {
        this(
                user.getUsername(),
                user.getEmailAddress(),
                user.getAuthoredRecipes(),
                user.getSavedRecipes(),
                user.getRatedRecipes(),
                user.getOwnedIngredients(),
                user.getShoppingList());
    }

    public UserForm(
            String username,
            String emailAddress,
            @Nullable List<@NotNull Recipe> authoredRecipes,
            @Nullable List<@NotNull Recipe> savedRecipes,
            @Nullable Map<@NotNull Recipe, @Nullable Double> ratedRecipes,
            @Nullable Set<@NotNull Ingredient> ownedIngredients,
            @Nullable Map<@NotNull Ingredient, @NotNull Double> shoppingList) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.authoredRecipes = Utils.allowNull(authoredRecipes, ArrayList::new);
        this.savedRecipes = Utils.allowNull(savedRecipes, ArrayList::new);
        this.ratedRecipes = Utils.allowNull(ratedRecipes, HashMap::new);
        this.ownedIngredients = Utils.allowNull(ownedIngredients, HashSet::new);
        this.shoppingList = Utils.allowNull(shoppingList, HashMap::new);
    }

    public String getUsername() {
        return username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<Recipe> getAuthoredRecipes() {
        return Utils.allowNull(authoredRecipes, Collections::unmodifiableList);
    }

    public List<Recipe> getSavedRecipes() {
        return Utils.allowNull(savedRecipes, Collections::unmodifiableList);
    }

    public Map<Recipe, Double> getRatedRecipes() {
        return Utils.allowNull(ratedRecipes, Collections::unmodifiableMap);
    }

    public Set<Ingredient> getOwnedIngredients() {
        return Utils.allowNull(ownedIngredients, Collections::unmodifiableSet);
    }

    public Map<Ingredient, Double> getShoppingList() {
        return Utils.allowNull(shoppingList, Collections::unmodifiableMap);
    }
}
