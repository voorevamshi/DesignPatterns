package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class PhonePeAdapter implements PaymentProcessor {

    @Override
    public void makePayment(double amount) {
        //call actual phonePe api
        System.out.println("Payment processed via PhonePe: " + amount);
    }
}
