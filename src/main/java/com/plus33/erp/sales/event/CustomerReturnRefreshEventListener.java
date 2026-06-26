package com.plus33.erp.sales.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class CustomerReturnRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public CustomerReturnRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerReturnRefresh(CustomerReturnRefreshEvent event) {
        // Refresh Sales & Finance Analytics Materialized Views
        analyticsRefreshService.refreshViewWithNewTransaction("mv_sales_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_receivables_dashboard");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_customer_aging");

        // Refresh Inventory Analytics Materialized Views
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_traceability_metrics");
    }
}
