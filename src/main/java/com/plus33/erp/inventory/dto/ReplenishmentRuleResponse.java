/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ReplenishmentRuleResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRuleController
 * Related Service   : ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 * Related Repository: ReplenishmentRuleRepository
 * Related Entity    : ReplenishmentRule
 * Related DTO       : ReplenishmentRuleResponse
 * Related Mapper    : ReplenishmentRuleMapper
 * Related DB Table  : replenishment_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentRuleController, ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRuleResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ReplenishmentRuleResponse(
        Long id,
        Long companyId,
        Long productId,
        String productName,
        Long warehouseId,
        String warehouseName,
        Long storeId,
        String storeName,
        BigDecimal minQuantity,
        BigDecimal maxQuantity,
        BigDecimal reorderPoint,
        BigDecimal reorderQuantity,
        int leadTimeDays,
        boolean active,
        UUID clientReferenceId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}
