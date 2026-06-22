package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class InventoryTraceabilityRefreshEvent extends ApplicationEvent {
    public InventoryTraceabilityRefreshEvent(Object source) {
        super(source);
    }
}
