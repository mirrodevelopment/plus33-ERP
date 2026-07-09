/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxCalculationEngine.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationEngineController
 * Related Service   : TaxCalculationEngineService, TaxCalculationEngineServiceImpl
 * Related Repository: TaxCalculationEngineRepository
 * Related Entity    : TaxCalculationEngine
 * Related DTO       : TaxCalculationRequest
 * Related Mapper    : TaxCalculationEngineMapper
 * Related DB Table  : tax_calculation_engines
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

public interface TaxCalculationEngine {
    TaxCalculationResult calculateTax(TaxCalculationRequest request);
}
