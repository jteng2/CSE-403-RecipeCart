/* (C)2023 */
package com.recipecart.storage;

import org.jetbrains.annotations.NotNull;

/**
 * This class represents an abstract "storage" where other objects in the Business Logic Layer go to
 * save and load entities. This saving and loading are implemented in the implementation of
 * EntitySaver and EntityLoader.
 *
 * <p>Invariant: If an object x representing an entity gets saved (i.e. through the given
 * EntitySaver), then when an object y representing the same entity gets loaded (i.e. through the
 * given EntityLoader), then x.equals(y) (where x is the most recently saved representation of the
 * entity). This, any EntitySaver or EntityLoader passed in, and their implementers are responsible
 * for upholding this invariant.
 *
 * <p>Invariant: An EntityStorage cannot store 2 different entities (of the same type (i.e. two
 * different Recipes)) with the same unique identifier. The "unique identifier" used by
 * EntityStorage is, for each entity type, one of their fields:
 *
 * <ul>
 *   <li><code>Tag</code>: <code>name</code> (from the <code>getName()</code> method)
 *   <li><code>Ingredient</code>: <code>name</code> (from the <code>getName()</code> method)
 *   <li><code>Recipe</code>: <code>name</code> (from the <code>getName()</code> method)
 *   <li><code>User</code>: <code>username</code> (from the <code>getUsername()</code> method)
 * </ul>
 *
 * That is, for all entities <code>x</code> and <code>y</code> stored by a given EntityStorage,
 *
 * <p><code>!(uid(x).equals(uid(y)) && x.getClass().equals(y.getClass())) || x.equals(y)</code>
 *
 * <p>(note: <code>uid(x)</code> returns the unique identifier of <code>x</code>, and <code>x</code>
 * and <code>y</code> are abstract and don't have to currently be existing in RAM). This, any
 * EntitySaver or EntityLoader passed in, and their implementers are responsible for upholding this
 * invariant.
 *
 * <p>If EntityStorage is storing an entity and is asked to store an entity (of the same type) with
 * the same unique identifier, then the original entity gets replaced. Two entities with the same
 * unique identifier (and type) can exist in RAM, but only one of them can be currently saved in a
 * given EntityStorage at any given time.
 */
public class EntityStorage {
    private final @NotNull EntitySaver saver;
    private final @NotNull EntityLoader loader;

    /**
     * Creates an EntityStorage that saves and loads entities in the way specified by the
     * implementers of EntitySaver and EntityLoader
     *
     * @param saver what will be used for saving entities
     * @param loader what will be used for loading entities
     */
    public EntityStorage(@NotNull EntitySaver saver, @NotNull EntityLoader loader) {
        this.saver = saver;
        this.loader = loader;
    }

    /**
     * @return the object used for saving entities, for this EntityStorage
     */
    @NotNull public EntitySaver getSaver() {
        return saver;
    }

    /**
     * @return the object used for loading entities, for this EntityStorage
     */
    @NotNull public EntityLoader getLoader() {
        return loader;
    }
}
