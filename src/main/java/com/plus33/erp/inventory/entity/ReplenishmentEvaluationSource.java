/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : ReplenishmentEvaluationSource.java
 * Purpose           : Enumeration of typed constants for Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentEvaluationSourceController
 * Related Service   : ReplenishmentEvaluationSourceService, ReplenishmentEvaluationSourceServiceImpl
 * Related Repository: ReplenishmentEvaluationSourceRepository
 * Related Entity    : ReplenishmentEvaluationSource
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentEvaluationSourceMapper
 * Related DB Table  : replenishment_evaluation_sources
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Typed enum defining state/category constants for Inventory Module. Referenced in entity fields, service logic, and DB check constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

public enum ReplenishmentEvaluationSource {
    MANUAL,
    SCHEDULED,
    EVENT_DRIVEN
}
