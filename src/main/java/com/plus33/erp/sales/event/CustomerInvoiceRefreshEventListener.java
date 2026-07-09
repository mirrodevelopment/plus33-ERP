/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : CustomerInvoiceRefreshEventListener.java
 * Purpose           : Component of Sales Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceRefreshEventListenerController
 * Related Service   : CustomerInvoiceRefreshEventListenerService, CustomerInvoiceRefreshEventListenerServiceImpl
 * Related Repository: CustomerInvoiceRefreshEventListenerRepository
 * Related Entity    : CustomerInvoiceRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : CustomerInvoiceRefreshEventListenerMapper
 * Related DB Table  : customer_invoice_refresh_event_listeners
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
 * <p><b>Class  :</b> {@code CustomerInvoiceRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class CustomerInvoiceRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public CustomerInvoiceRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the customer invoice refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCustomerInvoiceRefresh(CustomerInvoiceRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_sales_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_receivables_dashboard");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_customer_aging");
    }
}