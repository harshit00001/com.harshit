package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.VehicleType;

/** Concrete vehicle type. Largest vehicle in our design. */
public class Truck extends Vehicle {

    public Truck(String licensePlate) {
        super(licensePlate, VehicleType.TRUCK);
    }
}
