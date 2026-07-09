/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxEngineProvider.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxEngineProviderController
 * Related Service   : TaxEngineProviderService, TaxEngineProviderServiceImpl
 * Related Repository: TaxEngineProviderRepository
 * Related Entity    : TaxEngineProvider
 * Related DTO       : TaxCalculationRequest
 * Related Mapper    : TaxEngineProviderMapper
 * Related DB Table  : tax_engine_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.dto.TaxCalculationRequest;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.finance.tax.entity.TaxGroup;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxEngineProvider}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface TaxEngineProvider {
    String getTaxType(); // e.g., "VAT", "GST", "SALES_TAX"
    TaxCalculationResult calculateTax(TaxCalculationRequest request, TaxGroup taxGroup, boolean isExempt);
}
