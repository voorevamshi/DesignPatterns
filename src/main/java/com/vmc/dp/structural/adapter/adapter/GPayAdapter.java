package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.external.GPayApi;
import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class GPayAdapter implements PaymentProcessor {

    private final GPayApi gPayApi;

    public GPayAdapter(GPayApi gPayApi) {
        this.gPayApi = gPayApi;
    }

    @Override
    public void makePayment(double amount) {
        // GPay-specific logic: convert double to BigDecimal
        BigDecimal exactAmount = BigDecimal.valueOf(amount);
        gPayApi.sendDirectPayment(exactAmount);
    }
}
