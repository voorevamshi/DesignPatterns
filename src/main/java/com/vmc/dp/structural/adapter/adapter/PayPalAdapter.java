package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.external.PayPalApi;
import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class PayPalAdapter implements PaymentProcessor {

    private final PayPalApi payPalApi;

    public PayPalAdapter(PayPalApi payPalApi) {
        this.payPalApi = payPalApi;
    }

    @Override
    public void makePayment(double amount) {
        // PayPal-specific logic: format amount to Currency String
        String amountCurrency = String.format("%.2f USD", amount);
        payPalApi.transferFunds(amountCurrency);
    }
}
