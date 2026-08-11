package com.harshit.parkinglot;

import com.harshit.parkinglot.enums.VehicleType;
import com.harshit.parkinglot.factory.VehicleFactory;
import com.harshit.parkinglot.gate.EntryGate;
import com.harshit.parkinglot.gate.ExitGate;
import com.harshit.parkinglot.model.ParkingTicket;
import com.harshit.parkinglot.model.Vehicle;
import com.harshit.parkinglot.observer.DisplayBoard;
import com.harshit.parkinglot.singleton.ParkingLot;
import com.harshit.parkinglot.strategy.FlatPricingStrategy;
import com.harshit.parkinglot.strategy.HourlyPricingStrategy;
import com.harshit.parkinglot.strategy.PricingStrategy;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive console application for Parking Lot LLD demo.
 *
 * Run:
 * mvn compile exec:java
 *
 * Or run this main method from IDE.
 */
public class ParkingLotApplication {

    private final Scanner scanner;
    private final ParkingLot parkingLot;
    private final EntryGate entryGate;
    private ExitGate exitGate;

    public ParkingLotApplication() {
        this.scanner = new Scanner(System.in);
        this.parkingLot = ParkingLot.getInstance();
        this.parkingLot.registerObserver(new DisplayBoard("Main Entrance"));

        // default pricing = hourly
        PricingStrategy hourlyPricing = new HourlyPricingStrategy(50.0);
        this.entryGate = new EntryGate("Gate-A", parkingLot);
        this.exitGate = new ExitGate("Gate-B", parkingLot, hourlyPricing);
    }

    public static void main(String[] args) {
        ParkingLotApplication app = new ParkingLotApplication();
        app.run();
    }

    private void run() {
        System.out.println("========================================");
        System.out.println("   PARKING LOT LLD - Interactive Demo");
        System.out.println("========================================");
        parkingLot.printStatus();

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            try {
                switch (choice) {
                    case "1":
                        parkVehicle();
                        break;
                    case "2":
                        unparkVehicle();
                        break;
                    case "3":
                        parkingLot.printStatus();
                        break;
                    case "4":
                        showActiveTickets();
                        break;
                    case "5":
                        changePricingStrategy();
                        break;
                    case "6":
                        running = false;
                        System.out.println("Thank you. Goodbye!");
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter 1 to 6.");
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
        scanner.close();
    }

    private void printMenu() {
        System.out.println("\n----------- MENU -----------");
        System.out.println("1. Park vehicle (entry)");
        System.out.println("2. Unpark vehicle (exit)");
        System.out.println("3. Show parking status");
        System.out.println("4. Show active tickets");
        System.out.println("5. Change pricing strategy");
        System.out.println("6. Exit application");
        System.out.print("Enter choice: ");
    }

    private void parkVehicle() {
        VehicleType type = readVehicleType();
        System.out.print("Enter license plate: ");
        String plate = scanner.nextLine();

        Vehicle vehicle = VehicleFactory.create(type, plate);
        ParkingTicket ticket = entryGate.allowEntry(vehicle);

        System.out.println("Success! Keep this ticket id for exit: " + ticket.getTicketId());
        System.out.println("Assigned spot: " + ticket.getSpot().getSpotId());
    }

    private void unparkVehicle() {
        System.out.print("Enter ticket id: ");
        String ticketId = scanner.nextLine().trim().toUpperCase();
        exitGate.processExit(ticketId);
    }

    private void showActiveTickets() {
        List<ParkingTicket> tickets = parkingLot.getActiveTickets();
        if (tickets.isEmpty()) {
            System.out.println("No active tickets.");
            return;
        }
        System.out.println("\nActive tickets:");
        for (ParkingTicket ticket : tickets) {
            System.out.println("- " + ticket);
        }
    }

    private void changePricingStrategy() {
        System.out.println("\nChoose pricing strategy:");
        System.out.println("1. Hourly (Rs 50 per hour)");
        System.out.println("2. Flat (Rs 100 fixed)");
        System.out.print("Enter choice: ");
        String choice = scanner.nextLine().trim();

        PricingStrategy strategy;
        if ("2".equals(choice)) {
            strategy = new FlatPricingStrategy(100.0);
            System.out.println("Pricing changed to FLAT mode.");
        } else {
            strategy = new HourlyPricingStrategy(50.0);
            System.out.println("Pricing changed to HOURLY mode.");
        }

        this.exitGate = new ExitGate("Gate-B", parkingLot, strategy);
    }

    private VehicleType readVehicleType() {
        while (true) {
            System.out.println("\nSelect vehicle type:");
            System.out.println("1. Motorcycle");
            System.out.println("2. Car");
            System.out.println("3. Truck");
            System.out.print("Enter choice: ");
            String input = scanner.nextLine().trim();

            switch (input) {
                case "1":
                    return VehicleType.MOTORCYCLE;
                case "2":
                    return VehicleType.CAR;
                case "3":
                    return VehicleType.TRUCK;
                default:
                    System.out.println("Invalid vehicle type. Try again.");
            }
        }
    }
}
