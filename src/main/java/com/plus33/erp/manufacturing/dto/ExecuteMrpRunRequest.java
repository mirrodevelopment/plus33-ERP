package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import jakarta.validation.constraints.*;

public class ExecuteMrpRunRequest {
    @NotNull private Long companyId;
    @NotBlank private String runNumber;
    @NotNull private LocalDate horizonStartDate;
    @NotNull private LocalDate horizonEndDate;
    @NotNull private Long initiatedBy;

    public ExecuteMrpRunRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getRunNumber() { return runNumber; }
    public void setRunNumber(String runNumber) { this.runNumber = runNumber; }
    public LocalDate getHorizonStartDate() { return horizonStartDate; }
    public void setHorizonStartDate(LocalDate horizonStartDate) { this.horizonStartDate = horizonStartDate; }
    public LocalDate getHorizonEndDate() { return horizonEndDate; }
    public void setHorizonEndDate(LocalDate horizonEndDate) { this.horizonEndDate = horizonEndDate; }
    public Long getInitiatedBy() { return initiatedBy; }
    public void setInitiatedBy(Long initiatedBy) { this.initiatedBy = initiatedBy; }
}
