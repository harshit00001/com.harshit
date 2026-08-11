package com.harshit.parkinglot.factory;

import com.harshit.parkinglot.enums.VehicleType;
import com.harshit.parkinglot.model.Car;
import com.harshit.parkinglot.model.Motorcycle;
import com.harshit.parkinglot.model.Truck;
import com.harshit.parkinglot.model.Vehicle;

/**
 * FACTORY PATTERN:
 *
 * Problem: caller should not create Car/Motorcycle/Truck using many if-else blocks.
 * Solution: one factory method creates the correct Vehicle object.
 *
 * Benefit: if we add a new vehicle type later, we change code in one place only.
 */
public final class VehicleFactory {

    private VehicleFactory() {
        // utility class - no object creation
    }

    public static Vehicle create(VehicleType type, String licensePlate) {
        if (licensePlate == null || licensePlate.trim().isEmpty()) {
            throw new IllegalArgumentException("License plate cannot be empty.");
        }

        switch (type) {
            case MOTORCYCLE:
                return new Motorcycle(licensePlate.trim().toUpperCase());
            case CAR:
                return new Car(licensePlate.trim().toUpperCase());
            case TRUCK:
                return new Truck(licensePlate.trim().toUpperCase());
            default:
                throw new IllegalArgumentException("Unsupported vehicle type: " + type);
        }
    }
}
