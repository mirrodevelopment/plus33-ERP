/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxFilingService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxFilingController
 * Related Service   : TaxFilingService, TaxFilingServiceImpl
 * Related Repository: TaxFilingRepository
 * Related Entity    : TaxFiling
 * Related DTO       : N/A
 * Related Mapper    : TaxFilingMapper
 * Related DB Table  : tax_filings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.PreFilingValidationResult;
import com.plus33.erp.finance.tax.entity.TaxFiling;

public interface TaxFilingService {
    PreFilingValidationResult validateFiling(Long calendarId);
    TaxFiling calculateFiling(Long calendarId, String filedBy);
    TaxFiling submitFiling(Long filingId, String filedBy);
}
