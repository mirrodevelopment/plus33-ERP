/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : ComplianceProvider.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ComplianceProviderController
 * Related Service   : ComplianceProviderService, ComplianceProviderServiceImpl
 * Related Repository: ComplianceProviderRepository
 * Related Entity    : ComplianceProvider
 * Related DTO       : N/A
 * Related Mapper    : ComplianceProviderMapper
 * Related DB Table  : compliance_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

public interface ComplianceProvider {
    String getProviderType(); // ZATCA, IRP, PEPPOL
    ComplianceSubmissionResult submitDocument(String docType, Long docId, String ublXml);
}
