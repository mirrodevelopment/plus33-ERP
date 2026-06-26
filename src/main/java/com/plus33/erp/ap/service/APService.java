package com.plus33.erp.ap.service;

import com.plus33.erp.ap.dto.*;
import com.plus33.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface APService {

    /**
     * Returns company-wide AP dashboard metrics.
     */
    APDashboardResponse getAPDashboard(Long companyId);

    /**
     * Returns aging buckets from outstanding supplier invoices.
     * Supports configurable aging intervals. If intervals is null or empty, defaults to [30, 60, 90].
     * Pass {@code supplierId = null} to retrieve all suppliers.
     */
    List<APAgingResponse> getAgingReport(Long companyId, Long supplierId, List<Integer> intervals);

    /**
     * Returns the current AP position for a single supplier.
     */
    SupplierAPBalanceResponse getSupplierAPBalance(Long supplierId, Long companyId);

    /**
     * Builds a chronological ledger statement for a supplier within a date range.
     * Includes opening balance, bills, payments, cancellations, and closing balance.
     */
    SupplierStatementResponse getSupplierStatement(Long supplierId, Long companyId, LocalDate from, LocalDate to);

    /**
     * Returns all supplier invoices that are overdue: due_date < today AND outstanding_balance > 0.
     */
    PageResponse<APOverdueBillResponse> getOverdueBills(Long companyId, Pageable pageable);

    /**
     * Returns detailed AP analytics, including payment trends, concentration, and cash forecast.
     */
    APAnalyticsResponse getAPAnalytics(Long companyId);
}
