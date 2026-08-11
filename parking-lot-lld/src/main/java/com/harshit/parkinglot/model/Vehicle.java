package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.VehicleType;

/**
 * ABSTRACTION + INHERITANCE (OOP basics for LLD):
 *
 * Vehicle is the parent type. Car, Motorcycle, and Truck are child types.
 * ParkingLot works with Vehicle, not with every child separately.
 *
 * Liskov Substitution Principle (SOLID):
 * Any subclass can be used wherever Vehicle is expected.
 */
public abstract class Vehicle {

    private final String licensePlate;
    private final VehicleType type;

    protected Vehicle(String licensePlate, VehicleType type) {
        this.licensePlate = licensePlate;
        this.type = type;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public VehicleType getType() {
        return type;
    }

    @Override
    public String toString() {
        return type + " [" + licensePlate + "]";
    }
}
