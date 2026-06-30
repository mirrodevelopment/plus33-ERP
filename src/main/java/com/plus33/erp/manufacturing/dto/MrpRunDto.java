package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class MrpRunDto {
    private Long id;
    private Long companyId;
    private String runNumber;
    private LocalDate horizonStartDate;
    private LocalDate horizonEndDate;
    private String status;
    private Integer itemsProcessed;
    private Integer ordersGenerated;
    private Long initiatedBy;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private String executionLog;

    public MrpRunDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getRunNumber() { return runNumber; }
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    public LocalDate getHorizonStartDate() { return horizonStartDate; }
    public void setHorizonStartDate(LocalDate horizonStartDate) { this.horizonStartDate = horizonStartDate; }
    public LocalDate getHorizonEndDate() { return horizonEndDate; }
    public void setHorizonEndDate(LocalDate horizonEndDate) { this.horizonEndDate = horizonEndDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getItemsProcessed() { return itemsProcessed; }
    public void setItemsProcessed(Integer itemsProcessed) { this.itemsProcessed = itemsProcessed; }
    public Integer getOrdersGenerated() { return ordersGenerated; }
    public void setOrdersGenerated(Integer ordersGenerated) { this.ordersGenerated = ordersGenerated; }
    public Long getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(Long initiatedBy) { this.initiatedBy = initiatedBy; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getExecutionLog() { return executionLog; }
    public void setExecutionLog(String executionLog) { this.executionLog = executionLog; }
}
