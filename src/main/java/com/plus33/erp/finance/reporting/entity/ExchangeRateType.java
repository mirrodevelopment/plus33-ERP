/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.entity
 * File              : ExchangeRateType.java
 * Purpose           : Enumeration of typed constants for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ExchangeRateTypeController
 * Related Service   : ExchangeRateTypeService, ExchangeRateTypeServiceImpl
 * Related Repository: ExchangeRateTypeRepository
 * Related Entity    : ExchangeRateType
 * Related DTO       : N/A
 * Related Mapper    : ExchangeRateTypeMapper
 * Related DB Table  : exchange_rate_types
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Finance Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.entity;

public enum ExchangeRateType {
    SPOT,
    CORPORATE,
    MONTH_END,
    AVERAGE
}
