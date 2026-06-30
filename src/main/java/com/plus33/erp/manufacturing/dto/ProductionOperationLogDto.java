package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionOperationLogDto {
    private Long id;
    private Long productionOrderId;
    private Integer operationSequence;
    private String operationName;
    private Long workCenterId;
    private String workCenterCode;
    private String status;
    private BigDecimal setupHoursPlanned;
    private BigDecimal setupHoursActual;
    private BigDecimal runHoursPlanned;
    private BigDecimal runHoursActual;
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Long operatorUserId;
    private String operatorNotes;

    public ProductionOperationLogDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public Integer getOperationSequence() { return operationSequence; }
    public void setOperationSequence(Integer operationSequence) { this.operationSequence = operationSequence; }
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    public String getWorkCenterCode() { return workCenterCode; }
    public void setWorkCenterCode(String workCenterCode) { this.workCenterCode = workCenterCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getSetupHoursPlanned() { return setupHoursPlanned; }
    public void setSetupHoursPlanned(BigDecimal setupHoursPlanned) { this.setupHoursPlanned = setupHoursPlanned; }
    public BigDecimal getSetupHoursActual() { return setupHoursActual; }
    public void setSetupHoursActual(BigDecimal setupHoursActual) { this.setupHoursActual = setupHoursActual; }
    public BigDecimal getRunHoursPlanned() { return runHoursPlanned; }
    public void setRunHoursPlanned(BigDecimal runHoursPlanned) { this.runHoursPlanned = runHoursPlanned; }
    public BigDecimal getRunHoursActual() { return runHoursActual; }
    public void setRunHoursActual(BigDecimal runHoursActual) { this.runHoursActual = runHoursActual; }
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
    public String getOperatorNotes() { return operatorNotes; }
    public void setOperatorNotes(String operatorNotes) { this.operatorNotes = operatorNotes; }
}
