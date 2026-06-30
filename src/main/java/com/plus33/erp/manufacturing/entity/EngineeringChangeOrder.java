package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "engineering_change_orders")
public class EngineeringChangeOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "eco_number", nullable = false, length = 50)
    private String ecoNumber;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 255)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private EcoStatus status = EcoStatus.DRAFT;

    @Column(nullable = false, length = 20)
    private String priority = "NORMAL"; // LOW, NORMAL, HIGH, CRITICAL, SAFETY

    @Column(name = "effective_date")
    private java.time.LocalDate effectiveDate;

    @Column(name = "requested_by", nullable = false)
    private Long requestedBy;

    @Column(name = "reviewed_by")
    private Long reviewedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "implemented_at")
    private LocalDateTime implementedAt;

    @OneToMany(mappedBy = "engineeringChangeOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private java.util.List<EngineeringChangeLine> lines = new java.util.ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public EngineeringChangeOrder() {}

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
    public EcoStatus getStatus() { return status; }
    public void setStatus(EcoStatus status) { this.status = status; }
    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }
    public java.time.LocalDate getEffectiveDate() { return effectiveDate; }
    public void setEffectiveDate(java.time.LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
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
    public java.util.List<EngineeringChangeLine> getLines() { return lines; }
    public void setLines(java.util.List<EngineeringChangeLine> lines) { this.lines = lines; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
