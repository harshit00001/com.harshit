package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.VehicleType;

/** Concrete vehicle type. Medium size vehicle. */
public class Car extends Vehicle {

    public Car(String licensePlate) {
        super(licensePlate, VehicleType.CAR);
    }
}
