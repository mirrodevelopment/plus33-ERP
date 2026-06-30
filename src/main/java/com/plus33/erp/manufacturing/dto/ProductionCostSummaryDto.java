package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.util.List;

public class ProductionCostSummaryDto {
    private Long productionOrderId;
    private String orderNumber;
    private BigDecimal totalPlannedCost;
    private BigDecimal totalActualCost;
    private BigDecimal totalVariance;
    private BigDecimal materialCost;
    private BigDecimal laborCost;
    private BigDecimal overheadCost;
    private List<ProductionCostDto> lineItems;

    private BigDecimal materialVariance;
    private BigDecimal laborVariance;
    private BigDecimal machineVariance;
    private BigDecimal overheadVariance;
    private BigDecimal wipBalance;

    public ProductionCostSummaryDto() {}

    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public BigDecimal getTotalPlannedCost() { return totalPlannedCost; }
    public void setTotalPlannedCost(BigDecimal totalPlannedCost) { this.totalPlannedCost = totalPlannedCost; }
    public BigDecimal getTotalActualCost() { return totalActualCost; }
    public void setTotalActualCost(BigDecimal totalActualCost) { this.totalActualCost = totalActualCost; }
    public BigDecimal getTotalVariance() { return totalVariance; }
    public void setTotalVariance(BigDecimal totalVariance) { this.totalVariance = totalVariance; }
    public BigDecimal getMaterialCost() { return materialCost; }
    public void setMaterialCost(BigDecimal materialCost) { this.materialCost = materialCost; }
    public BigDecimal getLaborCost() { return laborCost; }
    public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }
    public BigDecimal getOverheadCost() { return overheadCost; }
    public void setOverheadCost(BigDecimal overheadCost) { this.overheadCost = overheadCost; }
    public List<ProductionCostDto> getLineItems() { return lineItems; }
    public void setLineItems(List<ProductionCostDto> lineItems) { this.lineItems = lineItems; }

    public BigDecimal getMaterialVariance() { return materialVariance; }
    public void setMaterialVariance(BigDecimal materialVariance) { this.materialVariance = materialVariance; }
    public BigDecimal getLaborVariance() { return laborVariance; }
    public void setLaborVariance(BigDecimal laborVariance) { this.laborVariance = laborVariance; }
    public BigDecimal getMachineVariance() { return machineVariance; }
    public void setMachineVariance(BigDecimal machineVariance) { this.machineVariance = machineVariance; }
    public BigDecimal getOverheadVariance() { return overheadVariance; }
    public void setOverheadVariance(BigDecimal overheadVariance) { this.overheadVariance = overheadVariance; }
    public BigDecimal getWipBalance() { return wipBalance; }
    public void setWipBalance(BigDecimal wipBalance) { this.wipBalance = wipBalance; }
}
