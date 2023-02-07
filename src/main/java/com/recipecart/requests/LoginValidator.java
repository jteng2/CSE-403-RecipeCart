package com.recipecart.requests;

import org.apache.commons.lang3.NotImplementedException;

/** This class represents a conditional of whether a login is valid. */
public class LoginValidator implements Validator {
    // implement some state about the login here (do NOT add parameters to checkValidity)

    /**
     * Checks if the given login is valid.
     *
     * @return true if the login is valid, false otherwise.
     */
    @Override
    public boolean checkValidity() {
        throw new NotImplementedException();
    }
}
