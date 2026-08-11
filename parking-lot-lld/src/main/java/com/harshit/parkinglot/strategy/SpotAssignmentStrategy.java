package com.harshit.parkinglot.strategy;

import com.harshit.parkinglot.enums.VehicleType;
import com.harshit.parkinglot.model.ParkingFloor;
import com.harshit.parkinglot.model.ParkingSpot;

import java.util.List;

/**
 * STRATEGY PATTERN (spot assignment part):
 *
 * Different ways to choose a spot:
 * - first available
 * - nearest to entrance
 * - best fit (compact car in compact spot)
 *
 * ParkingLot uses this interface so assignment logic can be changed easily.
 */
public interface SpotAssignmentStrategy {

    ParkingSpot findSpot(List<ParkingFloor> floors, VehicleType vehicleType);
}
