package com.harshit.parkinglot.gate;

import com.harshit.parkinglot.model.Payment;
import com.harshit.parkinglot.singleton.ParkingLot;
import com.harshit.parkinglot.strategy.PricingStrategy;

/**
 * ExitGate handles vehicle exit flow.
 *
 * Steps:
 * 1. take ticket id
 * 2. ask ParkingLot to unpark
 * 3. calculate fee using PricingStrategy
 * 4. return payment details
 */
public class ExitGate {

    private final String gateName;
    private final ParkingLot parkingLot;
    private final PricingStrategy pricingStrategy;

    public ExitGate(String gateName, ParkingLot parkingLot, PricingStrategy pricingStrategy) {
        this.gateName = gateName;
        this.parkingLot = parkingLot;
        this.pricingStrategy = pricingStrategy;
    }

    public Payment processExit(String ticketId) {
        System.out.println("\n[ExitGate: " + gateName + "] Processing ticket -> " + ticketId);
        Payment payment = parkingLot.unparkVehicle(ticketId, pricingStrategy);
        System.out.println("[ExitGate: " + gateName + "] Payment successful -> " + payment);
        return payment;
    }
}
