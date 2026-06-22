package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class InventoryTransferRefreshEvent extends ApplicationEvent {
    public InventoryTransferRefreshEvent(Object source) {
        super(source);
    }
}
