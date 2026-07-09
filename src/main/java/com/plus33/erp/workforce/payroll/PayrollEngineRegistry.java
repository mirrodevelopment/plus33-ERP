/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.payroll
 * File              : PayrollEngineRegistry.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollEngineRegistryController
 * Related Service   : PayrollEngineRegistryService, PayrollEngineRegistryServiceImpl
 * Related Repository: PayrollEngineRegistryRepository
 * Related Entity    : PayrollEngineRegistry
 * Related DTO       : N/A
 * Related Mapper    : PayrollEngineRegistryMapper
 * Related DB Table  : payroll_engine_registrys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.payroll;

import java.util.Optional;

public interface PayrollEngineRegistry {
    void registerProvider(PayrollEngineProvider provider);
    PayrollEngineProvider getProvider(String countryCode);
}
