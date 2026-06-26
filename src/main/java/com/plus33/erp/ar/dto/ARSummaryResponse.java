package com.plus33.erp.ar.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * Company-wide AR summary combining mv_sales_kpis and mv_receivables_dashboard.
 */
public record ARSummaryResponse(
        Long companyId,
        Long totalInvoices,
        BigDecimal totalInvoicedAmount,
        BigDecimal totalPaidAmount,
        BigDecimal totalOutstandingAmount,
        List<ARStatusBucket> invoicesByStatus
) {}
