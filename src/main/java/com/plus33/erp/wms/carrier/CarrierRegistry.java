package com.plus33.erp.wms.carrier;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Carrier Registry — O(1) lookup for carrier provider implementations.
 * Auto-discovers all {@link CarrierProvider} beans at startup.
 * To integrate a new carrier, implement the interface and add {@code @Component}.
 */
@Component
public class CarrierRegistry {

    private final Map<String, CarrierProvider> providers;

    public CarrierRegistry(List<CarrierProvider> providerList) {
        this.providers = providerList.stream()
                .collect(Collectors.toMap(CarrierProvider::providerKey, Function.identity()));
    }

    public CarrierProvider resolve(String providerKey) {
        return Optional.ofNullable(providers.get(providerKey))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown carrier provider: " + providerKey +
                        ". Available: " + providers.keySet()));
    }

    public String book(String providerKey, String accountNumber,
                       Long warehouseId, Map<String, Object> details) {
        return resolve(providerKey).book(accountNumber, warehouseId, details);
    }

    public String track(String providerKey, String trackingNumber, String accountNumber) {
        return resolve(providerKey).track(trackingNumber, accountNumber);
    }

    public String generateLabel(String providerKey, String trackingNumber,
                                 String accountNumber, Map<String, Object> request) {
        return resolve(providerKey).generateLabel(trackingNumber, accountNumber, request);
    }

    public BigDecimal estimateRate(String providerKey, String accountNumber,
                                    Map<String, Object> request) {
        return resolve(providerKey).estimateRate(accountNumber, request);
    }

    public String proofOfDelivery(String providerKey, String trackingNumber, String accountNumber) {
        return resolve(providerKey).proofOfDelivery(trackingNumber, accountNumber);
    }

    public java.util.Set<String> availableProviders() {
        return providers.keySet();
    }
}
