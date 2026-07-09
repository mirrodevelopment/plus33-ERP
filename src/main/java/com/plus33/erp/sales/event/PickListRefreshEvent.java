/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : PickListRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Sales Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListRefreshEventController
 * Related Service   : PickListRefreshEventService, PickListRefreshEventServiceImpl
 * Related Repository: PickListRefreshEventRepository
 * Related Entity    : PickListRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : PickListRefreshEventMapper
 * Related DB Table  : pick_list_refresh_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Sales Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Sales Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.sales.event;

import org.springframework.context.ApplicationEvent;

public class PickListRefreshEvent extends ApplicationEvent {
    public PickListRefreshEvent(Object source) {
        super(source);
    }
}
