package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionOrderOperationDto {
    private Long id;
    private Long productionOrderId;
    private Long routingOperationId;
    private Integer operationNumber;
    private String operationCode;
    private String description;
    private Long workCenterId;
    private String workCenterCode;
    private String workCenterName;
    private Long machineId;
    private String machineCode;
    private String status;
    private BigDecimal plannedQuantity;
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private BigDecimal plannedSetupHours;
    private BigDecimal actualSetupHours;
    private BigDecimal plannedRunHours;
    private BigDecimal actualRunHours;
    private LocalDateTime plannedStartDatetime;
    private LocalDateTime plannedEndDatetime;
    private LocalDateTime actualStartDatetime;
    private LocalDateTime actualEndDatetime;

    public ProductionOrderOperationDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public Long getRoutingOperationId() { return routingOperationId; }
    public void setRoutingOperationId(Long routingOperationId) { this.routingOperationId = routingOperationId; }
    public Integer getOperationNumber() { return operationNumber; }
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    public String getOperationCode() { return operationCode; }
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    public String getWorkCenterCode() { return workCenterCode; }
    public void setWorkCenterCode(String workCenterCode) { this.workCenterCode = workCenterCode; }
    public String getWorkCenterName() { return workCenterName; }
    public void setWorkCenterName(String workCenterName) { this.workCenterName = workCenterName; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public String getMachineCode() { return machineCode; }
    public void setMachineCode(String machineCode) { this.machineCode = machineCode; }
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
}
