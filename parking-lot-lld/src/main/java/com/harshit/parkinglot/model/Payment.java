package com.harshit.parkinglot.model;

import com.harshit.parkinglot.enums.PaymentStatus;

/**
 * Simple payment record created when vehicle exits.
 */
public class Payment {

    private final double amount;
    private final PaymentStatus status;

    public Payment(double amount, PaymentStatus status) {
        this.amount = amount;
        this.status = status;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "Payment{amount=" + amount + ", status=" + status + "}";
    }
}
