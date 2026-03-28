package com.harshit.preparation.oop.polymorphism;

import java.math.BigDecimal;

public final class UpiPayment implements Payment {

    @Override
    public void pay(BigDecimal amount) {
        System.out.println("UPI payment: " + amount);
    }
}
