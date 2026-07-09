/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : InventoryAdjustmentRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Inventory Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAdjustmentRefreshEventController
 * Related Service   : InventoryAdjustmentRefreshEventService, InventoryAdjustmentRefreshEventServiceImpl
 * Related Repository: InventoryAdjustmentRefreshEventRepository
 * Related Entity    : InventoryAdjustmentRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : InventoryAdjustmentRefreshEventMapper
 * Related DB Table  : inventory_adjustment_refresh_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Inventory Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Inventory Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class InventoryAdjustmentRefreshEvent extends ApplicationEvent {
    public InventoryAdjustmentRefreshEvent(Object source) {
        super(source);
    }
}
