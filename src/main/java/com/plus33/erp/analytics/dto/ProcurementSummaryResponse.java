package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record ProcurementSummaryResponse(
        Long companyId,
        Long totalPurchaseRequests,
        Long totalPurchaseOrders,
        Long totalGoodsReceipts,
        Long totalSupplierInvoices,
        Long totalPayments,
        BigDecimal totalSpend,
        BigDecimal avgPrCycleTimeDays
) {}
