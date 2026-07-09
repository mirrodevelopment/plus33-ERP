/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.event
 * File              : SupplierInvoiceRefreshEventListener.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceRefreshEventListenerController
 * Related Service   : SupplierInvoiceRefreshEventListenerService, SupplierInvoiceRefreshEventListenerServiceImpl
 * Related Repository: SupplierInvoiceRefreshEventListenerRepository
 * Related Entity    : SupplierInvoiceRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceRefreshEventListenerMapper
 * Related DB Table  : supplier_invoice_refresh_event_listeners
 * Related REST APIs : N/A
 * Depends On        : Analytics Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierInvoiceRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class SupplierInvoiceRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public SupplierInvoiceRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the supplier invoice refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
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