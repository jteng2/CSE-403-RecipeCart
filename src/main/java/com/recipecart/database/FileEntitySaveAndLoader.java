/* (C)2023 */
package com.recipecart.database;

import com.recipecart.entities.Ingredient;
import com.recipecart.entities.Recipe;
import com.recipecart.entities.Tag;
import com.recipecart.entities.User;
import com.recipecart.utils.RecipeForm;
import com.recipecart.utils.UserForm;
import com.recipecart.utils.Utils;
import java.io.*;
import java.util.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This class expands upon MapEntitySaveAndLoader's functionality by being able to save/load the
 * entities to/from a file.
 */
public class FileEntitySaveAndLoader extends MapEntitySaveAndLoader {
    private final @Nullable String autosaveFilename;
    private final @Nullable Integer maxSaveCounter;

    private final Object saveCounterLock = new Object();
    private Integer saveCounter;

    /**
     * Creates a FileEntitySaveAndLoader that starts off with no contents. This instance will
     * autosave its contents to the given file every saveCounter method calls of an EntitySaver
     * method.
     *
     * @param maxSaveCounter the number of EntitySaver method calls required to autosave
     * @throws IllegalArgumentException if saveCounter is negative
     */
    public FileEntitySaveAndLoader(
            @NotNull String autosaveFilename, @NotNull Integer maxSaveCounter) {
        super();

        Objects.requireNonNull(autosaveFilename);
        Objects.requireNonNull(maxSaveCounter);
        if (maxSaveCounter < 0) {
            throw new IllegalArgumentException("Max save counter cannot be negative");
        }

        this.autosaveFilename = autosaveFilename;
        this.maxSaveCounter = maxSaveCounter;
        this.saveCounter = 0;
    }

    /**
     * Creates a FileEntitySaveAndLoader that starts off with no contents. This instance will do no
     * autosaving.
     */
    public FileEntitySaveAndLoader() {
        super();
        this.autosaveFilename = null;
        this.maxSaveCounter = null;
        this.saveCounter = 0;
    }

    /**
     * @return the file this instance autosaves to, or null if this instance doesn't do autosaving.
     */
    @Nullable public String getAutosaveFilename() {
        return autosaveFilename;
    }

    private EntityFile loadFromStream(@NotNull InputStream stream)
            throws IOException, ClassNotFoundException {
        ObjectInputStream objectReader = new ObjectInputStream(stream);
        EntityFile fileObject = (EntityFile) objectReader.readObject();
        objectReader.close();

        return fileObject;
    }

    private void loadState(EntityFile stateToLoad) {
        getSavedTags().clear();
        getSavedTags().putAll(stateToLoad.getTags());

        getSavedIngredients().clear();
        getSavedIngredients().putAll(stateToLoad.getIngredients());

        Map<String, Recipe> recipes = stateToLoad.getFromRecipeForms();
        getSavedRecipes().clear();
        getSavedRecipes().putAll(recipes);

        Map<String, User> user = stateToLoad.getFromUserForms(recipes);
        getSavedUsers().clear();
        getSavedUsers().putAll(user);
    }

