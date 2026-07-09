/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : ProcurementSummaryResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementSummaryController
 * Related Service   : ProcurementSummaryService, ProcurementSummaryServiceImpl
 * Related Repository: ProcurementSummaryRepository
 * Related Entity    : ProcurementSummary
 * Related DTO       : ProcurementSummaryResponse
 * Related Mapper    : ProcurementSummaryMapper
 * Related DB Table  : procurement_summarys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementSummaryController, ProcurementSummaryService, ProcurementSummaryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
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
