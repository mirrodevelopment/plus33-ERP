package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class LogOperationRequest {
    @NotNull private Integer operationSequence;
    @NotBlank private String operationName;
    @NotNull private Long workCenterId;
    @NotNull private String action; // START, COMPLETE, PAUSE
    private BigDecimal completedQuantity;
    private BigDecimal scrappedQuantity;
    private BigDecimal actualSetupHours;
    private BigDecimal actualRunHours;
    private String operatorNotes;
    @NotNull private Long operatorUserId;

    public LogOperationRequest() {}

    public Integer getOperationSequence() { return operationSequence; }
    public void setOperationSequence(Integer operationSequence) { this.operationSequence = operationSequence; }
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
    public BigDecimal getCompletedQuantity() { return completedQuantity; }
    public void setCompletedQuantity(BigDecimal completedQuantity) { this.completedQuantity = completedQuantity; }
    public BigDecimal getScrappedQuantity() { return scrappedQuantity; }
    public void setScrappedQuantity(BigDecimal scrappedQuantity) { this.scrappedQuantity = scrappedQuantity; }
    public BigDecimal getActualSetupHours() { return actualSetupHours; }
    public void setActualSetupHours(BigDecimal actualSetupHours) { this.actualSetupHours = actualSetupHours; }
    public BigDecimal getActualRunHours() { return actualRunHours; }
    public void setActualRunHours(BigDecimal actualRunHours) { this.actualRunHours = actualRunHours; }
    public String getOperatorNotes() { return operatorNotes; }
    public void setOperatorNotes(String operatorNotes) { this.operatorNotes = operatorNotes; }
    public Long getOperatorUserId() { return operatorUserId; }
    public void setOperatorUserId(Long operatorUserId) { this.operatorUserId = operatorUserId; }
}
