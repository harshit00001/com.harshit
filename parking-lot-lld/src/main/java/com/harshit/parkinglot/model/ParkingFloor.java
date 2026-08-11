package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.VehicleType;

import java.util.ArrayList;
import java.util.List;

/**
 * One floor inside the parking lot.
 *
 * Responsibility: manage spots on that floor and find a free compatible spot.
 * ParkingLot delegates floor-level search to this class.
 */
public class ParkingFloor {

    private final int floorNumber;
    private final List<ParkingSpot> spots;

    public ParkingFloor(int floorNumber, List<ParkingSpot> spots) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>(spots);
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    public List<ParkingSpot> getSpots() {
        return spots;
    }

    /**
     * Returns first available spot that can fit the given vehicle type.
     * Returns null if no spot is free on this floor.
     */
    public ParkingSpot findAvailableSpot(VehicleType vehicleType) {
        for (ParkingSpot spot : spots) {
            if (spot.isAvailable() && spot.canFit(vehicleType)) {
                return spot;
            }
        }
        return null;
    }

    public long countAvailableSpots() {
        return spots.stream().filter(ParkingSpot::isAvailable).count();
    }

    @Override
    public String toString() {
        return "Floor " + floorNumber + " -> available spots: " + countAvailableSpots() + "/" + spots.size();
    }
}
