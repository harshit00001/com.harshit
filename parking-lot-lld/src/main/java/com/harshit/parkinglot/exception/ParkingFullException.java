package com.harshit.parkinglot.exception;

/**
 * Custom exception when parking lot has no free compatible spot.
 *
 * Interview tip: use specific exceptions instead of generic RuntimeException
 * so callers know exactly what went wrong.
 */
public class ParkingFullException extends RuntimeException {

    public ParkingFullException(String message) {
        super(message);
    }
}
