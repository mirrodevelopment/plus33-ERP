package com.plus33.erp.sales.event;

import org.springframework.context.ApplicationEvent;

public class CustomerInvoiceRefreshEvent extends ApplicationEvent {
    public CustomerInvoiceRefreshEvent(Object source) {
        super(source);
    }
}
