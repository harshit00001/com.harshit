package com.harshit.preparation.oop.polymorphism;

import java.math.BigDecimal;

/**
 * Open/Closed: add new {@link Payment} types without editing this class.
 */
public final class CheckoutService {

    public void checkout(BigDecimal cartTotal, Payment payment) {
        payment.pay(cartTotal);
    }
}
