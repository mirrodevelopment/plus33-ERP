package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryKpisResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        BigDecimal totalStockValue,
        Long totalUniqueProducts,
        Long outOfStockProducts,
        Long lowStockProducts,
        Long overstockProducts
) {}
