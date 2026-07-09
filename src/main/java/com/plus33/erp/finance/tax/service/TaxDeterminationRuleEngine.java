/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxDeterminationRuleEngine.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxDeterminationRuleEngineController
 * Related Service   : TaxDeterminationRuleEngineService, TaxDeterminationRuleEngineServiceImpl
 * Related Repository: TaxDeterminationRuleEngineRepository
 * Related Entity    : TaxDeterminationRuleEngine
 * Related DTO       : N/A
 * Related Mapper    : TaxDeterminationRuleEngineMapper
 * Related DB Table  : tax_determination_rule_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxGroup;
import java.time.LocalDate;

public interface TaxDeterminationRuleEngine {
    TaxGroup determineTaxGroup(
            Long companyId,
            String documentType,
            String customerTaxProfile,
            String supplierTaxProfile,
            String productTaxCategory,
            String originCountry,
            String originState,
            String destCountry,
            String destState,
            String incoterms,
            LocalDate date
    );
}
