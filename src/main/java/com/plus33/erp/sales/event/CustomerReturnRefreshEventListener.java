/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : CustomerReturnRefreshEventListener.java
 * Purpose           : Component of Sales Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnRefreshEventListenerController
 * Related Service   : CustomerReturnRefreshEventListenerService, CustomerReturnRefreshEventListenerServiceImpl
 * Related Repository: CustomerReturnRefreshEventListenerRepository
 * Related Entity    : CustomerReturnRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : CustomerReturnRefreshEventListenerMapper
 * Related DB Table  : customer_return_refresh_event_listeners
 * Related REST APIs : N/A
 * Depends On        : Analytics Module
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Sales Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.sales.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturnRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class CustomerReturnRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public CustomerReturnRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the customer return refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
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