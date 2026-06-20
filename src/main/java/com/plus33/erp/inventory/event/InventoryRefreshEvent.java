package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class InventoryRefreshEvent extends ApplicationEvent {
    public InventoryRefreshEvent(Object source) {
        super(source);
    }
}
