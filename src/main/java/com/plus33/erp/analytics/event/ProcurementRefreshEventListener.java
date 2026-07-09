/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.event
 * File              : ProcurementRefreshEventListener.java
 * Purpose           : Component of Analytics Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementRefreshEventListenerController
 * Related Service   : ProcurementRefreshEventListenerService, ProcurementRefreshEventListenerServiceImpl
 * Related Repository: ProcurementRefreshEventListenerRepository
 * Related Entity    : ProcurementRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : ProcurementRefreshEventListenerMapper
 * Related DB Table  : procurement_refresh_event_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Analytics Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.analytics.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Analytics Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.analytics.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Analytics Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ProcurementRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public ProcurementRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the procurement refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleProcurementRefresh(ProcurementRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_procurement_summary");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_supplier_performance");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_payables_aging");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_po_fulfilment");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_invoice_matching");
    }
}