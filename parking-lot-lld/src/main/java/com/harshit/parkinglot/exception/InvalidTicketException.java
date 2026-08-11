package com.harshit.parkinglot.exception;

/**
 * Custom exception when ticket id is wrong or already used.
 */
public class InvalidTicketException extends RuntimeException {

    public InvalidTicketException(String message) {
        super(message);
    }
}
