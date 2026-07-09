/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : CustomerReturnRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Sales Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnRefreshEventController
 * Related Service   : CustomerReturnRefreshEventService, CustomerReturnRefreshEventServiceImpl
 * Related Repository: CustomerReturnRefreshEventRepository
 * Related Entity    : CustomerReturnRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : CustomerReturnRefreshEventMapper
 * Related DB Table  : customer_return_refresh_events
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

public class CustomerReturnRefreshEvent extends ApplicationEvent {
    public CustomerReturnRefreshEvent(Object source) {
        super(source);
    }
}
