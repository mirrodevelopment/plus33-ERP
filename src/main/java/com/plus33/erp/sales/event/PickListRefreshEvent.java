package com.plus33.erp.sales.event;

import org.springframework.context.ApplicationEvent;

public class PickListRefreshEvent extends ApplicationEvent {
    public PickListRefreshEvent(Object source) {
        super(source);
    }
}
