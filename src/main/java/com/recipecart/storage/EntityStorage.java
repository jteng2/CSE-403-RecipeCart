/* (C)2023 */
package com.recipecart.storage;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents an abstract "storage" where other objects in the Business Logic Layer go to
 * save and load entities. This saving and loading are implemented in the implementation of
 * EntityWriter and EntityReader.
 *
 * <p>Invariant: If an object x representing an entity gets saved (i.e. through
 * EntityUpdateListener), then when an object y representing the same entity gets loaded (i.e.
 * through EntityReader), then x.equals(y) (where x is the most recently saved representation of the
 * entity). This, any EntityWriter or EntityReader passed in, and their implementers are responsible
 * for upholding this invariant.
 */
public class EntityStorage {
    private final @NotNull EntityWriter saver;
    private final @NotNull EntityReader loader;

    /**
     * Creates an EntityStorage that saves and loads entities in the way specified by the
     * implementers of EntityWriter and EntitySaver
     *
     * @param saver what will be used for saving entities
     * @param loader what will be used for loading entities
     */
    public EntityStorage(@NotNull EntityWriter saver, @NotNull EntityReader loader) {
        this.saver = saver;
        this.loader = loader;
    }

    /** @return the object used for saving entities, for this EntityStorage */
    @NotNull public EntityWriter getSaver() {
        return saver;
    }

    /** @return the object used for loading entities, for this EntityStorage */
    @NotNull public EntityReader getLoader() {
        return loader;
    }
}
