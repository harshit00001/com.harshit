package com.harshit.parkinglot.enums;

/**
 * ENUM - fixed list of vehicle types.
 *
 * Why enum instead of String?
 * - Prevents typos like "CAR" vs "Car"
 * - Compiler checks valid values
 * - Good for interview: shows type-safe domain modeling
 */
public enum VehicleType {
    MOTORCYCLE,
    CAR,
    TRUCK
}
