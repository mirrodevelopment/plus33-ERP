package com.plus33.erp.wms.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * WMS Event Listener — refreshes analytics materialized views after domain events.
 * Runs in a separate REQUIRES_NEW transaction so analytics failures never
 * roll back the main WMS operation.
 */
@Component
public class WmsEventListener {

    private static final Logger log = LoggerFactory.getLogger(WmsEventListener.class);

    private final JdbcTemplate jdbc;

    public WmsEventListener(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onMovementCreated(WmsEventBus.InventoryMovementCreatedEvent event) {
        refreshViewSafe("mv_inventory_turnover");
        refreshViewSafe("mv_wms_inventory_turnover");
        refreshViewSafe("mv_inventory_accuracy");
        log.info("WMS: Movement ledger event processed - type={}, movementId={}", event.movementType(), event.movementId());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPickingCompleted(WmsEventBus.PickingCompletedEvent event) {
        refreshViewSafe("mv_pick_rate");
        log.info("WMS: Picking completed event - pickingWorkId={}", event.pickingWorkId());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPutAwayCompleted(WmsEventBus.PutAwayCompletedEvent event) {
        refreshViewSafe("mv_putaway_rate");
        refreshViewSafe("mv_space_utilization");
        log.info("WMS: Put-away completed event - putAwayWorkId={}", event.putAwayWorkId());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onShipmentDispatched(WmsEventBus.ShipmentDispatchedEvent event) {
        refreshViewSafe("mv_order_fulfillment");
        log.info("WMS: Shipment dispatched - shipmentId={}, tracking={}", event.shipmentId(), event.trackingNumber());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onCycleCountCompleted(WmsEventBus.CycleCountCompletedEvent event) {
        refreshViewSafe("mv_inventory_accuracy");
        refreshViewSafe("mv_cycle_count_variance");
        log.info("WMS: Cycle count completed - planId={}", event.planId());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onInventoryAdjusted(WmsEventBus.InventoryAdjustedEvent event) {
        refreshViewSafe("mv_inventory_accuracy");
        refreshViewSafe("mv_space_utilization");
        log.info("WMS: Inventory adjusted - locationId={}, productId={}, variance={}", event.locationId(), event.productId(), event.varianceQty());
    }

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onManufacturingMaterialIssued(WmsEventBus.ManufacturingMaterialIssuedEvent event) {
        refreshViewSafe("mv_inventory_turnover");
        refreshViewSafe("mv_wms_inventory_turnover");
        log.info("WMS: Manufacturing material issued - productionOrderId={}, productId={}, qty={}",
                event.productionOrderId(), event.productId(), event.issuedQty());
    }

    private void refreshViewSafe(String viewName) {
        try {
            boolean exists = jdbc.queryForObject(
                    "SELECT COUNT(*) FROM pg_class WHERE relname = ? AND relkind = 'm'",
                    Integer.class, viewName) > 0;
            if (exists) {
                jdbc.execute("REFRESH MATERIALIZED VIEW CONCURRENTLY " + viewName);
            }
        } catch (Exception ex) {
            log.warn("WMS: Materialized view refresh skipped for {}: {}", viewName, ex.getMessage());
        }
    }
}
