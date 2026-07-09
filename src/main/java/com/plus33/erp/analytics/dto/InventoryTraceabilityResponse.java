/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryTraceabilityResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceabilityController
 * Related Service   : InventoryTraceabilityService, InventoryTraceabilityServiceImpl
 * Related Repository: InventoryTraceabilityRepository
 * Related Entity    : InventoryTraceability
 * Related DTO       : InventoryTraceabilityResponse
 * Related Mapper    : InventoryTraceabilityMapper
 * Related DB Table  : inventory_traceabilitys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTraceabilityController, InventoryTraceabilityService, InventoryTraceabilityServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryTraceabilityResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        Long activeRecallsCount,
        Long recalledLotsCount,
        Long recalledSerialsCount,
        BigDecimal totalRecalledQuantity,
        BigDecimal compromiseRate
) {}
