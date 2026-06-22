package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryAgingExpiryResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        BigDecimal aging0To30,
        BigDecimal aging31To90,
        BigDecimal aging91To180,
        BigDecimal aging180Plus,
        Long expiredLotsCount,
        Long expiring0To30Count,
        Long expiring31To90Count,
        Long expiring91To180Count,
        Long safeLotsCount
) {}
