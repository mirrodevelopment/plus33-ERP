/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.service
 * File              : ProcurementAnalyticsService.java
 * Purpose           : Service interface contract defining the API for Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementAnalyticsController
 * Related Service   : ProcurementAnalyticsService, ProcurementAnalyticsServiceImpl
 * Related Repository: ProcurementAnalyticsRepository
 * Related Entity    : ProcurementAnalytics
 * Related DTO       : InvoiceMatchingResponse, PayablesAgingResponse, PoFulfilmentResponse, ProcurementSummaryResponse, SupplierPerformanceResponse
 * Related Mapper    : ProcurementAnalyticsMapper
 * Related DB Table  : procurement_analyticss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Analytics Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.analytics.service;

import com.plus33.erp.analytics.dto.*;
import java.util.List;

public interface ProcurementAnalyticsService {
    ProcurementSummaryResponse getSummary(Long companyId);
    List<SupplierPerformanceResponse> getSuppliers(Long companyId);
    List<PayablesAgingResponse> getPayablesAging(Long companyId);
    List<PoFulfilmentResponse> getPurchaseOrders(Long companyId);
    List<InvoiceMatchingResponse> getInvoiceMatching(Long companyId);
}
