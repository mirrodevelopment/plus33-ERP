package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class ReplenishmentRefreshEvent extends ApplicationEvent {
    public ReplenishmentRefreshEvent(Object source) {
        super(source);
    }
}
