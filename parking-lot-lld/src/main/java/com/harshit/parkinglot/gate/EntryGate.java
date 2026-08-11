package com.harshit.parkinglot.gate;

import com.harshit.parkinglot.model.ParkingTicket;
import com.harshit.parkinglot.model.Vehicle;
import com.harshit.parkinglot.singleton.ParkingLot;

/**
 * EntryGate handles vehicle entry flow.
 *
 * Real world: barrier opens after ticket is issued.
 * Here: gate delegates actual parking work to ParkingLot.
 *
 * Single Responsibility: entry-side operations only.
 */
public class EntryGate {

    private final String gateName;
    private final ParkingLot parkingLot;

    public EntryGate(String gateName, ParkingLot parkingLot) {
        this.gateName = gateName;
        this.parkingLot = parkingLot;
    }

    public ParkingTicket allowEntry(Vehicle vehicle) {
        System.out.println("\n[EntryGate: " + gateName + "] Vehicle arrived -> " + vehicle);
        ParkingTicket ticket = parkingLot.parkVehicle(vehicle);
        System.out.println("[EntryGate: " + gateName + "] Ticket issued -> " + ticket.getTicketId());
        return ticket;
    }
}
