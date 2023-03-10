/* (C)2023 */
package com.recipecart.utils;

import com.recipecart.entities.*;
import com.recipecart.storage.EntitySaver;
import java.util.*;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** This class contains general helper methods to help with the implementation of RecipeCart. */
public class Utils {
    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Methods for dealing with nulls //////////////////////////////////
    /**
     * Passes the given T into the given function if the T isn't null; returns null otherwise
     *
     * @param mayBeNull an object that might be null
     * @param function a function (that usually doesn't accept nulls) to pass the non-null T into
     * @param <T> , and what the function can take as input
     * @param <U> the function's output type
     * @return the output of the function applied to the T if it isn't null; null otherwise
     */
    public static @Nullable <T, U> U allowNull(
            @Nullable T mayBeNull, Function<? super T, ? extends U> function) {
        return mayBeNull == null ? null : function.apply(mayBeNull);
    }

    /**
     * Throws an exception if the given collection is null or has null values.
     *
     * @param collection the collection to null-check
     * @param collectionNullMessage the exception's message if the collection is null
     * @param elementNullMessage the exception's message if an element is null
     * @param <T> the element type of the collection
     * @throws NullPointerException if there are any nulls
     */
    public static <T> void requireAllNotNull(
            Collection<T> collection, String collectionNullMessage, String elementNullMessage) {
        Objects.requireNonNull(collection, collectionNullMessage);
        for (T element : collection) {
            Objects.requireNonNull(element, elementNullMessage);
        }
    }

    /**
     * Throws an exception if the given map is null or has null values.
     *
     * @param map the map to null-check
     * @param mapNullMessage the exception's message if the map is null
     * @param keyNullMessage the exception's message if a key is null
     * @param valueNullMessage the exception's message if a value is null
     * @param <K> the key type of the map
     * @param <V> the value type of the map
     * @throws NullPointerException if there are any nulls
     */
    public static <K, V> void requireAllMapNotNull(
            Map<K, V> map, String mapNullMessage, String keyNullMessage, String valueNullMessage) {
        Objects.requireNonNull(map, mapNullMessage);
        for (Map.Entry<K, V> entry : map.entrySet()) {
            Objects.requireNonNull(entry.getKey(), keyNullMessage);
            Objects.requireNonNull(entry.getValue(), valueNullMessage);
        }
    }

