/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.event
 * File              : CustomerInvoiceRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Sales Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerInvoiceRefreshEventController
 * Related Service   : CustomerInvoiceRefreshEventService, CustomerInvoiceRefreshEventServiceImpl
 * Related Repository: CustomerInvoiceRefreshEventRepository
 * Related Entity    : CustomerInvoiceRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : CustomerInvoiceRefreshEventMapper
 * Related DB Table  : customer_invoice_refresh_events
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

public class CustomerInvoiceRefreshEvent extends ApplicationEvent {
    public CustomerInvoiceRefreshEvent(Object source) {
        super(source);
    }
}
