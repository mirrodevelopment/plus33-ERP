package com.plus33.erp.sales.event;

import org.springframework.context.ApplicationEvent;

public class CustomerReturnRefreshEvent extends ApplicationEvent {
    public CustomerReturnRefreshEvent(Object source) {
        super(source);
    }
}
