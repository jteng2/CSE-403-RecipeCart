/* (C)2023 */
package com.recipecart.requests;

import org.apache.commons.lang3.NotImplementedException;

/** This class represents a conditional of whether a login is valid. */
public class JwtValidator implements Validator<String> {
    /**
     * Checks if the given login information (in the given encrypted JWT) is valid.
     *
     * @return true if the login is valid, false otherwise.
     */
    @Override
    public boolean checkValidity(String encryptedJwt) {
        throw new NotImplementedException();
    }
}
