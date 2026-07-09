/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.compliance
 * File              : EInvoiceComplianceService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EInvoiceComplianceController
 * Related Service   : EInvoiceComplianceService, EInvoiceComplianceServiceImpl
 * Related Repository: EInvoiceComplianceRepository
 * Related Entity    : EInvoiceCompliance
 * Related DTO       : N/A
 * Related Mapper    : EInvoiceComplianceMapper
 * Related DB Table  : e_invoice_compliances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.compliance;

import com.plus33.erp.finance.tax.entity.EInvoiceComplianceLog;

public interface EInvoiceComplianceService {
    EInvoiceComplianceLog submitEInvoice(Long companyId, String docType, Long docId, String providerType, String ublXml);
}
