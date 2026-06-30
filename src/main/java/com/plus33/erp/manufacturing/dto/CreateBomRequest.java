package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class CreateBomRequest {
    @NotNull private Long companyId;
    @NotNull private Long productId;
    @NotBlank private String bomNumber;
    @NotNull @Positive private BigDecimal baseQuantity;
    @NotNull private Long unitId;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long routingHeaderId;
    private String notes;
    private Long createdBy;

    public CreateBomRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getBomNumber() { return bomNumber; }
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    public String getBomCode() { return bomNumber; }
    public void setBomCode(String bomCode) { this.bomNumber = bomCode; }
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public Long getRoutingHeaderId() { return routingHeaderId; }
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
}
