package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class QualityInspectionDto {
    private Long id;
    private Long companyId;
    private String inspectionNumber;
    private Long productionOrderId;
    private String productionOrderNumber;
    private Long productId;
    private String productCode;
    private String productName;
    private String inspectionType;
    private String status;
    private BigDecimal sampleSize;
    private BigDecimal inspectedQuantity;
    private BigDecimal passedQuantity;
    private BigDecimal failedQuantity;
    private String samplingPlan;
    private String disposition;
    private Boolean holdProduction;
    private String nonConformanceReport;
    private String correctiveAction;
    private Long inspectedBy;
    private Long approvedBy;
    private LocalDateTime inspectedAt;
    private LocalDateTime approvedAt;
    private String notes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public QualityInspectionDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getInspectionNumber() { return inspectionNumber; }
    public void setInspectionNumber(String inspectionNumber) { this.inspectionNumber = inspectionNumber; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public String getProductionOrderNumber() { return productionOrderNumber; }
    public void setProductionOrderNumber(String productionOrderNumber) { this.productionOrderNumber = productionOrderNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getProductCode() { return productCode; }
    public void setProductCode(String productCode) { this.productCode = productCode; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getInspectionType() { return inspectionType; }
    public void setInspectionType(String inspectionType) { this.inspectionType = inspectionType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getSampleSize() { return sampleSize; }
    public void setSampleSize(BigDecimal sampleSize) { this.sampleSize = sampleSize; }
    public BigDecimal getInspectedQuantity() { return inspectedQuantity; }
    public void setInspectedQuantity(BigDecimal inspectedQuantity) { this.inspectedQuantity = inspectedQuantity; }
    public BigDecimal getPassedQuantity() { return passedQuantity; }
    public void setPassedQuantity(BigDecimal passedQuantity) { this.passedQuantity = passedQuantity; }
    public BigDecimal getFailedQuantity() { return failedQuantity; }
    public void setFailedQuantity(BigDecimal failedQuantity) { this.failedQuantity = failedQuantity; }
    public String getSamplingPlan() { return samplingPlan; }
    public void setSamplingPlan(String samplingPlan) { this.samplingPlan = samplingPlan; }
    public String getDisposition() { return disposition; }
    public void setDisposition(String disposition) { this.disposition = disposition; }
    public Boolean getHoldProduction() { return holdProduction; }
    public void setHoldProduction(Boolean holdProduction) { this.holdProduction = holdProduction; }
    public String getNonConformanceReport() { return nonConformanceReport; }
    public void setNonConformanceReport(String nonConformanceReport) { this.nonConformanceReport = nonConformanceReport; }
    public String getCorrectiveAction() { return correctiveAction; }
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    public Long getInspectedBy() { return inspectedBy; }
    public void setInspectedBy(Long inspectedBy) { this.inspectedBy = inspectedBy; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getInspectedAt() { return inspectedAt; }
    public void setInspectedAt(LocalDateTime inspectedAt) { this.inspectedAt = inspectedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
