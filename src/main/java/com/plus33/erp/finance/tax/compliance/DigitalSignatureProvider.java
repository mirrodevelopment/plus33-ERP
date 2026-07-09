/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : DigitalSignatureProvider.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DigitalSignatureProviderController
 * Related Service   : DigitalSignatureProviderService, DigitalSignatureProviderServiceImpl
 * Related Repository: DigitalSignatureProviderRepository
 * Related Entity    : DigitalSignatureProvider
 * Related DTO       : N/A
 * Related Mapper    : DigitalSignatureProviderMapper
 * Related DB Table  : digital_signature_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

public interface DigitalSignatureProvider {
    String signPayload(String payload);
    String getPublicKey();
}
