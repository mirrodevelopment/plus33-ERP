/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.event
 * File              : ReplenishmentRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Inventory Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRefreshEventController
 * Related Service   : ReplenishmentRefreshEventService, ReplenishmentRefreshEventServiceImpl
 * Related Repository: ReplenishmentRefreshEventRepository
 * Related Entity    : ReplenishmentRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentRefreshEventMapper
 * Related DB Table  : replenishment_refresh_events
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

public class ReplenishmentRefreshEvent extends ApplicationEvent {
    public ReplenishmentRefreshEvent(Object source) {
        super(source);
    }
}
