/* (C)2023 */
package com.recipecart;

import com.recipecart.database.MockEntitySaveAndLoader;
import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.execution.EntityCommander;
import com.recipecart.requests.HttpRequestHandler;
import com.recipecart.requests.JwtValidator;
import com.recipecart.storage.EntityStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Main {
    public static final int PORT = 4567;

    public static void main(String[] args) {
        MockEntitySaveAndLoader saveAndLoader = new MockEntitySaveAndLoader();
        putInMockData(saveAndLoader);

        EntityStorage storage = new EntityStorage(saveAndLoader, saveAndLoader);
        EntityCommander commander = new EntityCommander(storage);
        JwtValidator validator = new JwtValidator();
        HttpRequestHandler requestHandler = new HttpRequestHandler(commander, validator, PORT);

        requestHandler.startHandler();
    }

    private static void putInMockData(MockEntitySaveAndLoader saveAndLoader) {
        List<Tag> tags = new ArrayList<>();
        for (String s : List.of("Tag1", "Tag2", "Tag3", "Tag4", "Tag5")) {
            tags.add(new Tag(s));
        }

        saveAndLoader.updateTags(tags);

        List<Ingredient> igs =
                List.of(
                        new Ingredient("Ingredient1", "Units1", "ImageUri1"),
                        new Ingredient("Ingredient2", "Units2", "ImageUri2"),
                        new Ingredient("Ingredient3", "Units3", "ImageUri3"),
                        new Ingredient("Ingredient4", "Units4", "ImageUri4"),
                        new Ingredient("Ingredient5", "Units5", "ImageUri5"));

        saveAndLoader.updateIngredients(igs);

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

        saveAndLoader.updateRecipes(List.of(recipe1, recipe2, recipe3));

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

        saveAndLoader.updateUsers(List.of(user1, user2));
    }
}
