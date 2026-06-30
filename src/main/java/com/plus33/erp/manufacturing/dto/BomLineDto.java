package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class BomLineDto {
    private Long id;
    private Long bomHeaderId;
    private Integer lineNumber;
    private Integer sortSequence;
    private Long componentProductId;
    private String componentProductCode;
    private String componentProductName;
    private BigDecimal quantity;
    private String unitCode;
    private String componentType;
    private Boolean phantom;
    private BigDecimal scrapPercentage;
    private BigDecimal yieldPercentage;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long substituteProductId;
    private String notes;
    private Long unitId;

    public BomLineDto() {}

    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getBomHeaderId() { return bomHeaderId; }
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
    public Integer getSortSequence() { return sortSequence; }
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
    public Long getComponentProductId() { return componentProductId; }
    public void setComponentProductId(Long componentProductId) { this.componentProductId = componentProductId; }
    public String getComponentProductCode() { return componentProductCode; }
    public void setComponentProductCode(String componentProductCode) { this.componentProductCode = componentProductCode; }
    public String getComponentProductName() { return componentProductName; }
    public void setComponentProductName(String componentProductName) { this.componentProductName = componentProductName; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
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
