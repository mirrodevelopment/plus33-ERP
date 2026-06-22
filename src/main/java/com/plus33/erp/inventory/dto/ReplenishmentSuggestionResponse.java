package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.ReplenishmentEvaluationSource;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

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
