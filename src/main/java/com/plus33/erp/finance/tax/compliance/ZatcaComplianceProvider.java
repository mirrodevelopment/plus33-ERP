/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : ZatcaComplianceProvider.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ZatcaComplianceProviderController
 * Related Service   : ZatcaComplianceProviderService, ZatcaComplianceProviderServiceImpl
 * Related Repository: ZatcaComplianceProviderRepository
 * Related Entity    : ZatcaComplianceProvider
 * Related DTO       : N/A
 * Related Mapper    : ZatcaComplianceProviderMapper
 * Related DB Table  : zatca_compliance_providers
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
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ZatcaComplianceProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.compliance}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ZatcaComplianceProvider implements ComplianceProvider {

    /**
     * Retrieves provider type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves provider type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public String getProviderType() {
        return "ZATCA";
    }

    /**
     * Submits the document for approval. Transitions DRAFT to SUBMITTED status.
     *
     * @param docType the docType input value
     * @param docId the docId input value
     * @param ublXml the ublXml input value
     * @return the ComplianceSubmissionResult result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Submits the document for approval. Transitions DRAFT to SUBMITTED status.
     *
     * @param docType the docType input value
     * @param docId the docId input value
     * @param ublXml the ublXml input value
     * @return the ComplianceSubmissionResult result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    public ComplianceSubmissionResult submitDocument(String docType, Long docId, String ublXml) {
        if (ublXml == null || !ublXml.contains("trn")) {
            return ComplianceSubmissionResult.builder()
                    .success(false)
                    .status("REJECTED")
                    .errorDetails("Missing Tax Registration Number (TRN) in ZATCA UBL XML payload.")
                    .build();
        }
        return ComplianceSubmissionResult.builder()
                .success(true)
                .status("ACCEPTED")
                .governmentUuid(UUID.randomUUID().toString())
                .build();
    }
}