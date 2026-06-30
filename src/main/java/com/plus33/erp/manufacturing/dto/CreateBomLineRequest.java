package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class CreateBomLineRequest {
    @NotNull private Long componentProductId;
    @NotNull @Positive private BigDecimal quantity;
    @NotNull private Long unitId;
    private Integer lineNumber;
    private Integer sortSequence;
    private String componentType = "COMPONENT";
    private Boolean phantom = false;
    private BigDecimal scrapPercentage = BigDecimal.ZERO;
    private BigDecimal yieldPercentage = new BigDecimal("100.00");
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long substituteProductId;
    private String notes;

    public CreateBomLineRequest() {}

    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }

    public Long getComponentProductId() { return componentProductId; }
    public void setComponentProductId(Long componentProductId) { this.componentProductId = componentProductId; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public Integer getSortSequence() { return sortSequence; }
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public Boolean getPhantom() { return phantom; }
    public void setPhantom(Boolean phantom) { this.phantom = phantom; }
    public BigDecimal getScrapPercentage() { return scrapPercentage; }
    public void setScrapPercentage(BigDecimal scrapPercentage) { this.scrapPercentage = scrapPercentage; }
    public BigDecimal getYieldPercentage() { return yieldPercentage; }
    public void setYieldPercentage(BigDecimal yieldPercentage) { this.yieldPercentage = yieldPercentage; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public Long getSubstituteProductId() { return substituteProductId; }
    public void setSubstituteProductId(Long substituteProductId) { this.substituteProductId = substituteProductId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
