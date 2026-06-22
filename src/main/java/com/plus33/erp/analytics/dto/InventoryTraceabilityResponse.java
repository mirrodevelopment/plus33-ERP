package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryTraceabilityResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        Long activeRecallsCount,
        Long recalledLotsCount,
        Long recalledSerialsCount,
        BigDecimal totalRecalledQuantity,
        BigDecimal compromiseRate
) {}
