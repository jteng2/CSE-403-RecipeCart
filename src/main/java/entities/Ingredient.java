/* (C)2023 */
package entities;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

/** This class represents an ingredient that a User can haveRecipe can require some quantity of */
public final class Ingredient {
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

    @Nullable public String getName() {
        return name;
    }

    @Nullable public String getUnits() {
        return units;
    }

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
