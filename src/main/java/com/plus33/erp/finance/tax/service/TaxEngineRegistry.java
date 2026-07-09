/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxEngineRegistry.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxEngineRegistryController
 * Related Service   : TaxEngineRegistryService, TaxEngineRegistryServiceImpl
 * Related Repository: TaxEngineRegistryRepository
 * Related Entity    : TaxEngineRegistry
 * Related DTO       : N/A
 * Related Mapper    : TaxEngineRegistryMapper
 * Related DB Table  : tax_engine_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

public interface TaxEngineRegistry {
    TaxEngineProvider resolve(String taxCategoryCode);
    void register(String taxCategoryCode, TaxEngineProvider provider);
}
