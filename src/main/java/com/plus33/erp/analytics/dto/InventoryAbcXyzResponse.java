package com.plus33.erp.analytics.dto;

public record InventoryAbcXyzResponse(
        Long companyId,
        Long productId,
        Long warehouseId,
        Long storeId,
        String abcClass,
        String xyzClass
) {}
