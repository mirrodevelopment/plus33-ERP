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
