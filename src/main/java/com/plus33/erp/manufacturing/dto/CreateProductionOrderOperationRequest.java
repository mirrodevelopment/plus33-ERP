package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CreateProductionOrderOperationRequest {
    @NotNull private Integer operationNumber;
    private String operationCode;
    private String operationName;
    @NotNull private Long workCenterId;
    private Long machineId;
    @NotNull @PositiveOrZero private BigDecimal estimatedSetupHours;
    @NotNull @PositiveOrZero private BigDecimal estimatedRunHours;
    private String description;

    public CreateProductionOrderOperationRequest() {}

    public Integer getOperationNumber() { return operationNumber; }
    public void setOperationNumber(Integer operationNumber) { this.operationNumber = operationNumber; }
    public String getOperationCode() { return operationCode; }
    public void setOperationCode(String operationCode) { this.operationCode = operationCode; }
    public String getOperationName() { return operationName; }
    public void setOperationName(String operationName) { this.operationName = operationName; }
    public Long getWorkCenterId() { return workCenterId; }
    public void setWorkCenterId(Long workCenterId) { this.workCenterId = workCenterId; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public BigDecimal getEstimatedSetupHours() { return estimatedSetupHours; }
    public void setEstimatedSetupHours(BigDecimal estimatedSetupHours) { this.estimatedSetupHours = estimatedSetupHours; }
    public BigDecimal getEstimatedRunHours() { return estimatedRunHours; }
    public void setEstimatedRunHours(BigDecimal estimatedRunHours) { this.estimatedRunHours = estimatedRunHours; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
