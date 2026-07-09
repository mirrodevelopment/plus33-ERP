/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.event
 * File              : SupplierInvoiceRefreshEvent.java
 * Purpose           : Spring ApplicationEvent published by Finance Module for async processing
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierInvoiceRefreshEventController
 * Related Service   : SupplierInvoiceRefreshEventService, SupplierInvoiceRefreshEventServiceImpl
 * Related Repository: SupplierInvoiceRefreshEventRepository
 * Related Entity    : SupplierInvoiceRefreshEvent
 * Related DTO       : N/A
 * Related Mapper    : SupplierInvoiceRefreshEventMapper
 * Related DB Table  : supplier_invoice_refresh_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Spring ApplicationEvent for Finance Module. Published on state changes. Consumed by @EventListener methods for async processing.
 ******************************************************************************/
package com.plus33.erp.finance.event;

import org.springframework.context.ApplicationEvent;

public class SupplierInvoiceRefreshEvent extends ApplicationEvent {
    public SupplierInvoiceRefreshEvent(Object source) {
        super(source);
    }
}
