package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class BomHeaderDto {
    private Long id;
    private Long companyId;
    private String bomNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private BigDecimal baseQuantity;
    private String unitCode;
    private String status;
    private Integer version;
    private LocalDate effectiveFrom;
    private LocalDate effectiveTo;
    private Long routingHeaderId;
    private String notes;
    private List<BomLineDto> lines;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BomHeaderDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getBomNumber() { return bomNumber; }
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    public String getBomCode() { return bomNumber; }
    public void setBomCode(String bomCode) { this.bomNumber = bomCode; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public Long getRoutingHeaderId() { return routingHeaderId; }
    public void setRoutingHeaderId(Long routingHeaderId) { this.routingHeaderId = routingHeaderId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public List<BomLineDto> getLines() { return lines; }
    public void setLines(List<BomLineDto> lines) { this.lines = lines; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
