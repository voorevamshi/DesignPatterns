# Adapter Design Pattern

## Overview
The **Adapter Design Pattern** is a structural pattern that allows objects with incompatible interfaces to collaborate. It acts as a bridge between two incompatible interfaces by wrapping an existing class with a new interface so that it becomes compatible with the client's interface.

## Key Components
1. **Target**: Defines the domain-specific interface that the client uses.
2. **Adapter**: Adapts the interface of the Adaptee to the Target interface.
3. **Adaptee**: Defines an existing, incompatible interface that needs adapting (usually a third-party service or legacy code).
4. **Client**: Collaborates with objects conforming to the Target interface.

## Current Project Implementation
In this repository, the Adapter pattern is structured around a payment processing system. 
- **Target Interface**: `PaymentProcessor` (Provides a unified `makePayment(double amount)` method).
- **Client**: `PaymentService` and `PaymentController` (They expect to communicate with a standard `PaymentProcessor`).
- **Adaptees (External APIs)**: Simulated third-party APIs with incompatible method signatures found in the `external` package (e.g., `StripeApi` expecting `int` cents, `GPayApi` expecting `BigDecimal`).
- **Adapters**: `GPayAdapter`, `StripeAdapter`, `PayPalAdapter`, `PhonePeAdapter`.

### How it Works
The client, `PaymentService`, needs to process payments but different payment gateways (Stripe, GPay, PayPal) have completely different method signatures and required parameters. 

Rather than polluting the client with custom logic for every payment gateway, we use an **Adapter**. 

For example, the `PaymentProcessor` sets a standard signature:
```java
public void makePayment(double amount);
```

But the `StripeApi` (Adaptee) requires the amount in cents as an integer:
```java
public class StripeApi {
    public void charge(int amountInCents) { ... }
}
```

The `StripeAdapter` bridges this gap. It implements the `PaymentProcessor` target interface but internally delegates the call to the `StripeApi` after performing the necessary data conversion:
```java
@Component
public class StripeAdapter implements PaymentProcessor {
    private final StripeApi stripeApi;

    public StripeAdapter(StripeApi stripeApi) {
        this.stripeApi = stripeApi;
    }

    @Override
    public void makePayment(double amount) {
        int amountInCents = (int) (amount * 100); // Conversion
        stripeApi.charge(amountInCents); // Delegation to Adaptee
    }
}
```

By doing this, the client can seamlessly execute `processor.makePayment(150.00)` without needing to understand the underlying complexities of Stripe, Google Pay, or PayPal. New gateways can be added simply by creating a new `Adapter` class without modifying the client logic.
