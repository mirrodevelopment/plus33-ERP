/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ReplenishmentRuleUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRuleUpdateController
 * Related Service   : ReplenishmentRuleUpdateService, ReplenishmentRuleUpdateServiceImpl
 * Related Repository: ReplenishmentRuleUpdateRepository
 * Related Entity    : ReplenishmentRuleUpdate
 * Related DTO       : ReplenishmentRuleUpdateRequest
 * Related Mapper    : ReplenishmentRuleUpdateMapper
 * Related DB Table  : replenishment_rule_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentRuleUpdateController, ReplenishmentRuleUpdateService, ReplenishmentRuleUpdateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRuleUpdateRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ReplenishmentRuleUpdateRequest(
        @DecimalMin(value = "0.00", message = "Min quantity must be at least 0")
        BigDecimal minQuantity,

        @DecimalMin(value = "0.01", message = "Max quantity must be greater than 0")
        BigDecimal maxQuantity,

        @DecimalMin(value = "0.00", message = "Reorder point must be at least 0")
        BigDecimal reorderPoint,

        @DecimalMin(value = "0.01", message = "Reorder quantity must be greater than 0")
        BigDecimal reorderQuantity,

        @Min(value = 0, message = "Lead time days must be at least 0")
        Integer leadTimeDays,

        Boolean active
) {}
