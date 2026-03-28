package com.harshit.preparation.oop.polymorphism;

import java.math.BigDecimal;

/** Runs polymorphism demo for interviews. */
public final class CheckoutDemo {

    public static void main(String[] args) {
        CheckoutService checkout = new CheckoutService();
        BigDecimal total = new BigDecimal("199.99");
        checkout.checkout(total, new UpiPayment());
        checkout.checkout(total, new CardPayment());
    }

    private CheckoutDemo() {
    }
}
