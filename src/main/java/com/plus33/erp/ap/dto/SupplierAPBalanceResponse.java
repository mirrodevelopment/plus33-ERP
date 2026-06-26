package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record SupplierAPBalanceResponse(
        Long supplierId,
        String supplierName,
        Long companyId,
        BigDecimal totalOutstanding,
        BigDecimal totalOverdue,
        BigDecimal totalPaid,
        BigDecimal totalCredited
) {}
