package com.harshit.parkinglot.observer;

/**
 * Display board shows live availability to users at the entrance.
 *
 * It does not search spots itself. It only reacts to updates from ParkingLot.
 */
public class DisplayBoard implements ParkingObserver {

    private final String boardName;

    public DisplayBoard(String boardName) {
        this.boardName = boardName;
    }

    @Override
    public void onParkingUpdate(int totalSpots, int availableSpots) {
        System.out.println("[DisplayBoard: " + boardName + "] Available spots: "
                + availableSpots + " / " + totalSpots);
    }
}
