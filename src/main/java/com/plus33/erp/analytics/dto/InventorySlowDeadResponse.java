/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventorySlowDeadResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySlowDeadController
 * Related Service   : InventorySlowDeadService, InventorySlowDeadServiceImpl
 * Related Repository: InventorySlowDeadRepository
 * Related Entity    : InventorySlowDead
 * Related DTO       : InventorySlowDeadResponse
 * Related Mapper    : InventorySlowDeadMapper
 * Related DB Table  : inventory_slow_deads
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySlowDeadController, InventorySlowDeadService, InventorySlowDeadServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record InventorySlowDeadResponse(
        Long companyId,
        Long productId,
        Long warehouseId,
        Long storeId,
        BigDecimal onHandQuantity,
        LocalDateTime lastMovementAt,
        BigDecimal usage90Days,
        Boolean isDead,
        Boolean isSlow
) {}
