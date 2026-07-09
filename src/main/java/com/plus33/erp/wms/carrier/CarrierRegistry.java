/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.carrier
 * File              : CarrierRegistry.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarrierRegistryController
 * Related Service   : CarrierRegistryService, CarrierRegistryServiceImpl
 * Related Repository: CarrierRegistryRepository
 * Related Entity    : CarrierRegistry
 * Related DTO       : N/A
 * Related Mapper    : CarrierRegistryMapper
 * Related DB Table  : carrier_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

    /**
     * Performs the resolve operation in this module.
     *
     * @param providerKey the providerKey input value
     * @return the CarrierProvider result
     */
    public CarrierProvider resolve(String providerKey) {
        return Optional.ofNullable(providers.get(providerKey))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown carrier provider: " + providerKey +
                        ". Available: " + providers.keySet()));
    }

    /**
     * Performs the book operation in this module.
     *
     * @return the result string value
     */
    public String book(String providerKey, String accountNumber,
                       Long warehouseId, Map<String, Object> details) {
        return resolve(providerKey).book(accountNumber, warehouseId, details);
    }

    /**
     * Performs the track operation in this module.
     *
     * @param providerKey the providerKey input value
     * @param trackingNumber the trackingNumber input value
     * @param accountNumber the accountNumber input value
     * @return the result string value
     */
    public String track(String providerKey, String trackingNumber, String accountNumber) {
        return resolve(providerKey).track(trackingNumber, accountNumber);
    }

    /**
     * Generates the label based on input parameters and business rules.
     *
     * @return the result string value
     */
    public String generateLabel(String providerKey, String trackingNumber,
                                 String accountNumber, Map<String, Object> request) {
        return resolve(providerKey).generateLabel(trackingNumber, accountNumber, request);
    }

    /**
     * Performs the estimateRate operation in this module.
     *
     * @return the BigDecimal result
     */
    public BigDecimal estimateRate(String providerKey, String accountNumber,
                                    Map<String, Object> request) {
        return resolve(providerKey).estimateRate(accountNumber, request);
    }

    /**
     * Performs the proofOfDelivery operation in this module.
     *
     * @param providerKey the providerKey input value
     * @param trackingNumber the trackingNumber input value
     * @param accountNumber the accountNumber input value
     * @return the result string value
     */
    public String proofOfDelivery(String providerKey, String trackingNumber, String accountNumber) {
        return resolve(providerKey).proofOfDelivery(trackingNumber, accountNumber);
    }

    /**
     * Performs the availableProviders operation in this module.
     *
     */
    public java.util.Set<String> availableProviders() {
        return providers.keySet();
    }
}
