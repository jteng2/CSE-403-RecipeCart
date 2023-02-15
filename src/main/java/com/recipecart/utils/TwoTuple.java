/* (C)2023 */
package com.recipecart.utils;

/**
 * A generic two-tuple that's assignment-immutable (doesn't protect from mutating mutable elements).
 */
public final class TwoTuple<T, U> {
    private final T first;
    private final U second;

    public TwoTuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }
}
