/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : InventoryTransferRefreshEventListener.java
 * Purpose           : Component of Inventory Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferRefreshEventListenerController
 * Related Service   : InventoryTransferRefreshEventListenerService, InventoryTransferRefreshEventListenerServiceImpl
 * Related Repository: InventoryTransferRefreshEventListenerRepository
 * Related Entity    : InventoryTransferRefreshEventListener
 * Related DTO       : N/A
 * Related Mapper    : InventoryTransferRefreshEventListenerMapper
 * Related DB Table  : inventory_transfer_refresh_event_listeners
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
 * <p><b>Class  :</b> {@code InventoryTransferRefreshEventListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.event}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Analytics</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class InventoryTransferRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public InventoryTransferRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    /**
     * Handles the inventory transfer refresh event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInventoryTransferRefresh(InventoryTransferRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_levels");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_procurement_summary");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_kpis");
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_slow_dead");
    }
}