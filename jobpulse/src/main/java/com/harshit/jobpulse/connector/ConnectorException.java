package com.harshit.jobpulse.connector;

/** Raised when an upstream career page cannot be fetched or parsed. */
public class ConnectorException extends RuntimeException {

    public ConnectorException(String message) {
        super(message);
    }

    public ConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
