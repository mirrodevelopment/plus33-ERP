package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class ReportCompletionRequest {
    @NotNull @Positive private BigDecimal producedQuantity;
    private BigDecimal rejectedQuantity = BigDecimal.ZERO;
    private Long targetWarehouseId;
    private String notes;
    @NotNull private Long reportedBy;

    public ReportCompletionRequest() {}

    public BigDecimal getProducedQuantity() { return producedQuantity; }
    public void setProducedQuantity(BigDecimal producedQuantity) { this.producedQuantity = producedQuantity; }
    public BigDecimal getRejectedQuantity() { return rejectedQuantity; }
    public void setRejectedQuantity(BigDecimal rejectedQuantity) { this.rejectedQuantity = rejectedQuantity; }
    public Long getTargetWarehouseId() { return targetWarehouseId; }
    public void setTargetWarehouseId(Long targetWarehouseId) { this.targetWarehouseId = targetWarehouseId; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getReportedBy() { return reportedBy; }
    public void setReportedBy(Long reportedBy) { this.reportedBy = reportedBy; }
}
