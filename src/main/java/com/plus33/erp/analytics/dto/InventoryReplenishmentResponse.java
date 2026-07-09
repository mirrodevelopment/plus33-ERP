/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryReplenishmentResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryReplenishmentController
 * Related Service   : InventoryReplenishmentService, InventoryReplenishmentServiceImpl
 * Related Repository: InventoryReplenishmentRepository
 * Related Entity    : InventoryReplenishment
 * Related DTO       : InventoryReplenishmentResponse
 * Related Mapper    : InventoryReplenishmentMapper
 * Related DB Table  : inventory_replenishments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryReplenishmentController, InventoryReplenishmentService, InventoryReplenishmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryReplenishmentResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        Long rulesCount,
        BigDecimal rulesCoveragePercent,
        BigDecimal avgReplenishmentCycleTimeHours,
        BigDecimal stockoutPreventionRate
) {}
