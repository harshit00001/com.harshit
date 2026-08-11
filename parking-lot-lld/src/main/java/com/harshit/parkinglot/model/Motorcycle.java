package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.VehicleType;

/** Concrete vehicle type. Smallest vehicle in our design. */
public class Motorcycle extends Vehicle {

    public Motorcycle(String licensePlate) {
        super(licensePlate, VehicleType.MOTORCYCLE);
    }
}
