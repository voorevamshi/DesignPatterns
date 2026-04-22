package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class StripeAdapter implements PaymentProcessor {

    public void makePayment(double amount) {
        // Stripe-specific logic to process payment
        System.out.println("Payment processed via Stripe: " + amount);
    }
}
