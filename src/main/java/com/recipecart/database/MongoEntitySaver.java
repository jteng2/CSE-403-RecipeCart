/* (C)2023 */
package com.recipecart.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.ReplaceOptions;
import com.recipecart.entities.*;
import com.recipecart.storage.EntitySaver;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.NotImplementedException;
import org.bson.Document;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;

/**
 * This class represents a place to save various RecipeCart entities into a Mongo database (that
 * houses such entities). While this class isn't strictly write-only to the database, its intention
 * is purely to write entities to the database.
 */
public class MongoEntitySaver extends MongoConnector implements EntitySaver {

    /** {@inheritDoc} */
    public MongoEntitySaver(String filename) throws FileNotFoundException {
        super(filename);
    }

    /**
     * Saves the given Tags to the Mongo database this saver is connected to.
     *
     * @param tags Tags that need to be saved
     * @throws IllegalArgumentException if any names of the Tags are null
     */
    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        for (Tag tag : tags) {
            if (tag.getName() == null) {
                throw new IllegalArgumentException("Tag name cannot be null");
            }
            Document doc = tagToDocument(tag);
            getTagCollection()
                    .replaceOne(
                            new Document("_id", doc.get("_id")),
                            doc,
                            new ReplaceOptions().upsert(true));
        }
    }

    /**
     * Saves the given Ingredients to the Mongo database this saver is connected to.
     *
     * @param ingredients Ingredients that need to be saved
     * @throws IllegalArgumentException if any names of the Ingredients are null
     */
    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getName() == null) {
                throw new IllegalArgumentException("Ingredient name cannot be null");
            }
            Document doc = ingredientToDocument(ingredient);
            getIngredientCollection()
                    .replaceOne(
                            new Document("_id", doc.get("_id")),
                            doc,
                            new ReplaceOptions().upsert(true));
        }
    }

    /**
     * Saves the given Recipes to the Mongo database this saver is connected to.
     *
     * @param recipes Recipes that need to be saved
     * @throws IllegalArgumentException if any (non-presentation) names of the Recipes are null
     */
    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        for (Recipe recipe : recipes) {
            if (recipe.getName() == null) {
                throw new IllegalArgumentException("Recipe name cannot be null");
            }
            Document doc = recipeToDocument(recipe);
            getRecipeCollection()
                    .replaceOne(
                            new Document("_id", doc.get("_id")),
                            doc,
                            new ReplaceOptions().upsert(true));
        }
    }

    /**
     * Saves the given Users to the Mongo database this saver is connected to.
     *
     * @param users Users that need to be saved
     * @throws IllegalArgumentException if any usernames of the Users are null
     */
    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        for (User user : users) {
            if (user.getUsername() == null) {
                throw new IllegalArgumentException("Username cannot be null");
            }
            Document doc = userToDocument(user);
            getUserCollection()
                    .replaceOne(
                            new Document("_id", doc.get("_id")),
                            doc,
                            new ReplaceOptions().upsert(true));
        }
    }

    // possible helper functions that can help with the implementation of EntitySaver

    private Document tagToDocument(@NotNull Tag tag) {
        Document doc = new Document();
        ObjectId id = tag.getName() != null ? new ObjectId(tag.getName()) : new ObjectId();
        doc.put("_id", id);
        doc.put("name", tag.getName());
        return doc;
    }

    private Document ingredientToDocument(@NotNull Ingredient ingredient) {
        Document doc = new Document();
        ObjectId id =
                ingredient.getName() != null ? new ObjectId(ingredient.getName()) : new ObjectId();
        doc.put("_id", id);
        doc.put("name", ingredient.getName());
        doc.put("unit", ingredient.getUnits());
        return doc;
    }

    private Document recipeToDocument(@NotNull Recipe recipe) {
        Document doc = new Document();
        ObjectId id = recipe.getName() != null ? new ObjectId(recipe.getName()) : new ObjectId();
        doc.put("_id", id);
        doc.put("name", recipe.getName());
        doc.put("description", recipe.getPresentationName());
        List<Document> ingredientsDocs = new ArrayList<>();
        for (Ingredient ingredient : recipe.getRequiredIngredients().keySet()) {
            ObjectId ingredientId = ingredientToId(ingredient);
            Document ingredientDoc =
                    new Document("_id", ingredientId).append("quantity", ingredient.getUnits());
            ingredientsDocs.add(ingredientDoc);
        }
        doc.put("ingredients", ingredientsDocs);
        List<String> tagNames = new ArrayList<>();
        for (Tag tag : recipe.getTags()) {
            tagNames.add(tag.getName());
        }
        doc.put("tags", tagNames);
        doc.put("instructions", recipe.getDirections());
        doc.put("prepTime", recipe.getPrepTime());
        doc.put("cookTime", recipe.getCookTime());
        doc.put("servings", recipe.getNumServings());
        doc.put("imageUrl", recipe.getImageUri());
        return doc;
    }

    private Document userToDocument(@NotNull User user) {
        Document doc = new Document();
        ObjectId id =
                user.getUsername() != null ? new ObjectId(user.getUsername()) : new ObjectId();
        doc.put("_id", id);
        doc.put("username", user.getUsername());
        doc.put("email", user.getEmailAddress());
        doc.put("name", user.getUsername());
        return doc;
    }

    private @NotNull ObjectId tagToId(@NotNull Tag tags) {
        throw new NotImplementedException();
    }

    private @NotNull ObjectId ingredientToId(@NotNull Ingredient ingredient) {
        MongoCollection<Document> ingredients = getIngredientCollection();
        Document query = new Document("name", ingredient.getName());
        Document result = ingredients.find(query).first();
        if (result != null) {
            return result.getObjectId("_id");
        } else {
            return new ObjectId();
        }
    }

    private @NotNull ObjectId recipeToId(@NotNull Recipe recipe) {
        throw new NotImplementedException();
    }

    private ObjectId userToId(@NotNull User user) {
        if (user.getUsername() == null) {
            throw new IllegalArgumentException("Username cannot be null");
        }
        Document doc =
                getUserCollection().find(new Document("username", user.getUsername())).first();
        if (doc == null) {
            throw new IllegalArgumentException(
                    "User with username " + user.getUsername() + " not found");
        }
        return doc.getObjectId("_id");
    }
}
