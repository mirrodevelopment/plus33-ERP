package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mrp_runs")
public class MrpRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "run_number", nullable = false, length = 50)
    private String runNumber;

    @Column(name = "run_type", nullable = false, length = 30)
    private String runType = "REGENERATIVE"; // REGENERATIVE, NET_CHANGE, NET_CHANGE_PLANNED

    @Column(nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, RUNNING, COMPLETED, FAILED, CANCELLED

    @Column(name = "planning_horizon_days", nullable = false)
    private Integer planningHorizonDays = 90;

    @Column(name = "include_forecasts", nullable = false)
    private Boolean includeForecasts = true;

    @Column(name = "include_sales_orders", nullable = false)
    private Boolean includeSalesOrders = true;

    @Column(name = "include_safety_stock", nullable = false)
    private Boolean includeSafetyStock = true;

    @Column(name = "planned_orders_generated", nullable = false)
    private Integer plannedOrdersGenerated = 0;

    @Column(name = "purchase_reqs_generated", nullable = false)
    private Integer purchaseReqsGenerated = 0;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "run_by", nullable = false)
    private Long runBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public MrpRun() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getRunNumber() { return runNumber; }
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    public String getRunType() { return runType; }
    public void setRunType(String runType) { this.runType = runType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getPlanningHorizonDays() { return planningHorizonDays; }
    public void setPlanningHorizonDays(Integer planningHorizonDays) { this.planningHorizonDays = planningHorizonDays; }
    public Boolean getIncludeForecasts() { return includeForecasts; }
    public void setIncludeForecasts(Boolean includeForecasts) { this.includeForecasts = includeForecasts; }
    public Boolean getIncludeSalesOrders() { return includeSalesOrders; }
    public void setIncludeSalesOrders(Boolean includeSalesOrders) { this.includeSalesOrders = includeSalesOrders; }
    public Boolean getIncludeSafetyStock() { return includeSafetyStock; }
    public void setIncludeSafetyStock(Boolean includeSafetyStock) { this.includeSafetyStock = includeSafetyStock; }
    public Integer getPlannedOrdersGenerated() { return plannedOrdersGenerated; }
    public void setPlannedOrdersGenerated(Integer plannedOrdersGenerated) { this.plannedOrdersGenerated = plannedOrdersGenerated; }
    public Integer getPurchaseReqsGenerated() { return purchaseReqsGenerated; }
    public void setPurchaseReqsGenerated(Integer purchaseReqsGenerated) { this.purchaseReqsGenerated = purchaseReqsGenerated; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    public Long getRunBy() { return runBy; }
    public void setRunBy(Long runBy) { this.runBy = runBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Getter/setter aliases for compatibility
    public Long getExecutedBy() { return runBy; }
    public void setExecutedBy(Long executedBy) { this.runBy = executedBy; }
    public Integer getItemsProcessed() { return plannedOrdersGenerated; }
    public void setItemsProcessed(Integer itemsProcessed) { this.plannedOrdersGenerated = itemsProcessed; }
    public Integer getOrdersGenerated() { return plannedOrdersGenerated; }
    public void setOrdersGenerated(Integer ordersGenerated) { this.plannedOrdersGenerated = ordersGenerated; }
}
