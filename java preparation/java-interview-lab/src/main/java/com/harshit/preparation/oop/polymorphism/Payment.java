package com.harshit.preparation.oop.polymorphism;

import java.math.BigDecimal;

/**
 * Strategy-style capability: any implementation can be passed to {@link CheckoutService#checkout}.
 * Interview: this is polymorphism — same method call, different behavior at runtime.
 */
public interface Payment {

    void pay(BigDecimal amount);
}
