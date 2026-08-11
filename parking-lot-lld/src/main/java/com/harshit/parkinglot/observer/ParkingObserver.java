package com.harshit.parkinglot.observer;

/**
 * OBSERVER PATTERN:
 *
 * When parking state changes (spot occupied or freed),
 * observers like DisplayBoard get notified automatically.
 *
 * This keeps display logic separate from core parking logic.
 */
public interface ParkingObserver {

    void onParkingUpdate(int totalSpots, int availableSpots);
}
