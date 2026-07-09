/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ReplenishmentSuggestionResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentSuggestionController
 * Related Service   : ReplenishmentSuggestionService, ReplenishmentSuggestionServiceImpl
 * Related Repository: ReplenishmentSuggestionRepository
 * Related Entity    : ReplenishmentSuggestion
 * Related DTO       : ReplenishmentSuggestionResponse
 * Related Mapper    : ReplenishmentSuggestionMapper
 * Related DB Table  : replenishment_suggestions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentSuggestionController, ReplenishmentSuggestionService, ReplenishmentSuggestionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.ReplenishmentEvaluationSource;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentSuggestionResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record ReplenishmentSuggestionResponse(
        Long id,
        Long ruleId,
        Long companyId,
        Long productId,
        String productName,
        Long warehouseId,
        String warehouseName,
        Long storeId,
        String storeName,
        BigDecimal currentQuantity,
        BigDecimal reservedQuantity,
        BigDecimal availableQuantity,
        BigDecimal suggestedQuantity,
        ReplenishmentSuggestionStatus status,
        ReplenishmentEvaluationSource evaluationSource,
        Long purchaseRequestId,
        String purchaseRequestNumber,
        Long transferId,
        String transferNumber,
        UUID clientReferenceId,
        String notes,
        LocalDateTime evaluatedAt,
        LocalDateTime acknowledgedAt,
        LocalDateTime orderedAt,
        LocalDateTime fulfilledAt,
        LocalDateTime cancelledAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        Long version
) {}
