package com.plus33.erp.wms.carrier;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Pluggable carrier provider interface.
 * Each carrier implementation registers itself with the {@link CarrierRegistry}.
 */
public interface CarrierProvider {

    /** Provider key matching the carrier's provider_key column (e.g. FEDEX, DHL, UPS, LOCAL) */
    String providerKey();

    /** Book a shipment and return the carrier's booking reference / tracking number */
    String book(String accountNumber, Long warehouseId, Map<String, Object> shipmentDetails);

    /** Track a shipment by tracking number — returns status string */
    String track(String trackingNumber, String accountNumber);

    /** Generate a shipping label — returns URL or base64 content */
    String generateLabel(String trackingNumber, String accountNumber, Map<String, Object> labelRequest);

    /** Estimate freight cost */
    BigDecimal estimateRate(String accountNumber, Map<String, Object> rateRequest);

    /** Record proof of delivery */
    String proofOfDelivery(String trackingNumber, String accountNumber);
}
