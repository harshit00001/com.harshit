package com.harshit.parkinglot.model;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Ticket = proof that a vehicle entered the parking lot.
 *
 * Stores:
 * - unique ticket id
 * - vehicle details
 * - assigned spot
 * - entry time (used later for pricing)
 */
public class ParkingTicket {

    private final String ticketId;
    private final Vehicle vehicle;
    private final ParkingSpot spot;
    private final LocalDateTime entryTime;
    private boolean active;

    public ParkingTicket(Vehicle vehicle, ParkingSpot spot, LocalDateTime entryTime) {
        this.ticketId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = entryTime;
        this.active = true;
    }

    public String getTicketId() {
        return ticketId;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public ParkingSpot getSpot() {
        return spot;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public boolean isActive() {
        return active;
    }

    public void close() {
        this.active = false;
    }

    @Override
    public String toString() {
        return "Ticket{id=" + ticketId
                + ", vehicle=" + vehicle
                + ", spot=" + spot.getSpotId()
                + ", entry=" + entryTime
                + ", active=" + active + "}";
    }
}
