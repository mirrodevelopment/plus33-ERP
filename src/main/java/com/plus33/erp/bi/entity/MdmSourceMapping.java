package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_mdm_source_mapping")
public class MdmSourceMapping {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "golden_record_id", nullable = false)
    @NotNull
    private MdmGoldenRecord goldenRecord;

    @Column(name = "source_system", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceSystem;

    @Column(name = "source_table", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceTable;

    @Column(name = "source_dim_id", nullable = false)
    @NotNull
    private Long sourceDimId;

    @Column(name = "confidence_score", nullable = false)
    @NotNull
    private java.math.BigDecimal confidenceScore = java.math.BigDecimal.valueOf(100);

    @Column(name = "mapped_at", nullable = false)
    @NotNull
    private LocalDateTime mappedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MdmGoldenRecord getGoldenRecord() { return goldenRecord; }
    public void setGoldenRecord(MdmGoldenRecord goldenRecord) { this.goldenRecord = goldenRecord; }
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