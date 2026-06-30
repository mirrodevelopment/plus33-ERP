package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class CreateQualityInspectionRequest {
    @NotNull private Long companyId;
    @NotBlank private String inspectionNumber;
    private Long productionOrderId;
    @NotNull private Long productId;
    @NotBlank private String inspectionType;
    @NotNull @Positive private BigDecimal sampleSize;
    private LocalDate inspectionDate;
    private Long inspectorUserId;
    private String notes;

    public CreateQualityInspectionRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getInspectionNumber() { return inspectionNumber; }
    public void setInspectionNumber(String inspectionNumber) { this.inspectionNumber = inspectionNumber; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getInspectionType() { return inspectionType; }
    public void setInspectionType(String inspectionType) { this.inspectionType = inspectionType; }
    public BigDecimal getSampleSize() { return sampleSize; }
    public void setSampleSize(BigDecimal sampleSize) { this.sampleSize = sampleSize; }
    public LocalDate getInspectionDate() { return inspectionDate; }
    public void setInspectionDate(LocalDate inspectionDate) { this.inspectionDate = inspectionDate; }
    public Long getInspectorUserId() { return inspectorUserId; }
    public void setInspectorUserId(Long inspectorUserId) { this.inspectorUserId = inspectorUserId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
