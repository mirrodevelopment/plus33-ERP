package com.plus33.erp.manufacturing.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.*;

public class RecordInspectionResultRequest {
    @NotNull private BigDecimal passedQuantity;
    @NotNull private BigDecimal failedQuantity;
    private String defectCode;
    private String defectDescription;
    private String disposition; // ACCEPT, REJECT, REWORK, SCRAP
    private String notes;
    @NotNull private Long inspectorUserId;

    public RecordInspectionResultRequest() {}

    public BigDecimal getPassedQuantity() { return passedQuantity; }
    public void setPassedQuantity(BigDecimal passedQuantity) { this.passedQuantity = passedQuantity; }
    public BigDecimal getFailedQuantity() { return failedQuantity; }
    public void setFailedQuantity(BigDecimal failedQuantity) { this.failedQuantity = failedQuantity; }
    public String getDefectCode() { return defectCode; }
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
    public String getDefectDescription() { return defectDescription; }
    public void setDefectDescription(String defectDescription) { this.defectDescription = defectDescription; }
    public String getDisposition() { return disposition; }
    public void setDisposition(String disposition) { this.disposition = disposition; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getInspectorUserId() { return inspectorUserId; }
    public void setInspectorUserId(Long inspectorUserId) { this.inspectorUserId = inspectorUserId; }
}
