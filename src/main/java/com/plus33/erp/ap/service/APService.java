/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.service
 * File              : APService.java
 * Purpose           : Service interface contract defining the API for Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APController
 * Related Service   : APService, APServiceImpl
 * Related Repository: APRepository
 * Related Entity    : AP
 * Related DTO       : APAgingResponse, APAnalyticsResponse, APDashboardResponse, APOverdueBillResponse, PageResponse
 * Related Mapper    : APMapper
 * Related DB Table  : a_ps
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Ap Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ap Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.ap.service;

import com.plus33.erp.ap.dto.*;
import com.plus33.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Ap Module</b>
 *
 * <p><b>Class  :</b> {@code APService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.ap.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Ap Module.</p>
 *
 * <p><b>Module Deps      :</b> Ap, Common</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