    /**
     * Loads contents from the given stream (i.e. the output file from a previous save call from
     * some other FileEntitySaveAndLoader) into this FileEntitySaveAndLoader. The loaded contents
     * will overwrite the current contents of this FileEntitySaveAndLoader.
     *
     * @param stream the stream to read entity data from
     * @throws FileNotFoundException if the given file cannot be found, or if there's an error with
     *     reading from the file.
     * @throws ClassNotFoundException if the class of the serialized object from the stream can't be
     *     found.
     */
    public void load(@NotNull InputStream stream) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(stream);
        tagWriteLock.lock();
        ingredientWriteLock.lock();
        recipeWriteLock.lock();
        userWriteLock.lock();
        try {
            EntityFile stateToLoad = loadFromStream(stream);
            loadState(stateToLoad);
        } finally {
            userWriteLock.unlock();
            recipeWriteLock.unlock();
            ingredientWriteLock.unlock();
            tagWriteLock.unlock();
        }
    }

    /**
     * Loads the contents of the given file (i.e. the output file from a previous save call from
     * some other FileEntitySaveAndLoader) into this FileEntitySaveAndLoader. The loaded contents
     * will overwrite the current contents of this FileEntitySaveAndLoader.
     *
     * @param filename the file to load entities from
     * @throws FileNotFoundException if the given file cannot be found, or if there's an error with
     *     reading from the file.
     * @throws ClassNotFoundException if the class of the serialized object from the stream can't be
     *     found.
     */
    public void load(String filename) throws IOException, ClassNotFoundException {
        Objects.requireNonNull(filename);
        load(new FileInputStream(filename));
    }

    private EntityFile getCurrentState() {
        return new EntityFile(
                getSavedTags(),
                getSavedIngredients(),
                Utils.toRecipeFormMap(getSavedRecipes()),
                Utils.toUserFormMap(getSavedUsers()));
    }

    private void writeToStream(EntityFile state, OutputStream stream) throws IOException {
        ObjectOutputStream objectWriter = new ObjectOutputStream(stream);
        objectWriter.writeObject(state);
        objectWriter.flush();
        objectWriter.close();
    }

    /**
     * Saves the current contents of this instance to the given stream. The file's original contents
     * will be overwritten.
     *
     * @param stream the stream to write to
     * @throws IOException if there's an error with writing to the file.
     */
    public void save(@NotNull OutputStream stream) throws IOException {
        Objects.requireNonNull(stream);
        tagWriteLock.lock();
        ingredientWriteLock.lock();
        recipeWriteLock.lock();
        userWriteLock.lock();
        try {
            EntityFile state = getCurrentState();
            writeToStream(state, stream);
        } finally {
            userWriteLock.unlock();
            recipeWriteLock.unlock();
            ingredientWriteLock.unlock();
            tagWriteLock.unlock();
        }
    }

    /**
     * Saves the current contents of this instance to the given file. The file's original contents
     * will be overwritten.
     *
     * @param filename the name of the file to save to
     * @throws IOException if there's an error with writing to the file.
     */
    public void save(@NotNull String filename) throws IOException {
        Objects.requireNonNull(filename);
        save(new FileOutputStream(filename));
    }

    private void incrementSaveCounter() {
        if (maxSaveCounter != null && getAutosaveFilename() != null) {
            synchronized (saveCounterLock) {
                try {
                    if (Objects.equals(++saveCounter, maxSaveCounter)) {
                        save(getAutosaveFilename());
                        saveCounter = 0;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    saveCounter = Math.max(0, saveCounter - 1);
                }
            }
        }
    }

    @Override
    public void updateTags(@NotNull Collection<@NotNull Tag> tags) {
        super.updateTags(tags);
        incrementSaveCounter();
    }

    @Override
    public void updateIngredients(@NotNull Collection<@NotNull Ingredient> ingredients) {
        super.updateIngredients(ingredients);
        incrementSaveCounter();
    }

    @Override
    public void updateRecipes(@NotNull Collection<@NotNull Recipe> recipes) {
        super.updateRecipes(recipes);
        incrementSaveCounter();
    }

    @Override
    public void updateUsers(@NotNull Collection<@NotNull User> users) {
        super.updateUsers(users);
        incrementSaveCounter();
    }

    private static class EntityFile implements Serializable {
        private static final long serialVersionUID = 0xcafef00dL;

        private final @NotNull Map<String, Tag> tags;
        private final @NotNull Map<String, Ingredient> ingredients;
        private final @NotNull Map<String, RecipeForm> recipeForms;
        private final @NotNull Map<String, UserForm> userForms;

        public EntityFile(
                @NotNull Map<String, Tag> tags,
                @NotNull Map<String, Ingredient> ingredients,
                @NotNull Map<String, RecipeForm> recipes,
                @NotNull Map<String, UserForm> users) {
            this.tags = new HashMap<>(tags);
            this.ingredients = new HashMap<>(ingredients);
            this.recipeForms = new HashMap<>(recipes);
            this.userForms = new HashMap<>(users);
        }

        public Map<String, Tag> getTags() {
            return Collections.unmodifiableMap(tags);
        }

        public Map<String, Ingredient> getIngredients() {
            return Collections.unmodifiableMap(ingredients);
        }

        public Map<String, RecipeForm> getRecipeForms() {
            return Collections.unmodifiableMap(recipeForms);
        }

        public Map<String, UserForm> getUserForms() {
            return Collections.unmodifiableMap(userForms);
        }

        public Map<String, Recipe> getFromRecipeForms() {
            Map<String, Recipe> recipes = new HashMap<>();
            for (Map.Entry<String, RecipeForm> entry : recipeForms.entrySet()) {
                Recipe recipe = Utils.fromRecipeForm(entry.getValue(), tags, ingredients);
                recipes.put(entry.getKey(), recipe);
            }
            return recipes;
        }

        public Map<String, User> getFromUserForms(Map<String, Recipe> recipes) {
            Map<String, User> users = new HashMap<>();
            for (Map.Entry<String, UserForm> entry : userForms.entrySet()) {
                User user = Utils.fromUserForm(entry.getValue(), ingredients, recipes);
                users.put(entry.getKey(), user);
            }
            return users;
        }
    }
}
