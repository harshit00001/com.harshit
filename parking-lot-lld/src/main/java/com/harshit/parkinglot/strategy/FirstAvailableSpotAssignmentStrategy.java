package com.harshit.parkinglot.strategy;

import com.harshit.parkinglot.enums.VehicleType;
import com.harshit.parkinglot.model.ParkingFloor;
import com.harshit.parkinglot.model.ParkingSpot;

import java.util.List;

/**
 * Simple assignment rule: pick the first compatible free spot
 * starting from floor 1 and moving forward.
 */
public class FirstAvailableSpotAssignmentStrategy implements SpotAssignmentStrategy {

    @Override
    public ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType vehicleType) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(vehicleType);
            if (spot != null) {
                return spot;
            }
        }
        return null;
    }
}
