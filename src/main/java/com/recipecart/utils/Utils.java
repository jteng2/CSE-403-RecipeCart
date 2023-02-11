/* (C)2023 */
package com.recipecart.utils;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/** This class contains general utility methods to help with the implementation of RecipeCart. */
public class Utils {
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
}
