package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class CompleteOperationRequest {
    @NotNull @PositiveOrZero private BigDecimal actualSetupHours;
    @NotNull @PositiveOrZero private BigDecimal actualRunHours;
    @NotNull @PositiveOrZero private BigDecimal yieldQuantity;
    @NotNull @PositiveOrZero private BigDecimal scrapQuantity;
    private Long machineId;
    private Long laborGroupId;
    private String notes;

    public CompleteOperationRequest() {}

    public BigDecimal getActualSetupHours() { return actualSetupHours; }
    public void setActualSetupHours(BigDecimal actualSetupHours) { this.actualSetupHours = actualSetupHours; }
    public BigDecimal getActualRunHours() { return actualRunHours; }
    public void setActualRunHours(BigDecimal actualRunHours) { this.actualRunHours = actualRunHours; }
    public BigDecimal getYieldQuantity() { return yieldQuantity; }
    public void setYieldQuantity(BigDecimal yieldQuantity) { this.yieldQuantity = yieldQuantity; }
    public BigDecimal getScrapQuantity() { return scrapQuantity; }
    public void setScrapQuantity(BigDecimal scrapQuantity) { this.scrapQuantity = scrapQuantity; }
    public Long getMachineId() { return machineId; }
    public void setMachineId(Long machineId) { this.machineId = machineId; }
    public Long getLaborGroupId() { return laborGroupId; }
    public void setLaborGroupId(Long laborGroupId) { this.laborGroupId = laborGroupId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
