package com.vmc.dp.structural.adapter.adapter;

import com.vmc.dp.structural.adapter.external.PhonePeApi;
import com.vmc.dp.structural.adapter.processor.PaymentProcessor;
import org.springframework.stereotype.Component;

@Component
public class PhonePeAdapter implements PaymentProcessor {

    private final PhonePeApi phonePeApi;

    public PhonePeAdapter(PhonePeApi phonePeApi) {
        this.phonePeApi = phonePeApi;
    }

    @Override
    public void makePayment(double amount) {
        // PhonePe-specific logic: delegate exactly or adapt specific fields
        phonePeApi.executeTransaction(amount);
    }
}