    /**
     * Throws an exception if any tag names are null.
     *
     * @param tags the Tags to check
     * @throws IllegalArgumentException if any tags have null names
     */
    public static void nullCheckTagNames(Collection<Tag> tags) {
        for (Tag t : tags) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Tag names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given tag has a null name.
     *
     * @param tag the Tag to null-check
     * @throws IllegalArgumentException if the tag is null
     */
    public static void nullCheckTagName(Tag tag) {
        nullCheckTagNames(Collections.singletonList(tag));
    }

    /**
     * Throws an exception if any ingredient names are null.
     *
     * @param ingredients the Ingredients to check
     * @throws IllegalArgumentException if any ingredients have null names
     */
    public static void nullCheckIngredientNames(Collection<Ingredient> ingredients) {
        for (Ingredient t : ingredients) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Ingredient names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given ingredient has a null name.
     *
     * @param ingredient the Ingredient to null-check
     * @throws IllegalArgumentException if the ingredient is null
     */
    public static void nullCheckIngredientName(Ingredient ingredient) {
        nullCheckIngredientNames(Collections.singletonList(ingredient));
    }

    /**
     * Throws an exception if any recipe names are null.
     *
     * @param recipes the Recipes to check
     * @throws IllegalArgumentException if any recipes have null names
     */
    public static void nullCheckRecipeNames(Collection<Recipe> recipes) {
        for (Recipe t : recipes) {
            if (t.getName() == null) {
                throw new IllegalArgumentException("Recipe names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given recipe has a null name.
     *
     * @param recipe the Recipe to null-check
     * @throws IllegalArgumentException if the recipe is null
     */
    public static void nullCheckRecipeName(Recipe recipe) {
        nullCheckRecipeNames(Collections.singletonList(recipe));
    }

    /**
     * Throws an exception if any usernames are null.
     *
     * @param users the Users to check
     * @throws IllegalArgumentException if any users have null names
     */
    public static void nullCheckUserNames(Collection<User> users) {
        for (User t : users) {
            if (t.getUsername() == null) {
                throw new IllegalArgumentException("User names cannot be null");
            }
        }
    }

    /**
     * Throws an exception if the given user has a null name.
     *
     * @param user the User to null-check
     * @throws IllegalArgumentException if the user is null
     */
    public static void nullCheckUserName(User user) {
        nullCheckUserNames(Collections.singletonList(user));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////// Methods for renaming entities ///////////////////////////////////

    @SuppressWarnings("unused") // currently, newName is the only field of tag
    public static Tag renameTag(@NotNull Tag tag, String newName) {
        return new Tag(newName);
    }

    public static Ingredient renameIngredient(Ingredient ingredient, String toRename) {
        return new Ingredient(toRename, ingredient.getUnits(), ingredient.getImageUri());
    }

    public static Recipe renameRecipe(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setName(toRename).build();
    }

    public static Recipe renameRecipePresentationName(Recipe recipe, String toRename) {
        return new Recipe.Builder(recipe).setPresentationName(toRename).build();
    }

    public static Recipe renameRecipeFull(
            Recipe recipe, String toRename, String toRenamePresentation) {
        return new Recipe.Builder(recipe)
                .setName(toRename)
                .setPresentationName(toRenamePresentation)
                .build();
    }

    public static User renameUser(User User, String toRename) {
        return new User.Builder(User).setUsername(toRename).build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////// Methods for converting entity data structures ////////////////////////////

    public static Set<String> fromTagSet(Set<Tag> tags) {
        Set<String> tagNames = new HashSet<>();
        tags.forEach((tag) -> tagNames.add(tag.getName()));
        return tagNames;
    }

    public static Set<String> fromIngredientSet(Set<Ingredient> ingredients) {
        Set<String> ingredientNames = new HashSet<>();
        ingredients.forEach((ingredient) -> ingredientNames.add(ingredient.getName()));
        return ingredientNames;
    }

    public static List<String> fromRecipeList(List<Recipe> recipes) {
        List<String> recipeNames = new ArrayList<>();
        recipes.forEach((recipe) -> recipeNames.add(recipe.getName()));
        return recipeNames;
    }

    public static <T> Map<String, T> fromIngredientMap(Map<Ingredient, ? extends T> ingredientMap) {
        Map<String, T> ingredientNameMap = new HashMap<>();
        ingredientMap.forEach(
                (ingredient, value) -> ingredientNameMap.put(ingredient.getName(), value));
        return ingredientNameMap;
    }

    public static <T> Map<String, T> fromRecipeMap(Map<Recipe, ? extends T> recipeMap) {
        Map<String, T> recipeNameMap = new HashMap<>();
        recipeMap.forEach((recipe, value) -> recipeNameMap.put(recipe.getName(), value));
        return recipeNameMap;
    }

    public static <T> Map<T, RecipeForm> toRecipeFormMap(Map<? extends T, Recipe> recipeMap) {
        Map<T, RecipeForm> recipeFormMap = new HashMap<>();
        recipeMap.forEach((key, recipe) -> recipeFormMap.put(key, new RecipeForm(recipe)));
        return recipeFormMap;
    }

    public static <T> Map<T, UserForm> toUserFormMap(Map<? extends T, User> userMap) {
        Map<T, UserForm> userFormMap = new HashMap<>();
        userMap.forEach((key, user) -> userFormMap.put(key, new UserForm(user)));
        return userFormMap;
    }

    public static Collection<RecipeForm> fromRecipes(Collection<Recipe> recipes) {
        Collection<RecipeForm> forms = new ArrayList<>();
        recipes.forEach(recipe -> forms.add(new RecipeForm(recipe)));
        return forms;
    }

    public static Collection<UserForm> fromUsers(Collection<User> users) {
        Collection<UserForm> forms = new ArrayList<>();
        users.forEach(user -> forms.add(new UserForm(user)));
        return forms;
    }

    public static Recipe fromRecipeForm(
            RecipeForm form, Map<String, Tag> allTags, Map<String, Ingredient> allIngredients) {
        List<String> directions =
                form.getDirections() == null ? Collections.emptyList() : form.getDirections();

        Set<String> tagNames =
                form.getTagNames() == null ? Collections.emptySet() : form.getTagNames();
        Set<Tag> tags = new HashSet<>();
        tagNames.forEach((key) -> tags.add(allTags.get(key)));

        Map<String, Double> ingredientNames =
                form.getRequiredIngredients() == null
                        ? Collections.emptyMap()
                        : form.getRequiredIngredients();
        Map<Ingredient, Double> ingredients = new HashMap<>();
        ingredientNames.forEach((name, value) -> ingredients.put(allIngredients.get(name), value));

        return new Recipe.Builder()
                .setName(form.getName())
                .setPresentationName(form.getPresentationName())
                .setAuthorUsername(form.getAuthorUsername())
                .setPrepTime(form.getPrepTime())
                .setCookTime(form.getCookTime())
                .setImageUri(form.getImageUri())
                .setNumServings(form.getNumServings())
                .setAvgRating(form.getAvgRating())
                .setNumRatings(form.getNumRatings())
                .setDirections(directions)
                .setTags(tags)
                .setRequiredIngredients(ingredients)
                .build();
    }

    public static User fromUserForm(
            UserForm form, Map<String, Ingredient> allIngredients, Map<String, Recipe> allRecipes) {
        List<String> authoredRecipeNames =
                form.getAuthoredRecipes() == null
                        ? Collections.emptyList()
                        : form.getAuthoredRecipes();
        List<Recipe> authoredRecipes = new ArrayList<>();
        authoredRecipeNames.forEach((name) -> authoredRecipes.add(allRecipes.get(name)));

        List<String> savedRecipeNames =
                form.getSavedRecipes() == null ? Collections.emptyList() : form.getSavedRecipes();
        List<Recipe> savedRecipes = new ArrayList<>();
        savedRecipeNames.forEach((name) -> savedRecipes.add(allRecipes.get(name)));

        Map<String, Double> ratedRecipeNames =
                form.getRatedRecipes() == null ? Collections.emptyMap() : form.getRatedRecipes();
        Map<Recipe, Double> ratedRecipes = new HashMap<>();
        ratedRecipeNames.forEach((name, value) -> ratedRecipes.put(allRecipes.get(name), value));

        Set<String> ownedIngredientNames =
                form.getOwnedIngredients() == null
                        ? Collections.emptySet()
                        : form.getOwnedIngredients();
        Set<Ingredient> ownedIngredients = new HashSet<>();
        ownedIngredientNames.forEach((name) -> ownedIngredients.add(allIngredients.get(name)));

        Map<String, Double> shoppingListNames =
                form.getShoppingList() == null ? Collections.emptyMap() : form.getShoppingList();
        Map<Ingredient, Double> shoppingList = new HashMap<>();
        shoppingListNames.forEach(
                (name, value) -> shoppingList.put(allIngredients.get(name), value));

        return new User.Builder()
                .setUsername(form.getUsername())
                .setEmailAddress(form.getEmailAddress())
                .setAuthoredRecipes(authoredRecipes)
                .setSavedRecipes(savedRecipes)
                .setRatedRecipes(ratedRecipes)
                .setOwnedIngredients(ownedIngredients)
                .setShoppingList(shoppingList)
                .build();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////// Misc. methods ////////////////////////////////////////////////

    public static boolean isNumber(String string) {
        return string.matches("\\d+");
    }

    public static <T> Map<T, Double> addMaps(Map<T, Double> map1, Map<T, Double> map2) {
        Map<T, Double> sum = new HashMap<>(map1);
        for (Map.Entry<T, Double> entry : map2.entrySet()) {
            sum.putIfAbsent(entry.getKey(), 0.0);
            sum.put(entry.getKey(), sum.get(entry.getKey()) + entry.getValue());
        }
        return sum;
    }

    public static void putInMockData(EntitySaver saver) {
        List<Tag> tags = new ArrayList<>();
        for (String s : List.of("Tag1", "Tag2", "Tag3", "Tag4", "Tag5")) {
            tags.add(new Tag(s));
        }

        saver.updateTags(tags);

        List<Ingredient> igs =
                List.of(
                        new Ingredient("Ingredient1", "Units1", "ImageUri1"),
                        new Ingredient("Ingredient2", "Units2", "ImageUri2"),
                        new Ingredient("Ingredient3", "Units3", "ImageUri3"),
                        new Ingredient("Ingredient4", "Units4", "ImageUri4"),
                        new Ingredient("Ingredient5", "Units5", "ImageUri5"));

        saver.updateIngredients(igs);

        String author1 = "Author1", author2 = "Author2";

        Recipe recipe1 =
                new Recipe.Builder()
                        .setName("Recipe1")
                        .setPresentationName("First recipe x y")
                        .setAuthorUsername(author1)
                        .setPrepTime(60)
                        .setCookTime(10)
                        .setDirections(
                                List.of("Preheat oven to 475", "Preheat oven to 300", "etc."))
                        .build();

        Recipe recipe2 =
                new Recipe.Builder()
                        .setName("Recipe2")
                        .setPresentationName("Second recipe y z")
                        .setAuthorUsername(author2)
                        .setImageUri("ImageUri4")
                        .setNumServings(10)
                        .setTags(Set.of(tags.get(0), tags.get(1), tags.get(4)))
                        .build();

        Recipe recipe3 =
                new Recipe.Builder()
                        .setName("Recipe3")
                        .setPresentationName("Third recipe x z")
                        .setAuthorUsername(author1)
                        .setAvgRating(4.9)
                        .setNumRatings(100)
                        .setRequiredIngredients(Map.of(igs.get(1), .03, igs.get(3), 5.0))
                        .build();

        saver.updateRecipes(List.of(recipe1, recipe2, recipe3));

        User user1 =
                new User.Builder()
                        .setUsername(author1)
                        .setAuthoredRecipes(List.of(recipe1, recipe3))
                        .build();

        User user2 =
                new User.Builder()
                        .setUsername(author2)
                        .setAuthoredRecipes(List.of(recipe2))
                        .build();

        saver.updateUsers(List.of(user1, user2));
    }
}
