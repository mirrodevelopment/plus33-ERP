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
