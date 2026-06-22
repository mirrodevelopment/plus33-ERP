package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record SupplierPerformanceResponse(
        Long companyId,
        Long supplierId,
        String supplierName,
        Long totalOrders,
        BigDecimal totalSpend,
        BigDecimal onTimeDeliveryRate,
        BigDecimal avgLeadTimeDays
) {}
