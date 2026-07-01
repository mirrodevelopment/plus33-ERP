package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_xai_lineage")
public class PlatformXaiLineage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "decision_key", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String decisionKey;

    @Column(name = "contributing_factors", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String contributingFactors;

    @Column(name = "model_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String modelVersion;

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDecisionKey() { return decisionKey; }
    public void setDecisionKey(String decisionKey) { this.decisionKey = decisionKey; }
    public String getContributingFactors() { return contributingFactors; }
    public void setContributingFactors(String contributingFactors) { this.contributingFactors = contributingFactors; }
    public String getModelVersion() { return modelVersion; }
    public void setModelVersion(String modelVersion) { this.modelVersion = modelVersion; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}