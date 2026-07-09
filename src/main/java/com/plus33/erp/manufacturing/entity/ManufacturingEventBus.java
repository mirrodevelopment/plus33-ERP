/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingEventBus.java
 * Purpose           : Component of Manufacturing Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingEventBusController
 * Related Service   : ManufacturingEventBusService, ManufacturingEventBusServiceImpl
 * Related Repository: ManufacturingEventRepository
 * Related Entity    : ManufacturingEventBus
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingEventBusMapper
 * Related DB Table  : manufacturing_event_buss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Manufacturing Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Manufacturing Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.manufacturing.repository.ManufacturingEventRepository;
import com.plus33.erp.manufacturing.service.ManufacturingAnalyticsService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingEventBus}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Manufacturing Module.</p>
 *
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
public class ManufacturingEventBus {

    private final ManufacturingEventRepository eventRepository;
    private final ManufacturingAnalyticsService analyticsService;

    public ManufacturingEventBus(ManufacturingEventRepository eventRepository,
                                 ManufacturingAnalyticsService analyticsService) {
        this.eventRepository = eventRepository;
        this.analyticsService = analyticsService;
    }

    public static class MrpRunCompletedEvent {
        private final Long mrpRunId;
        private final Long companyId;

        public MrpRunCompletedEvent(Long mrpRunId, Long companyId) {
            this.mrpRunId = mrpRunId;
            this.companyId = companyId;
        }

        public Long getMrpRunId() { return mrpRunId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class PlannedOrderReleasedEvent {
        private final Long plannedOrderId;
        private final Long productionOrderId;

        public PlannedOrderReleasedEvent(Long plannedOrderId, Long productionOrderId) {
            this.plannedOrderId = plannedOrderId;
            this.productionOrderId = productionOrderId;
        }

        public Long getPlannedOrderId() { return plannedOrderId; }
        public Long getProductionOrderId() { return productionOrderId; }
    }

    public static class ProductionOrderReleasedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public ProductionOrderReleasedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class ProductionOrderCompletedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public ProductionOrderCompletedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class ProductionOrderCreatedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public ProductionOrderCreatedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class MaterialAllocatedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public MaterialAllocatedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class MaterialIssuedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public MaterialIssuedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class OperationStartedEvent {
        private final Long productionOrderId;
        private final Long operationId;
        private final Long companyId;

        public OperationStartedEvent(Long productionOrderId, Long operationId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.operationId = operationId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getOperationId() { return operationId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class OperationCompletedEvent {
        private final Long productionOrderId;
        private final Long operationId;
        private final Long companyId;

        public OperationCompletedEvent(Long productionOrderId, Long operationId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.operationId = operationId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getOperationId() { return operationId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class QualityApprovedEvent {
        private final Long inspectionId;
        private final Long companyId;

        public QualityApprovedEvent(Long inspectionId, Long companyId) {
            this.inspectionId = inspectionId;
            this.companyId = companyId;
        }

        public Long getInspectionId() { return inspectionId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class CostCalculatedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public CostCalculatedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class InventoryReceivedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public InventoryReceivedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    public static class WipClosedEvent {
        private final Long productionOrderId;
        private final Long companyId;

        public WipClosedEvent(Long productionOrderId, Long companyId) {
            this.productionOrderId = productionOrderId;
            this.companyId = companyId;
        }

        public Long getProductionOrderId() { return productionOrderId; }
        public Long getCompanyId() { return companyId; }
    }

    /**
     * Handles the mrp run completed event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleMrpRunCompleted(MrpRunCompletedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("MRP_RUN_COMPLETED");
        me.setReferenceType("MrpRun");
        me.setReferenceId(event.getMrpRunId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);

        // Refresh views
        analyticsService.refreshMaterializedViews();
    }

    /**
     * Handles the planned order released event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handlePlannedOrderReleased(PlannedOrderReleasedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(1L); // Default fallback company ID
        me.setEventType("PLANNED_ORDER_RELEASED");
        me.setReferenceType("MrpPlannedOrder");
        me.setReferenceId(event.getPlannedOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the production order released event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleProductionOrderReleased(ProductionOrderReleasedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("PRODUCTION_ORDER_RELEASED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);

        // Refresh views
        analyticsService.refreshMaterializedViews();
    }

    /**
     * Handles the production order completed event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleProductionOrderCompleted(ProductionOrderCompletedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("PRODUCTION_ORDER_COMPLETED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);

        // Refresh views
        analyticsService.refreshMaterializedViews();
    }

    /**
     * Handles the production order created event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleProductionOrderCreated(ProductionOrderCreatedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("PRODUCTION_ORDER_CREATED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the material allocated event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleMaterialAllocated(MaterialAllocatedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("MATERIAL_ALLOCATED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the material issued event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleMaterialIssued(MaterialIssuedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("MATERIAL_ISSUED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the operation started event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleOperationStarted(OperationStartedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("OPERATION_STARTED");
        me.setReferenceType("ProductionOrderOperation");
        me.setReferenceId(event.getOperationId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the operation completed event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleOperationCompleted(OperationCompletedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("OPERATION_COMPLETED");
        me.setReferenceType("ProductionOrderOperation");
        me.setReferenceId(event.getOperationId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the quality approved event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleQualityApproved(QualityApprovedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("QUALITY_APPROVED");
        me.setReferenceType("QualityInspection");
        me.setReferenceId(event.getInspectionId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the cost calculated event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleCostCalculated(CostCalculatedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("COST_CALCULATED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the inventory received event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleInventoryReceived(InventoryReceivedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("INVENTORY_RECEIVED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }

    /**
     * Handles the wip closed event or exception in the business workflow.
     *
     * @param event the event input value
     */
    @EventListener
    public void handleWipClosed(WipClosedEvent event) {
        ManufacturingEvent me = new ManufacturingEvent();
        me.setCompanyId(event.getCompanyId());
        me.setEventType("WIP_CLOSED");
        me.setReferenceType("ProductionOrder");
        me.setReferenceId(event.getProductionOrderId());
        me.setOccurredAt(LocalDateTime.now());
        eventRepository.save(me);
    }
}