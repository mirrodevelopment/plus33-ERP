/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : InventoryRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Inventory Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryRefreshEventController
 * Related Service   : InventoryRefreshEventService, InventoryRefreshEventServiceImpl
 * Related Repository: InventoryRefreshEventRepository
 * Related Entity    : InventoryRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : InventoryRefreshEventMapper
 * Related DB Table  : inventory_refresh_events
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

public class InventoryRefreshEvent extends ApplicationEvent {
    public InventoryRefreshEvent(Object source) {
        super(source);
    }
}
