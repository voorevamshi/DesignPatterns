package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.external.StripeApi;
import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class StripeAdapter implements PaymentProcessor {

    private final StripeApi stripeApi;

    public StripeAdapter(StripeApi stripeApi) {
        this.stripeApi = stripeApi;
    }

    @Override
    public void makePayment(double amount) {
        // Stripe-specific logic: convert amount to cents
        int amountInCents = (int) (amount * 100);
        stripeApi.charge(amountInCents);
    }
}
