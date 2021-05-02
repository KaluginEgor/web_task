package com.epam.project.exception;

/**
 * The type Connection exception.
 */
public class ConnectionException extends Exception {
    /**
     * Instantiates a new Connection exception.
     */
    public ConnectionException() {}

    /**
     * Instantiates a new Connection exception.
     *
     * @param message the message
     */
    public ConnectionException(String message) {
        super(message);
    }

    /**
     * Instantiates a new Connection exception.
     *
     * @param message the message
     * @param cause   the cause
     */
    public ConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Instantiates a new Connection exception.
     *
     * @param cause the cause
     */
    public ConnectionException(Throwable cause) {
        super(cause);
    }
}
