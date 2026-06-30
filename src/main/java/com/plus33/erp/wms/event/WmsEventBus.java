package com.plus33.erp.wms.event;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * WMS Event Bus — typed domain event publisher for all warehouse operations.
 * Listeners receive events through Spring's {@link org.springframework.context.event.EventListener}
 * and run analytics refreshes in a {@code REQUIRES_NEW} transaction to prevent
 * analytics failures from rolling back the main operation.
 */
@Component
public class WmsEventBus {

    private final ApplicationEventPublisher publisher;

    public WmsEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    // ============================================================
    // INBOUND EVENTS
    // ============================================================

    public void publishAsnReceived(Long companyId, Long asnId, Long warehouseId) {
        publisher.publishEvent(new AsnReceivedEvent(companyId, asnId, warehouseId));
    }

    public void publishPutAwayCompleted(Long companyId, Long putAwayWorkId, Long toLocationId) {
        publisher.publishEvent(new PutAwayCompletedEvent(companyId, putAwayWorkId, toLocationId));
    }

    // ============================================================
    // INVENTORY EVENTS
    // ============================================================

    public void publishInventoryReserved(Long companyId, Long reservationId, Long productId, BigDecimal qty) {
        publisher.publishEvent(new InventoryReservedEvent(companyId, reservationId, productId, qty));
    }

    public void publishInventoryMovementCreated(Long companyId, Long movementId, String movementType) {
        publisher.publishEvent(new InventoryMovementCreatedEvent(companyId, movementId, movementType));
    }

    public void publishInventoryAdjusted(Long companyId, Long locationId, Long productId, BigDecimal varianceQty) {
        publisher.publishEvent(new InventoryAdjustedEvent(companyId, locationId, productId, varianceQty));
    }

    // ============================================================
    // OUTBOUND EVENTS
    // ============================================================

    public void publishWaveCreated(Long companyId, Long waveId, Long warehouseId) {
        publisher.publishEvent(new WaveCreatedEvent(companyId, waveId, warehouseId));
    }

    public void publishPickingCompleted(Long companyId, Long pickingWorkId) {
        publisher.publishEvent(new PickingCompletedEvent(companyId, pickingWorkId));
    }

    public void publishShipmentDispatched(Long companyId, Long shipmentId, String trackingNumber) {
        publisher.publishEvent(new ShipmentDispatchedEvent(companyId, shipmentId, trackingNumber));
    }

    // ============================================================
    // CROSS-DOCK & REPLENISHMENT EVENTS
    // ============================================================

    public void publishCrossDockCompleted(Long companyId, Long crossDockOrderId) {
        publisher.publishEvent(new CrossDockCompletedEvent(companyId, crossDockOrderId));
    }

    public void publishReplenishmentTriggered(Long companyId, Long replenishmentTaskId, Long productId) {
        publisher.publishEvent(new ReplenishmentTriggeredEvent(companyId, replenishmentTaskId, productId));
    }

    // ============================================================
    // CYCLE COUNT EVENTS
    // ============================================================

    public void publishCycleCountCompleted(Long companyId, Long planId, Long warehouseId) {
        publisher.publishEvent(new CycleCountCompletedEvent(companyId, planId, warehouseId));
    }

    // ============================================================
    // MANUFACTURING INTEGRATION EVENT
    // ============================================================

    public void publishManufacturingMaterialIssued(Long companyId, Long productionOrderId,
                                                    Long productId, BigDecimal issuedQty) {
        publisher.publishEvent(new ManufacturingMaterialIssuedEvent(companyId, productionOrderId,
                productId, issuedQty));
    }

    // ============================================================
    // DOMAIN EVENT RECORDS
    // ============================================================

    public record AsnReceivedEvent(Long companyId, Long asnId, Long warehouseId) {}
    public record PutAwayCompletedEvent(Long companyId, Long putAwayWorkId, Long toLocationId) {}
    public record InventoryReservedEvent(Long companyId, Long reservationId, Long productId, BigDecimal qty) {}
    public record InventoryMovementCreatedEvent(Long companyId, Long movementId, String movementType) {}
    public record InventoryAdjustedEvent(Long companyId, Long locationId, Long productId, BigDecimal varianceQty) {}
    public record WaveCreatedEvent(Long companyId, Long waveId, Long warehouseId) {}
    public record PickingCompletedEvent(Long companyId, Long pickingWorkId) {}
    public record ShipmentDispatchedEvent(Long companyId, Long shipmentId, String trackingNumber) {}
    public record CrossDockCompletedEvent(Long companyId, Long crossDockOrderId) {}
    public record ReplenishmentTriggeredEvent(Long companyId, Long replenishmentTaskId, Long productId) {}
    public record CycleCountCompletedEvent(Long companyId, Long planId, Long warehouseId) {}
    public record ManufacturingMaterialIssuedEvent(Long companyId, Long productionOrderId,
                                                    Long productId, BigDecimal issuedQty) {}
}
