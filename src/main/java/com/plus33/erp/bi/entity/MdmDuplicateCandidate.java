package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_duplicate_candidate")
public class MdmDuplicateCandidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "record_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String recordType;

    @Column(name = "source_system_a", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemA;

    @Column(name = "source_table_a", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTableA;

    @Column(name = "source_dim_id_a", nullable = false)
    @NotNull
    private Long sourceDimIdA;

    @Column(name = "source_system_b", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystemB;

    @Column(name = "source_table_b", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTableB;

    @Column(name = "source_dim_id_b", nullable = false)
    @NotNull
    private Long sourceDimIdB;

    @Column(name = "similarity_score", nullable = false)
    @NotNull
    private java.math.BigDecimal similarityScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getRecordType() { return recordType; }
    public void setRecordType(String recordType) { this.recordType = recordType; }
    public String getSourceSystemA() { return sourceSystemA; }
    public void setSourceSystemA(String sourceSystemA) { this.sourceSystemA = sourceSystemA; }
    public String getSourceTableA() { return sourceTableA; }
    public void setSourceTableA(String sourceTableA) { this.sourceTableA = sourceTableA; }
    public Long getSourceDimIdA() { return sourceDimIdA; }
    public void setSourceDimIdA(Long sourceDimIdA) { this.sourceDimIdA = sourceDimIdA; }
    public String getSourceSystemB() { return sourceSystemB; }
    public void setSourceSystemB(String sourceSystemB) { this.sourceSystemB = sourceSystemB; }
    public String getSourceTableB() { return sourceTableB; }
    public void setSourceTableB(String sourceTableB) { this.sourceTableB = sourceTableB; }
    public Long getSourceDimIdB() { return sourceDimIdB; }
    public void setSourceDimIdB(Long sourceDimIdB) { this.sourceDimIdB = sourceDimIdB; }
    public java.math.BigDecimal getSimilarityScore() { return similarityScore; }
    public void setSimilarityScore(java.math.BigDecimal similarityScore) { this.similarityScore = similarityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDetectedAt() { return detectedAt; }
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
}