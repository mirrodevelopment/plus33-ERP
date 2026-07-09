/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryTurnoverResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTurnoverController
 * Related Service   : InventoryTurnoverService, InventoryTurnoverServiceImpl
 * Related Repository: InventoryTurnoverRepository
 * Related Entity    : InventoryTurnover
 * Related DTO       : InventoryTurnoverResponse
 * Related Mapper    : InventoryTurnoverMapper
 * Related DB Table  : inventory_turnovers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTurnoverController, InventoryTurnoverService, InventoryTurnoverServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryTurnoverResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        BigDecimal costOfGoodsSold,
        BigDecimal averageInventoryValue,
        BigDecimal inventoryTurnoverRatio,
        BigDecimal daysOnHand
) {}
