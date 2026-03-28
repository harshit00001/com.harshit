package com.harshit.preparation.oop.polymorphism;

import java.math.BigDecimal;

public final class CardPayment implements Payment {

    @Override
    public void pay(BigDecimal amount) {
        System.out.println("Card payment: " + amount);
    }
}
