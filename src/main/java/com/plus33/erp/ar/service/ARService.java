/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.service
 * File              : ARService.java
 * Purpose           : Service interface contract defining the API for Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARController
 * Related Service   : ARService, ARServiceImpl
 * Related Repository: ARRepository
 * Related Entity    : AR
 * Related DTO       : ARAgingResponse, AROverdueInvoiceResponse, ARSummaryResponse, CustomerARBalanceResponse, CustomerStatementResponse
 * Related Mapper    : ARMapper
 * Related DB Table  : a_rs
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Ar Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Ar Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.ar.service;

import com.plus33.erp.ar.dto.*;
import com.plus33.erp.common.dto.PageResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

/**
 * Read-only AR reporting service.
 * <p>
 * All transactional write operations belong in {@link ARWriteOffService}.
 */
public interface ARService {

    /**
     * Returns aging buckets from the mv_customer_aging materialized view.
     * Pass {@code customerId = null} to retrieve all customers for the company.
     */
    List<ARAgingResponse> getAgingReport(Long companyId, Long customerId);

    /**
     * Returns company-wide AR summary from mv_sales_kpis and mv_receivables_dashboard.
     */
    ARSummaryResponse getARSummary(Long companyId);

    /**
     * Returns the current AR position for a single customer.
     */
    CustomerARBalanceResponse getCustomerARBalance(Long customerId, Long companyId);

    /**
     * Builds a chronological ledger statement for a customer within a date range.
     * Includes invoices, payments, credit notes, and write-offs with a running balance.
     */
    CustomerStatementResponse getCustomerStatement(Long customerId, Long companyId, LocalDate from, LocalDate to);

    /**
     * Returns all invoices that are overdue: due_date &lt; today AND outstanding_balance &gt; 0.
     */
    PageResponse<AROverdueInvoiceResponse> getOverdueInvoices(Long companyId, Pageable pageable);
}
