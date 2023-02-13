/* (C)2023 */
package com.recipecart.requests;

import com.recipecart.execution.EntityCommander;
import org.apache.commons.lang3.NotImplementedException;
import org.jetbrains.annotations.NotNull;

/**
 * This class takes requests from the front-end to do some use case, tells the Business Logic Layer
 * to perform that use case, then gives the front-end an output response.
 */
public class HttpRequestHandler {
    private final @NotNull EntityCommander commander;
    private final @NotNull LoginValidator loginChecker;

    /**
     * Creates a handler that sends its commands to the given EntityCommander and validates logins
     * with the given LoginValidator.
     *
     * @param commander where commands are executed
     * @param loginChecker where logins are verified
     */
    public HttpRequestHandler(
            @NotNull EntityCommander commander, @NotNull LoginValidator loginChecker) {
        this.commander = commander;
        this.loginChecker = loginChecker;
    }

    /** Gets this handler to start taking requests from the front-end. */
    public void startHandler() {
        throw new NotImplementedException();
    }
}
