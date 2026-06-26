package com.plus33.erp.finance.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class SupplierInvoiceRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public SupplierInvoiceRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSupplierInvoiceRefresh(SupplierInvoiceRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_payables_aging");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_procurement_summary");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_supplier_performance");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_po_fulfilment");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_invoice_matching");
    }
}
