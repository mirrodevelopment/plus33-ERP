/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ReplenishmentRuleRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRuleController
 * Related Service   : ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 * Related Repository: ReplenishmentRuleRepository
 * Related Entity    : ReplenishmentRule
 * Related DTO       : ReplenishmentRuleRequest
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

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRuleRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ReplenishmentRuleRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Product ID is required")
        Long productId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Min quantity is required")
        @DecimalMin(value = "0.00", message = "Min quantity must be at least 0")
        BigDecimal minQuantity,

        @NotNull(message = "Max quantity is required")
        @DecimalMin(value = "0.01", message = "Max quantity must be greater than 0")
        BigDecimal maxQuantity,

        @NotNull(message = "Reorder point is required")
        @DecimalMin(value = "0.00", message = "Reorder point must be at least 0")
        BigDecimal reorderPoint,

        @NotNull(message = "Reorder quantity is required")
        @DecimalMin(value = "0.01", message = "Reorder quantity must be greater than 0")
        BigDecimal reorderQuantity,

        @Min(value = 0, message = "Lead time days must be at least 0")
        Integer leadTimeDays,

        Boolean active,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId
) {}
