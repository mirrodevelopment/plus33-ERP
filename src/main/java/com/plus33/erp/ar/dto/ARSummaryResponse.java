/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ar Module
 * Package           : com.plus33.erp.ar.dto
 * File              : ARSummaryResponse.java
 * Purpose           : Data Transfer Object for request/response in Ar Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ARSummaryController
 * Related Service   : ARSummaryService, ARSummaryServiceImpl
 * Related Repository: ARSummaryRepository
 * Related Entity    : ARSummary
 * Related DTO       : ARSummaryResponse
 * Related Mapper    : ARSummaryMapper
 * Related DB Table  : a_r_summarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ARSummaryController, ARSummaryService, ARSummaryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ar Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
