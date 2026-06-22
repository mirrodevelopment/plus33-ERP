package com.plus33.erp.analytics.event;

import org.springframework.context.ApplicationEvent;

public class ProcurementRefreshEvent extends ApplicationEvent {
    public ProcurementRefreshEvent(Object source) {
        super(source);
    }
}
