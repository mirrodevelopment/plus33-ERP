package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

public class CreateProductionOrderRequest {
    @NotNull private Long companyId;
    @NotBlank private String orderNumber;
    @NotNull private Long productId;
    private Long bomHeaderId;
    private Long routingHeaderId;
    @NotNull @Positive private BigDecimal plannedQuantity;
    @NotNull private Long unitId;
    private Integer priority = 5;
    @NotNull private LocalDate plannedStartDate;
    @NotNull private LocalDate plannedEndDate;
    private Long warehouseId;
    private String notes;
    private Long createdBy;
    private List<CreateProductionOrderOperationRequest> operations;

    public CreateProductionOrderRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getOrderNumber() { return orderNumber; }
    public void setOrderNumber(String orderNumber) { this.orderNumber = orderNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getBomHeaderId() { return bomHeaderId; }
    public void setBomHeaderId(Long bomHeaderId) { this.bomHeaderId = bomHeaderId; }
    public Long getRoutingHeaderId() { return routingHeaderId; }
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    public BigDecimal getPlannedQuantity() { return plannedQuantity; }
    public void setPlannedQuantity(BigDecimal plannedQuantity) { this.plannedQuantity = plannedQuantity; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public LocalDate getPlannedStartDate() { return plannedStartDate; }
    public void setPlannedStartDate(LocalDate plannedStartDate) { this.plannedStartDate = plannedStartDate; }
    public LocalDate getPlannedEndDate() { return plannedEndDate; }
    public void setPlannedEndDate(LocalDate plannedEndDate) { this.plannedEndDate = plannedEndDate; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public List<CreateProductionOrderOperationRequest> getOperations() { return operations; }
    public void setOperations(List<CreateProductionOrderOperationRequest> operations) { this.operations = operations; }
}
