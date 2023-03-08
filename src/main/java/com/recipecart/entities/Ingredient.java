/* (C)2023 */
package com.recipecart.entities;

import java.io.Serializable;
import java.util.Objects;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an immutable ingredient that many Users can have and many Recipes can
 * require
 */
public final class Ingredient implements Serializable {
    private final @Nullable String name; // EntityStorage's "unique identifier" for Ingredient
    private final @Nullable String units;
    private final @Nullable String imageUri;

    /**
     * Creates an Ingredient with the given information
     *
     * @param name the name of the ingredient
     * @param units what units of measurement this ingredient uses
     * @param imageUri a URI leading to an image of this ingredient
     */
    public Ingredient(@Nullable String name, @Nullable String units, @Nullable String imageUri) {
        this.name = name;
        this.units = units;
        this.imageUri = imageUri;
    }

    /**
     * @return the name of the Ingredient
     */
    @Nullable public String getName() {
        return name;
    }

    /**
     * @return what units of measurement this Ingredient uses
     */
    @Nullable public String getUnits() {
        return units;
    }

    /**
     * @return a URI leading to an image of this Ingredient
     */
    @Nullable public String getImageUri() {
        return imageUri;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient ing = (Ingredient) o;
        return Objects.equals(getName(), ing.getName())
                && Objects.equals(getUnits(), ing.getUnits())
                && Objects.equals(getImageUri(), ing.getImageUri());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUnits(), getImageUri());
    }
}
