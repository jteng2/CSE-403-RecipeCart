/* (C)2023 */
package entities;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

/**
 * This class represents an immutable tag that many Recipes can have. Two Recipes can have a tag in
 * common if they have a Tag with the same name, even if the Tags aren't equal by reference.
 */
public final class Tag {
    // EntityStorage's "unique identifier" for Tag
    private final @Nullable String name;

    /**
     * Constructs a Tag with the given name
     *
     * @param name the name associated with this Tag
     */
    public Tag(@Nullable String name) {
        this.name = name;
    }

    @Nullable public String getName() {
        return name;
    }

    @Override
    public @Nullable String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tag tag = (Tag) o;
        return Objects.equals(getName(), tag.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName());
    }
}
