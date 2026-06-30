package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.manufacturing.repository.ManufacturingEventRepository;
import com.plus33.erp.manufacturing.service.ManufacturingAnalyticsService;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
