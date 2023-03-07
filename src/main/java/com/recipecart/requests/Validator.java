/* (C)2023 */
package com.recipecart.requests;

/**
 * This interface represents a general conditional for decision-making: specifically, to check
 * whether something is valid.
 *
 * @param <T> the type of object to base validity decisions on
 */
public interface Validator<T> {

    /**
     * Checks the validity of the given parameter in the implementation of this interface.
     *
     * @param toValidate the object to check the validity of
     * @return true if valid, false otherwise
     */
    boolean checkValidity(T toValidate);
}
