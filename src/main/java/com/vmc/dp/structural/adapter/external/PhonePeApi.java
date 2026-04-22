package com.vmc.dp.structural.adapter.external;

import org.springframework.stereotype.Component;

@Component
public class PhonePeApi {
    public void executeTransaction(double txnAmount) {
        System.out.println("PhonePeApi: Transaction executed for " + txnAmount);
    }
}
