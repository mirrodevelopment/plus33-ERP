/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : DigitalSignatureProviderImpl.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
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

import org.springframework.stereotype.Component;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DigitalSignatureProviderImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.compliance}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class DigitalSignatureProviderImpl implements DigitalSignatureProvider {

    /**
     * Performs the signPayload operation in this module.
     *
     * @param payload the payload input value
     * @return the result string value
     */
    /**
     * Performs the signPayload operation in this module.
     *
     * @param payload the payload input value
     * @return the result string value
     */
    @Override
    public String signPayload(String payload) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(payload.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            throw new RuntimeException("Error signing payload", e);
        }
    }

    /**
     * Retrieves public key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves public key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getPublicKey() {
        return "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0y6...stub...key";
    }
}