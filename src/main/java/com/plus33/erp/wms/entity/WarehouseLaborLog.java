package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_labor_logs")
public class WarehouseLaborLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_type", nullable = false, length = 30)
    private String taskType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "travel_time_seconds")
    private Integer travelTimeSeconds = 0;

    @Column(name = "idle_time_seconds")
    private Integer idleTimeSeconds = 0;

    @Column(name = "items_handled", precision = 18, scale = 6)
    private BigDecimal itemsHandled = BigDecimal.ZERO;

    @Column(name = "labor_cost", precision = 18, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public String getTaskType() { return taskType; }
    public void setTaskType(String taskType) { this.taskType = taskType; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Integer getTravelTimeSeconds() { return travelTimeSeconds; }
    public void setTravelTimeSeconds(Integer travelTimeSeconds) { this.travelTimeSeconds = travelTimeSeconds; }
    public Integer getIdleTimeSeconds() { return idleTimeSeconds; }
    public void setIdleTimeSeconds(Integer idleTimeSeconds) { this.idleTimeSeconds = idleTimeSeconds; }
    public BigDecimal getItemsHandled() { return itemsHandled; }
    public void setItemsHandled(BigDecimal itemsHandled) { this.itemsHandled = itemsHandled; }
    public BigDecimal getLaborCost() { return laborCost; }
    public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
