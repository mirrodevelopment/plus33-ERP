/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.carrier
 * File              : CarrierProviderImpls.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarrierProviderImplsController
 * Related Service   : CarrierProviderImplsService, CarrierProviderImplsServiceImpl
 * Related Repository: CarrierProviderImplsRepository
 * Related Entity    : CarrierProviderImpls
 * Related DTO       : N/A
 * Related Mapper    : CarrierProviderImplsMapper
 * Related DB Table  : carrier_provider_implss
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
import java.util.Map;
import java.util.UUID;

/**
 * Built-in carrier provider implementations.
 * Each is auto-discovered by {@link CarrierRegistry} via Spring bean injection.
 *
 * In production, the FedEx/DHL/UPS implementations would call the respective
 * REST APIs using the api_endpoint and api_key from the Carrier entity.
 * Here they return well-formed mock responses suitable for integration testing.
 */

@Component
class FedExCarrierProvider implements CarrierProvider {
    @Override public String providerKey() { return "FEDEX"; }
    @Override public String book(String account, Long warehouseId, Map<String, Object> details) {
        return "FX-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
    @Override public String track(String trackingNumber, String account) {
        return "IN_TRANSIT";
    }
    @Override public String generateLabel(String tracking, String account, Map<String, Object> req) {
        return "https://labels.fedex.example.com/" + tracking + ".pdf";
    }
    @Override public BigDecimal estimateRate(String account, Map<String, Object> req) {
        return new BigDecimal("24.99");
    }
    @Override public String proofOfDelivery(String tracking, String account) {
        return "POD-FX-" + tracking;
    }
}

@Component
class DhlCarrierProvider implements CarrierProvider {
    @Override public String providerKey() { return "DHL"; }
    @Override public String book(String account, Long warehouseId, Map<String, Object> details) {
        return "DH-" + UUID.randomUUID().toString().substring(0, 12).toUpperCase();
    }
    @Override public String track(String trackingNumber, String account) { return "DELIVERED"; }
    @Override public String generateLabel(String tracking, String account, Map<String, Object> req) {
        return "https://labels.dhl.example.com/" + tracking + ".pdf";
    }
    @Override public BigDecimal estimateRate(String account, Map<String, Object> req) {
        return new BigDecimal("18.50");
    }
    @Override public String proofOfDelivery(String tracking, String account) {
        return "POD-DH-" + tracking;
    }
}

@Component
class UpsCarrierProvider implements CarrierProvider {
    @Override public String providerKey() { return "UPS"; }
    @Override public String book(String account, Long warehouseId, Map<String, Object> details) {
        return "1Z" + UUID.randomUUID().toString().substring(0, 16).toUpperCase();
    }
    @Override public String track(String trackingNumber, String account) { return "OUT_FOR_DELIVERY"; }
    @Override public String generateLabel(String tracking, String account, Map<String, Object> req) {
        return "https://labels.ups.example.com/" + tracking + ".gif";
    }
    @Override public BigDecimal estimateRate(String account, Map<String, Object> req) {
        return new BigDecimal("21.75");
    }
    @Override public String proofOfDelivery(String tracking, String account) {
        return "POD-UPS-" + tracking;
    }
}

@Component
class LocalCarrierProvider implements CarrierProvider {
    @Override public String providerKey() { return "LOCAL"; }
    @Override public String book(String account, Long warehouseId, Map<String, Object> details) {
        return "LC-" + System.currentTimeMillis();
    }
    @Override public String track(String trackingNumber, String account) { return "DISPATCHED"; }
    @Override public String generateLabel(String tracking, String account, Map<String, Object> req) {
        return "LOCAL-LABEL-" + tracking;
    }
    @Override public BigDecimal estimateRate(String account, Map<String, Object> req) {
        return new BigDecimal("5.00");
    }
    @Override public String proofOfDelivery(String tracking, String account) {
        return "POD-LC-" + tracking;
    }
}

@Component
class CustomRestCarrierProvider implements CarrierProvider {
    @Override public String providerKey() { return "CUSTOM"; }
    @Override public String book(String account, Long warehouseId, Map<String, Object> details) {
        return "CUSTOM-" + UUID.randomUUID();
    }
    @Override public String track(String trackingNumber, String account) { return "UNKNOWN"; }
    @Override public String generateLabel(String tracking, String account, Map<String, Object> req) {
        return "CUSTOM-LABEL-" + tracking;
    }
    @Override public BigDecimal estimateRate(String account, Map<String, Object> req) {
        return BigDecimal.ZERO;
    }
    @Override public String proofOfDelivery(String tracking, String account) {
        return "POD-CUSTOM-" + tracking;
    }
}
