/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : InventoryTransferRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Inventory Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTransferRefreshEventController
 * Related Service   : InventoryTransferRefreshEventService, InventoryTransferRefreshEventServiceImpl
 * Related Repository: InventoryTransferRefreshEventRepository
 * Related Entity    : InventoryTransferRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : InventoryTransferRefreshEventMapper
 * Related DB Table  : inventory_transfer_refresh_events
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

public class InventoryTransferRefreshEvent extends ApplicationEvent {
    public InventoryTransferRefreshEvent(Object source) {
        super(source);
    }
}
