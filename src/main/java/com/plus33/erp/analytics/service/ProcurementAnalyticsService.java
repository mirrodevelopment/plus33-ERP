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
