package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MrpPlannedOrderDto {
    private Long id;
    private Long mrpRunId;
    private Long companyId;
    private Long productId;
    private String productCode;
    private String productName;
    private String orderType;
    private BigDecimal quantity;
    private String unitCode;
    private LocalDate releaseDate;
    private LocalDate dueDate;
    private String status;
    private Boolean firmed;
    private Long releasedProductionOrderId;

    public MrpPlannedOrderDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getMrpRunId() { return mrpRunId; }
    public void setMrpRunId(Long mrpRunId) { this.mrpRunId = mrpRunId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getOrderType() { return orderType; }
    public void setOrderType(String orderType) { this.orderType = orderType; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public String getUnitCode() { return unitCode; }
    public void setUnitCode(String unitCode) { this.unitCode = unitCode; }
    public LocalDate getReleaseDate() { return releaseDate; }
    public void setReleaseDate(LocalDate releaseDate) { this.releaseDate = releaseDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Boolean getFirmed() { return firmed; }
    public void setFirmed(Boolean firmed) { this.firmed = firmed; }
    public Long getReleasedProductionOrderId() { return releasedProductionOrderId; }
    public void setReleasedProductionOrderId(Long releasedProductionOrderId) { this.releasedProductionOrderId = releasedProductionOrderId; }
}
