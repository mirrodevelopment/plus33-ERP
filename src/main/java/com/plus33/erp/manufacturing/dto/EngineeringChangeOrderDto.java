package com.plus33.erp.manufacturing.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EngineeringChangeOrderDto {
    private Long id;
    private Long companyId;
    private String ecoNumber;
    private String title;
    private String description;
    private String reason;
    private String status;
    private String priority;
    private LocalDate effectiveDate;
    private Long requestedBy;
    private Long reviewedBy;
    private Long approvedBy;
    private LocalDateTime reviewedAt;
    private LocalDateTime approvedAt;
    private LocalDateTime implementedAt;
    private List<EngineeringChangeLineDto> lines;

    public EngineeringChangeOrderDto() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    public Long getRequestedBy() { return requestedBy; }
    public void setRequestedBy(Long requestedBy) { this.requestedBy = requestedBy; }
    public Long getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Long reviewedBy) { this.reviewedBy = reviewedBy; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public LocalDateTime getImplementedAt() { return implementedAt; }
    public void setImplementedAt(LocalDateTime implementedAt) { this.implementedAt = implementedAt; }
    public List<EngineeringChangeLineDto> getLines() { return lines; }
    public void setLines(List<EngineeringChangeLineDto> lines) { this.lines = lines; }
}
