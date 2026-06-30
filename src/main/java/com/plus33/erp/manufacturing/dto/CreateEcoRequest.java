package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.util.List;
import jakarta.validation.constraints.*;

public class CreateEcoRequest {
    @NotNull private Long companyId;
    @NotBlank private String ecoNumber;
    @NotBlank private String title;
    private String description;
    private String reason;
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, CRITICAL, SAFETY
    private LocalDate effectiveDate;
    @NotNull private Long requestedBy;
    private List<CreateEcoLineRequest> lines;

    public CreateEcoRequest() {}

    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getEcoNumber() { return ecoNumber; }
    public void setEcoNumber(String ecoNumber) { this.ecoNumber = ecoNumber; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public Long getRequestedBy() { return requestedBy; }
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }
    public List<CreateEcoLineRequest> getLines() { return lines; }
    public void setLines(List<CreateEcoLineRequest> lines) { this.lines = lines; }
}
