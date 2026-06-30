package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProductionCostDto {
    private Long id;
    private Long productionOrderId;
    private String costCategory;
    private String description;
    private BigDecimal plannedAmount;
    private BigDecimal actualAmount;
    private BigDecimal varianceAmount;
    private Long referenceEntityId;
    private String referenceEntityType;
    private LocalDateTime recordedAt;

    private String costingMethod;
    private BigDecimal actualMaterialCost;
    private BigDecimal actualLaborCost;
    private BigDecimal actualMachineCost;
    private BigDecimal actualOverheadCost;
    private BigDecimal actualSubcontractCost;
    private BigDecimal actualTotalCost;
    private BigDecimal standardMaterialCost;
    private BigDecimal standardLaborCost;
    private BigDecimal standardMachineCost;
    private BigDecimal standardOverheadCost;
    private BigDecimal standardSubcontractCost;
    private BigDecimal standardTotalCost;
    private BigDecimal wipBalance;
    private String status;

    public ProductionCostDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public String getCostCategory() { return costCategory; }
    public void setCostCategory(String costCategory) { this.costCategory = costCategory; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BigDecimal getPlannedAmount() { return plannedAmount; }
    public void setPlannedAmount(BigDecimal plannedAmount) { this.plannedAmount = plannedAmount; }
    public BigDecimal getActualAmount() { return actualAmount; }
    public void setActualAmount(BigDecimal actualAmount) { this.actualAmount = actualAmount; }
    public BigDecimal getVarianceAmount() { return varianceAmount; }
    public void setVarianceAmount(BigDecimal varianceAmount) { this.varianceAmount = varianceAmount; }
    public Long getReferenceEntityId() { return referenceEntityId; }
    public void setReferenceEntityId(Long referenceEntityId) { this.referenceEntityId = referenceEntityId; }
    public String getReferenceEntityType() { return referenceEntityType; }
    public void setReferenceEntityType(String referenceEntityType) { this.referenceEntityType = referenceEntityType; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }

    public String getCostingMethod() { return costingMethod; }
    public void setCostingMethod(String costingMethod) { this.costingMethod = costingMethod; }
    public BigDecimal getActualMaterialCost() { return actualMaterialCost; }
    public void setActualMaterialCost(BigDecimal actualMaterialCost) { this.actualMaterialCost = actualMaterialCost; }
    public BigDecimal getActualLaborCost() { return actualLaborCost; }
    public void setActualLaborCost(BigDecimal actualLaborCost) { this.actualLaborCost = actualLaborCost; }
    public BigDecimal getActualMachineCost() { return actualMachineCost; }
    public void setActualMachineCost(BigDecimal actualMachineCost) { this.actualMachineCost = actualMachineCost; }
    public BigDecimal getActualOverheadCost() { return actualOverheadCost; }
    public void setActualOverheadCost(BigDecimal actualOverheadCost) { this.actualOverheadCost = actualOverheadCost; }
    public BigDecimal getActualSubcontractCost() { return actualSubcontractCost; }
    public void setActualSubcontractCost(BigDecimal actualSubcontractCost) { this.actualSubcontractCost = actualSubcontractCost; }
    public BigDecimal getActualTotalCost() { return actualTotalCost; }
    public void setActualTotalCost(BigDecimal actualTotalCost) { this.actualTotalCost = actualTotalCost; }
    public BigDecimal getStandardMaterialCost() { return standardMaterialCost; }
    public void setStandardMaterialCost(BigDecimal standardMaterialCost) { this.standardMaterialCost = standardMaterialCost; }
    public BigDecimal getStandardLaborCost() { return standardLaborCost; }
    public void setStandardLaborCost(BigDecimal standardLaborCost) { this.standardLaborCost = standardLaborCost; }
    public BigDecimal getStandardMachineCost() { return standardMachineCost; }
    public void setStandardMachineCost(BigDecimal standardMachineCost) { this.standardMachineCost = standardMachineCost; }
    public BigDecimal getStandardOverheadCost() { return standardOverheadCost; }
    public void setStandardOverheadCost(BigDecimal standardOverheadCost) { this.standardOverheadCost = standardOverheadCost; }
    public BigDecimal getStandardSubcontractCost() { return standardSubcontractCost; }
    public void setStandardSubcontractCost(BigDecimal standardSubcontractCost) { this.standardSubcontractCost = standardSubcontractCost; }
    public BigDecimal getStandardTotalCost() { return standardTotalCost; }
    public void setStandardTotalCost(BigDecimal standardTotalCost) { this.standardTotalCost = standardTotalCost; }
    public BigDecimal getWipBalance() { return wipBalance; }
    public void setWipBalance(BigDecimal wipBalance) { this.wipBalance = wipBalance; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
