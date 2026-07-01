package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_ai_insight_generation")
public class BiAiInsightGeneration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "insight_code", nullable = false, unique = true)
    private String insightCode;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "kpi_code", nullable = false)
    private String kpiCode;
    @Column(name = "source_snapshot_id")
    private Long sourceSnapshotId;
    @Column(name = "generated_model", nullable = false)
    private String generatedModel;
    @Column(name = "confidence_score")
    private java.math.BigDecimal confidenceScore;
    @Column(name = "narrative_text", nullable = false)
    private String narrativeText;
    @Column(name = "generated_at", nullable = false, updatable = false)
    private LocalDateTime generatedAt = LocalDateTime.now();
    @Column(name = "accepted_by_user")
    private Boolean acceptedByUser = false;
    @Column(name = "feedback_rating")
    private Integer feedbackRating;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInsightCode() { return insightCode; }
    public void setInsightCode(String insightCode) { this.insightCode = insightCode; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getKpiCode() { return kpiCode; }
    public void setKpiCode(String kpiCode) { this.kpiCode = kpiCode; }
    public Long getSourceSnapshotId() { return sourceSnapshotId; }
    public void setSourceSnapshotId(Long sourceSnapshotId) { this.sourceSnapshotId = sourceSnapshotId; }
    public String getGeneratedModel() { return generatedModel; }
    public void setGeneratedModel(String generatedModel) { this.generatedModel = generatedModel; }
    public java.math.BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(java.math.BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public String getNarrativeText() { return narrativeText; }
    public void setNarrativeText(String narrativeText) { this.narrativeText = narrativeText; }
    public LocalDateTime getGeneratedAt() { return generatedAt; }
    public void setGeneratedAt(LocalDateTime generatedAt) { this.generatedAt = generatedAt; }
    public Boolean getAcceptedByUser() { return acceptedByUser; }
    public void setAcceptedByUser(Boolean acceptedByUser) { this.acceptedByUser = acceptedByUser; }
    public Integer getFeedbackRating() { return feedbackRating; }
    public void setFeedbackRating(Integer feedbackRating) { this.feedbackRating = feedbackRating; }
}