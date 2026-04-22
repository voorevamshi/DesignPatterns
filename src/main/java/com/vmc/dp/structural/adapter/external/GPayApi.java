package com.vmc.dp.structural.adapter.external;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class GPayApi {
    public void sendDirectPayment(BigDecimal amount) {
        System.out.println("GPayApi: Processed payment of " + amount);
    }
}
