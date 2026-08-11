package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.SpotStatus;
import com.harshit.parkinglot.enums.SpotType;
import com.harshit.parkinglot.enums.VehicleType;
import com.harshit.parkinglot.util.SpotCompatibility;

/**
 * ParkingSpot = one physical parking space.
 *
 * Single Responsibility:
 * - knows its type (compact/large)
 * - knows if it is free or occupied
 * - parks or removes a vehicle
 *
 * synchronized methods = basic thread safety when two gates try to book same spot.
 */
public class ParkingSpot {

    private final String spotId;
    private final SpotType spotType;
    private SpotStatus status;
    private Vehicle parkedVehicle;

    public ParkingSpot(String spotId, SpotType spotType) {
        this.spotId = spotId;
        this.spotType = spotType;
        this.status = SpotStatus.AVAILABLE;
    }

    public String getSpotId() {
        return spotId;
    }

    public SpotType getSpotType() {
        return spotType;
    }

    public SpotStatus getStatus() {
        return status;
    }

    public Vehicle getParkedVehicle() {
        return parkedVehicle;
    }

    public boolean isAvailable() {
        return status == SpotStatus.AVAILABLE;
    }

    public boolean canFit(VehicleType vehicleType) {
        return SpotCompatibility.canFit(vehicleType, spotType);
    }

    public synchronized void parkVehicle(Vehicle vehicle) {
        if (!isAvailable()) {
            throw new IllegalStateException("Spot " + spotId + " is already occupied.");
        }
        if (!canFit(vehicle.getType())) {
            throw new IllegalArgumentException("Spot " + spotId + " cannot fit " + vehicle.getType());
        }

        this.parkedVehicle = vehicle;
        this.status = SpotStatus.OCCUPIED;
    }

    public synchronized void vacate() {
        this.parkedVehicle = null;
        this.status = SpotStatus.AVAILABLE;
    }

    @Override
    public String toString() {
        return spotId + " (" + spotType + ", " + status
                + (parkedVehicle != null ? ", vehicle=" + parkedVehicle.getLicensePlate() : "") + ")";
    }
}
