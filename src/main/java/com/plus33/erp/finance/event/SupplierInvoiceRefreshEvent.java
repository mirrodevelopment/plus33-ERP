package com.plus33.erp.finance.event;

import org.springframework.context.ApplicationEvent;

public class SupplierInvoiceRefreshEvent extends ApplicationEvent {
    public SupplierInvoiceRefreshEvent(Object source) {
        super(source);
    }
}
