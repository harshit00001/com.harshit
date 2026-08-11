package com.harshit.parkinglot.singleton;

import com.harshit.parkinglot.enums.PaymentStatus;
import com.harshit.parkinglot.enums.SpotType;
import com.harshit.parkinglot.exception.InvalidTicketException;
import com.harshit.parkinglot.exception.ParkingFullException;
import com.harshit.parkinglot.model.ParkingFloor;
import com.harshit.parkinglot.model.ParkingSpot;
import com.harshit.parkinglot.model.ParkingTicket;
import com.harshit.parkinglot.model.Payment;
import com.harshit.parkinglot.model.Vehicle;
import com.harshit.parkinglot.observer.ParkingObserver;
import com.harshit.parkinglot.strategy.FirstAvailableSpotAssignmentStrategy;
import com.harshit.parkinglot.strategy.PricingStrategy;
import com.harshit.parkinglot.strategy.SpotAssignmentStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SINGLETON PATTERN:
 *
 * In real life there is one parking lot instance managing all floors/spots.
 * We allow only one ParkingLot object in the application.
 *
 * ParkingLot is the orchestrator:
 * - finds spot using SpotAssignmentStrategy
 * - creates ticket on entry
 * - frees spot and closes ticket on exit
 * - notifies observers when availability changes
 */
public class ParkingLot {

    private static ParkingLot instance;

    private final String name;
    private final List<ParkingFloor> floors;
    private final Map<String, ParkingTicket> activeTickets;
    private final SpotAssignmentStrategy spotAssignmentStrategy;
    private final List<ParkingObserver> observers;

    private ParkingLot(String name,
                       List<ParkingFloor> floors,
                       SpotAssignmentStrategy spotAssignmentStrategy) {
        this.name = name;
        this.floors = floors;
        this.spotAssignmentStrategy = spotAssignmentStrategy;
        this.activeTickets = new HashMap<>();
        this.observers = new ArrayList<>();
    }

    /**
     * Singleton access method.
     * synchronized = safe when multiple threads call getInstance() together.
     */
    public static synchronized ParkingLot getInstance() {
        if (instance == null) {
            instance = createDefaultParkingLot();
        }
        return instance;
    }

    /**
     * For tests or custom setup: replace default singleton instance.
     */
    public static synchronized void initialize(ParkingLot customInstance) {
        instance = customInstance;
    }

    public void registerObserver(ParkingObserver observer) {
        observers.add(observer);
        notifyObservers();
    }

    public synchronized ParkingTicket parkVehicle(Vehicle vehicle) {
        if (isVehicleAlreadyParked(vehicle.getLicensePlate())) {
            throw new IllegalStateException("Vehicle already inside: " + vehicle.getLicensePlate());
        }

        ParkingSpot spot = spotAssignmentStrategy.findSpot(floors, vehicle.getType());
        if (spot == null) {
            throw new ParkingFullException("No compatible spot available for " + vehicle.getType());
        }

        spot.parkVehicle(vehicle);
        ParkingTicket ticket = new ParkingTicket(vehicle, spot, LocalDateTime.now());
        activeTickets.put(ticket.getTicketId(), ticket);

        notifyObservers();
        return ticket;
    }

    public synchronized Payment unparkVehicle(String ticketId, PricingStrategy pricingStrategy) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null || !ticket.isActive()) {
            throw new InvalidTicketException("Invalid or already closed ticket: " + ticketId);
        }

        LocalDateTime exitTime = LocalDateTime.now();
        double fee = pricingStrategy.calculateFee(ticket.getEntryTime(), exitTime);

        ticket.getSpot().vacate();
        ticket.close();
        activeTickets.remove(ticketId);

        notifyObservers();
        return new Payment(fee, PaymentStatus.PAID);
    }

    public synchronized List<ParkingTicket> getActiveTickets() {
        return new ArrayList<>(activeTickets.values());
    }

    public String getName() {
        return name;
    }

    public List<ParkingFloor> getFloors() {
        return Collections.unmodifiableList(floors);
    }

    public int getTotalSpots() {
        return floors.stream().mapToInt(floor -> floor.getSpots().size()).sum();
    }

    public int getAvailableSpots() {
        return (int) floors.stream()
                .flatMap(floor -> floor.getSpots().stream())
                .filter(ParkingSpot::isAvailable)
                .count();
    }

    public void printStatus() {
        System.out.println("\n=== Parking Lot: " + name + " ===");
        for (ParkingFloor floor : floors) {
            System.out.println(floor);
            for (ParkingSpot spot : floor.getSpots()) {
                System.out.println("  - " + spot);
            }
        }
        System.out.println("Total available: " + getAvailableSpots() + "/" + getTotalSpots());
    }

    private boolean isVehicleAlreadyParked(String licensePlate) {
        return activeTickets.values().stream()
                .anyMatch(ticket -> ticket.getVehicle().getLicensePlate().equalsIgnoreCase(licensePlate));
    }

    private void notifyObservers() {
        int total = getTotalSpots();
        int available = getAvailableSpots();
        for (ParkingObserver observer : observers) {
            observer.onParkingUpdate(total, available);
        }
    }

    /**
     * Demo setup:
     * Floor 1 -> 2 compact + 1 large
     * Floor 2 -> 1 compact + 2 large
     */
    private static ParkingLot createDefaultParkingLot() {
        List<ParkingFloor> floors = new ArrayList<>();

        List<ParkingSpot> floor1Spots = new ArrayList<>();
        floor1Spots.add(new ParkingSpot("F1-C1", SpotType.COMPACT));
        floor1Spots.add(new ParkingSpot("F1-C2", SpotType.COMPACT));
        floor1Spots.add(new ParkingSpot("F1-L1", SpotType.LARGE));
        floors.add(new ParkingFloor(1, floor1Spots));

        List<ParkingSpot> floor2Spots = new ArrayList<>();
        floor2Spots.add(new ParkingSpot("F2-C1", SpotType.COMPACT));
        floor2Spots.add(new ParkingSpot("F2-L1", SpotType.LARGE));
        floor2Spots.add(new ParkingSpot("F2-L2", SpotType.LARGE));
        floors.add(new ParkingFloor(2, floor2Spots));

        return new ParkingLot(
                "City Mall Parking",
                floors,
                new FirstAvailableSpotAssignmentStrategy()
        );
    }
}
