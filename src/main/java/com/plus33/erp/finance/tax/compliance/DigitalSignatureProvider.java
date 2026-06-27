package com.plus33.erp.finance.tax.compliance;

public interface DigitalSignatureProvider {
    String signPayload(String payload);
    String getPublicKey();
}
