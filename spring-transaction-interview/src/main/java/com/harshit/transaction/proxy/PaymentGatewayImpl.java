package com.harshit.transaction.proxy;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentGatewayImpl implements PaymentGateway {

    @Transactional
    @Override
    public void charge(long cents) {
        // no-op demo
    }
}
