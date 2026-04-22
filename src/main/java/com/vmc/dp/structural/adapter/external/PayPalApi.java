package com.vmc.dp.structural.adapter.external;

import org.springframework.stereotype.Component;

@Component
public class PayPalApi {
    public void transferFunds(String amountCurrency) {
        System.out.println("PayPalApi: Transferred " + amountCurrency);
    }
}
