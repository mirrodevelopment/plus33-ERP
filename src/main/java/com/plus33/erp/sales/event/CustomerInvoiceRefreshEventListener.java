package com.plus33.erp.sales.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerInvoiceRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public CustomerInvoiceRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerInvoiceRefresh(CustomerInvoiceRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_sales_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_receivables_dashboard");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_customer_aging");
    }
}
