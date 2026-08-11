package com.harshit.parkinglot.strategy;

import java.time.LocalDateTime;

/**
 * Flat pricing example: same fee no matter how long vehicle stayed.
 *
 * Useful when interviewer asks: "How will you support another pricing model?"
 */
public class FlatPricingStrategy implements PricingStrategy {

    private final double flatFee;

    public FlatPricingStrategy(double flatFee) {
        this.flatFee = flatFee;
    }

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        return flatFee;
    }
}
