package com.plus33.erp.inventory.event;

import org.springframework.context.ApplicationEvent;

public class InventoryAdjustmentRefreshEvent extends ApplicationEvent {
    public InventoryAdjustmentRefreshEvent(Object source) {
        super(source);
    }
}
