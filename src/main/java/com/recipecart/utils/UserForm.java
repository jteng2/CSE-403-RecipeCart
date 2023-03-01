/* (C)2023 */
package com.recipecart.utils;

import com.recipecart.entities.User;
import java.io.Serializable;
import java.util.*;
import org.jetbrains.annotations.Nullable;

/**
 * This class is a version of User where references to other entities are replaced with Strings
 * representing their (unique) names. Creating instances of this class is mainly done through
 * deserialization.
 */
public final class UserForm implements Serializable {
    private final String username; // EntityStorage's "unique identifier" for User
    private final String emailAddress;
    private final @Nullable List<String> authoredRecipes;
    private final @Nullable List<String> savedRecipes;
    private final @Nullable Map<String, Double> ratedRecipes;
    private final @Nullable Set<String> ownedIngredients;
    private final @Nullable Map<String, Double> shoppingList;

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
                Utils.fromRecipeList(user.getAuthoredRecipes()),
                Utils.fromRecipeList(user.getSavedRecipes()),
                Utils.fromRecipeMap(user.getRatedRecipes()),
                Utils.fromIngredientSet(user.getOwnedIngredients()),
                Utils.fromIngredientMap(user.getShoppingList()));
    }

    public UserForm(
            String username,
            String emailAddress,
            @Nullable List<String> authoredRecipes,
            @Nullable List<String> savedRecipes,
            @Nullable Map<String, Double> ratedRecipes,
            @Nullable Set<String> ownedIngredients,
            @Nullable Map<String, Double> shoppingList) {
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

    public List<String> getAuthoredRecipes() {
        return Utils.allowNull(authoredRecipes, Collections::unmodifiableList);
    }

    public List<String> getSavedRecipes() {
        return Utils.allowNull(savedRecipes, Collections::unmodifiableList);
    }

    public Map<String, Double> getRatedRecipes() {
        return Utils.allowNull(ratedRecipes, Collections::unmodifiableMap);
    }

    public Set<String> getOwnedIngredients() {
        return Utils.allowNull(ownedIngredients, Collections::unmodifiableSet);
    }

    public Map<String, Double> getShoppingList() {
        return Utils.allowNull(shoppingList, Collections::unmodifiableMap);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserForm userForm = (UserForm) o;
        return Objects.equals(getUsername(), userForm.getUsername())
                && Objects.equals(getEmailAddress(), userForm.getEmailAddress())
                && Objects.equals(getAuthoredRecipes(), userForm.getAuthoredRecipes())
                && Objects.equals(getSavedRecipes(), userForm.getSavedRecipes())
                && Objects.equals(getRatedRecipes(), userForm.getRatedRecipes())
                && Objects.equals(getOwnedIngredients(), userForm.getOwnedIngredients())
                && Objects.equals(getShoppingList(), userForm.getShoppingList());
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
}
