/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : PickListRefreshEventListener.java
 * Purpose           : Component of Sales Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListRefreshEventListenerController
 * Related Service   : PickListRefreshEventListenerService, PickListRefreshEventListenerServiceImpl
 * Related Repository: PickListRefreshEventListenerRepository
 * Related Entity    : PickListRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : PickListRefreshEventListenerMapper
 * Related DB Table  : pick_list_refresh_event_listeners
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
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class PickListRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public PickListRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the pick list refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handlePickListRefresh(PickListRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_replenishment_metrics");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_turnover");
    }
}