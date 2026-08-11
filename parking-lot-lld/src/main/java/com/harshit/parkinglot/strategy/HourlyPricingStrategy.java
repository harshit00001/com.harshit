package com.harshit.parkinglot.strategy;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Hourly pricing example: charge per started hour.
 *
 * Demo rate: Rs 50 per hour (minimum 1 hour).
 */
public class HourlyPricingStrategy implements PricingStrategy {

    private final double ratePerHour;

    public HourlyPricingStrategy(double ratePerHour) {
        this.ratePerHour = ratePerHour;
    }

    @Override
    public double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        long hours = Math.max(1, (minutes + 59) / 60); // round up to full hours
        return hours * ratePerHour;
    }
}
