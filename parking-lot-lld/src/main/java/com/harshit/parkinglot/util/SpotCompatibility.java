package com.harshit.parkinglot.util;

import com.harshit.parkinglot.enums.SpotType;
import com.harshit.parkinglot.enums.VehicleType;

/**
 * Business rule helper: which spot can fit which vehicle?
 *
 * Rules used in this project:
 * - Motorcycle can use any spot (compact or large)
 * - Car can use compact or large
 * - Truck can use large spot only
 *
 * Keeping this logic in one class follows Single Responsibility Principle.
 */
public final class SpotCompatibility {

    private SpotCompatibility() {
    }

    public static boolean canFit(VehicleType vehicleType, SpotType spotType) {
        switch (vehicleType) {
            case MOTORCYCLE:
                return true;
            case CAR:
                return spotType == SpotType.COMPACT || spotType == SpotType.LARGE;
            case TRUCK:
                return spotType == SpotType.LARGE;
            default:
                return false;
        }
    }
}
