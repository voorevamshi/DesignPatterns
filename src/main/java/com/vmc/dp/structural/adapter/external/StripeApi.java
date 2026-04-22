package com.vmc.dp.structural.adapter.external;

import org.springframework.stereotype.Component;

@Component
public class StripeApi {
    public void charge(int amountInCents) {
        System.out.println("StripeApi: Charged " + amountInCents + " cents.");
    }
}
