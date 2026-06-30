package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_order_operations")
public class ProductionOrderOperation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "routing_operation_id")
    private RoutingOperation routingOperation;

    @Column(name = "operation_number", nullable = false)
    private Integer operationNumber;

    @Column(name = "operation_code", nullable = false, length = 50)
    private String operationCode;

    @Column(length = 255)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "machine_id")
    private Machine machine;

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, QUEUED, SETUP, IN_PROGRESS, COMPLETED, SKIPPED, SCRAPPED

    @Column(name = "planned_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal plannedQuantity;

    @Column(name = "completed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal completedQuantity = BigDecimal.ZERO;

    @Column(name = "scrapped_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrappedQuantity = BigDecimal.ZERO;

    @Column(name = "planned_setup_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal plannedSetupHours = BigDecimal.ZERO;

    @Column(name = "actual_setup_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualSetupHours = BigDecimal.ZERO;

    @Column(name = "planned_run_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal plannedRunHours = BigDecimal.ZERO;

    @Column(name = "actual_run_hours", nullable = false, precision = 10, scale = 4)
    private BigDecimal actualRunHours = BigDecimal.ZERO;

    @Column(name = "planned_start_datetime")
    private LocalDateTime plannedStartDatetime;

    @Column(name = "planned_end_datetime")
    private LocalDateTime plannedEndDatetime;

    @Column(name = "actual_start_datetime")
    private LocalDateTime actualStartDatetime;

    @Column(name = "actual_end_datetime")
    private LocalDateTime actualEndDatetime;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionOrderOperation() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public RoutingOperation getRoutingOperation() { return routingOperation; }
    public void setRoutingOperation(RoutingOperation routingOperation) { this.routingOperation = routingOperation; }
    public Integer getOperationNumber() { return operationNumber; }
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    public String getOperationCode() { return operationCode; }
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public Machine getMachine() { return machine; }
    public void setMachine(Machine machine) { this.machine = machine; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getPlannedQuantity() { return plannedQuantity; }
    public void setPlannedQuantity(BigDecimal plannedQuantity) { this.plannedQuantity = plannedQuantity; }
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    public BigDecimal getPlannedSetupHours() { return plannedSetupHours; }
    public void setPlannedSetupHours(BigDecimal plannedSetupHours) { this.plannedSetupHours = plannedSetupHours; }
    public BigDecimal getActualSetupHours() { return actualSetupHours; }
    public void setActualSetupHours(BigDecimal actualSetupHours) { this.actualSetupHours = actualSetupHours; }
    public BigDecimal getPlannedRunHours() { return plannedRunHours; }
    public void setPlannedRunHours(BigDecimal plannedRunHours) { this.plannedRunHours = plannedRunHours; }
    public BigDecimal getActualRunHours() { return actualRunHours; }
    public void setActualRunHours(BigDecimal actualRunHours) { this.actualRunHours = actualRunHours; }
    public LocalDateTime getPlannedStartDatetime() { return plannedStartDatetime; }
    public void setPlannedStartDatetime(LocalDateTime plannedStartDatetime) { this.plannedStartDatetime = plannedStartDatetime; }
    public LocalDateTime getPlannedEndDatetime() { return plannedEndDatetime; }
    public void setPlannedEndDatetime(LocalDateTime plannedEndDatetime) { this.plannedEndDatetime = plannedEndDatetime; }
    public LocalDateTime getActualStartDatetime() { return actualStartDatetime; }
    public void setActualStartDatetime(LocalDateTime actualStartDatetime) { this.actualStartDatetime = actualStartDatetime; }
    public LocalDateTime getActualEndDatetime() { return actualEndDatetime; }
    public void setActualEndDatetime(LocalDateTime actualEndDatetime) { this.actualEndDatetime = actualEndDatetime; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
