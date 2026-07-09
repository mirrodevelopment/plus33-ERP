/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : InventoryRefreshEventListener.java
 * Purpose           : Component of Inventory Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRefreshEventListenerController
 * Related Service   : InventoryRefreshEventListenerService, InventoryRefreshEventListenerServiceImpl
 * Related Repository: InventoryRefreshEventListenerRepository
 * Related Entity    : InventoryRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : InventoryRefreshEventListenerMapper
 * Related DB Table  : inventory_refresh_event_listeners
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
import com.plus33.erp.inventory.event.InventoryRefreshEvent;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class InventoryRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public InventoryRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the inventory refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInventoryRefresh(InventoryRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_levels");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_aging_expiry");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_traceability_metrics");
    }
}