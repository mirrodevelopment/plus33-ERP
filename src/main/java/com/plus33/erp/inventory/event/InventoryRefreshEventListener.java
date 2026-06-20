package com.plus33.erp.inventory.event;

import com.plus33.erp.analytics.service.AnalyticsRefreshService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class InventoryRefreshEventListener {

    private final AnalyticsRefreshService analyticsRefreshService;

    public InventoryRefreshEventListener(AnalyticsRefreshService analyticsRefreshService) {
        this.analyticsRefreshService = analyticsRefreshService;
    }

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleInventoryRefresh(InventoryRefreshEvent event) {
        analyticsRefreshService.refreshViewWithNewTransaction("mv_inventory_levels");
    }
}
