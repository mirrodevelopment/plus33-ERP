package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APAnalyticsResponse(
        Long companyId,
        BigDecimal averageInvoiceAmount,
        BigDecimal averageDaysToPay,
        BigDecimal earlyPaymentDiscounts,
        List<TopSupplierDTO> supplierConcentration,
        List<TopSupplierDTO> largestOutstandingSuppliers,
        CashRequirementDTO cashForecast
) {}
