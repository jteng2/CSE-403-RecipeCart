/* (C)2023 */
package com.recipecart.requests;

/**
 * This interface represents a conditional for decision-making: specifically, to check whether
 * something is valid.
 */
public interface Validator {

    /**
     * Checks the validity of something in the implementation of this interface.
     *
     * @return true if valid, false otherwise
     */
    boolean checkValidity();
}
