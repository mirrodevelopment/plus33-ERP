/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.event
 * File              : ProcurementRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Analytics Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementRefreshEventController
 * Related Service   : ProcurementRefreshEventService, ProcurementRefreshEventServiceImpl
 * Related Repository: ProcurementRefreshEventRepository
 * Related Entity    : ProcurementRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : ProcurementRefreshEventMapper
 * Related DB Table  : procurement_refresh_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Analytics Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Analytics Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.analytics.event;

import org.springframework.context.ApplicationEvent;

public class ProcurementRefreshEvent extends ApplicationEvent {
    public ProcurementRefreshEvent(Object source) {
        super(source);
    }
}
