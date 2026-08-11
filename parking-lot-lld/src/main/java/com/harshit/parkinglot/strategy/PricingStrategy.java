package com.harshit.parkinglot.strategy;

import java.time.LocalDateTime;

/**
 * STRATEGY PATTERN (pricing part):
 *
 * Define a family of pricing algorithms and switch them at runtime.
 * ExitGate depends on this interface, not on one fixed formula.
 *
 * Open/Closed Principle: add weekend pricing by creating a new class,
 * without changing ExitGate code.
 */
public interface PricingStrategy {

    double calculateFee(LocalDateTime entryTime, LocalDateTime exitTime);
}
