/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : ReplenishmentRefreshEventListener.java
 * Purpose           : Component of Inventory Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRefreshEventListenerController
 * Related Service   : ReplenishmentRefreshEventListenerService, ReplenishmentRefreshEventListenerServiceImpl
 * Related Repository: ReplenishmentRefreshEventListenerRepository
 * Related Entity    : ReplenishmentRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentRefreshEventListenerMapper
 * Related DB Table  : replenishment_refresh_event_listeners
 * Related REST APIs : N/A
 * Depends On        : Analytics Module
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Inventory Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.inventory.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ReplenishmentRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public ReplenishmentRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the replenishment refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleReplenishmentRefresh(ReplenishmentRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_levels");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_procurement_summary");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_replenishment_metrics");
    }
}