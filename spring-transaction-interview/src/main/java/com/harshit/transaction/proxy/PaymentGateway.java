package com.harshit.transaction.proxy;

public interface PaymentGateway {

    void charge(long cents);
}
