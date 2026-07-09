/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.event
 * File              : WmsEventBus.java
 * Purpose           : Component of Wms Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WmsEventBusController
 * Related Service   : WmsEventBusService, WmsEventBusServiceImpl
 * Related Repository: WmsEventBusRepository
 * Related Entity    : WmsEventBus
 * Related DTO       : N/A
 * Related Mapper    : WmsEventBusMapper
 * Related DB Table  : wms_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param asnId the asnId input value
     * @param warehouseId the warehouseId input value
     */
    public void publishAsnReceived(Long companyId, Long asnId, Long warehouseId) {
        publisher.publishEvent(new AsnReceivedEvent(companyId, asnId, warehouseId));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param putAwayWorkId the putAwayWorkId input value
     * @param toLocationId the toLocationId input value
     */
    public void publishPutAwayCompleted(Long companyId, Long putAwayWorkId, Long toLocationId) {
        publisher.publishEvent(new PutAwayCompletedEvent(companyId, putAwayWorkId, toLocationId));
    }

    // ============================================================
    // INVENTORY EVENTS
    // ============================================================

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param reservationId the reservationId input value
     * @param productId the productId input value
     * @param qty the qty input value
     */
    public void publishInventoryReserved(Long companyId, Long reservationId, Long productId, BigDecimal qty) {
        publisher.publishEvent(new InventoryReservedEvent(companyId, reservationId, productId, qty));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param movementId the movementId input value
     * @param movementType the movementType input value
     */
    public void publishInventoryMovementCreated(Long companyId, Long movementId, String movementType) {
        publisher.publishEvent(new InventoryMovementCreatedEvent(companyId, movementId, movementType));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param locationId the locationId input value
     * @param productId the productId input value
     * @param varianceQty the varianceQty input value
     */
    public void publishInventoryAdjusted(Long companyId, Long locationId, Long productId, BigDecimal varianceQty) {
        publisher.publishEvent(new InventoryAdjustedEvent(companyId, locationId, productId, varianceQty));
    }

    // ============================================================
    // OUTBOUND EVENTS
    // ============================================================

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param waveId the waveId input value
     * @param warehouseId the warehouseId input value
     */
    public void publishWaveCreated(Long companyId, Long waveId, Long warehouseId) {
        publisher.publishEvent(new WaveCreatedEvent(companyId, waveId, warehouseId));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param pickingWorkId the pickingWorkId input value
     */
    public void publishPickingCompleted(Long companyId, Long pickingWorkId) {
        publisher.publishEvent(new PickingCompletedEvent(companyId, pickingWorkId));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param shipmentId the shipmentId input value
     * @param trackingNumber the trackingNumber input value
     */
    public void publishShipmentDispatched(Long companyId, Long shipmentId, String trackingNumber) {
        publisher.publishEvent(new ShipmentDispatchedEvent(companyId, shipmentId, trackingNumber));
    }

    // ============================================================
    // CROSS-DOCK & REPLENISHMENT EVENTS
    // ============================================================

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param crossDockOrderId the crossDockOrderId input value
     */
    public void publishCrossDockCompleted(Long companyId, Long crossDockOrderId) {
        publisher.publishEvent(new CrossDockCompletedEvent(companyId, crossDockOrderId));
    }

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param replenishmentTaskId the replenishmentTaskId input value
     * @param productId the productId input value
     */
    public void publishReplenishmentTriggered(Long companyId, Long replenishmentTaskId, Long productId) {
        publisher.publishEvent(new ReplenishmentTriggeredEvent(companyId, replenishmentTaskId, productId));
    }

    // ============================================================
    // CYCLE COUNT EVENTS
    // ============================================================

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param planId the planId input value
     * @param warehouseId the warehouseId input value
     */
    public void publishCycleCountCompleted(Long companyId, Long planId, Long warehouseId) {
        publisher.publishEvent(new CycleCountCompletedEvent(companyId, planId, warehouseId));
    }

    // ============================================================
    // MANUFACTURING INTEGRATION EVENT
    // ============================================================

    /**
     * Publishes a domain event to notify dependent modules of the state change.
     *
     */
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
