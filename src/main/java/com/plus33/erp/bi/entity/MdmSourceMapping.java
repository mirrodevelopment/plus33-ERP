package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_source_mapping")
public class MdmSourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "golden_record_id", nullable = false)
    private Long goldenRecordId;
    @Column(name = "source_system", nullable = false)
    private String sourceSystem;
    @Column(name = "source_table", nullable = false)
    private String sourceTable;
    @Column(name = "source_dim_id", nullable = false)
    private Long sourceDimId;
    @Column(name = "confidence_score", nullable = false)
    private java.math.BigDecimal confidenceScore = java.math.BigDecimal.valueOf(100);
    @Column(name = "mapped_at", nullable = false)
    private LocalDateTime mappedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getGoldenRecordId() { return goldenRecordId; }
    public void setGoldenRecordId(Long goldenRecordId) { this.goldenRecordId = goldenRecordId; }
    public String getSourceSystem() { return sourceSystem; }
    public void setSourceSystem(String sourceSystem) { this.sourceSystem = sourceSystem; }
    public String getSourceTable() { return sourceTable; }
    public void setSourceTable(String sourceTable) { this.sourceTable = sourceTable; }
    public Long getSourceDimId() { return sourceDimId; }
    public void setSourceDimId(Long sourceDimId) { this.sourceDimId = sourceDimId; }
    public java.math.BigDecimal getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(java.math.BigDecimal confidenceScore) { this.confidenceScore = confidenceScore; }
    public LocalDateTime getMappedAt() { return mappedAt; }
    public void setMappedAt(LocalDateTime mappedAt) { this.mappedAt = mappedAt; }
}