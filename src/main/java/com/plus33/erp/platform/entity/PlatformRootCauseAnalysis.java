package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_root_cause_analysis")
public class PlatformRootCauseAnalysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "causal_model_id", nullable = false)
    @NotNull
    private Long causalModelId;

    @Column(name = "anomaly_event", nullable = false)
    @NotNull
    @Size(max = 150)
    private String anomalyEvent;

    @Column(name = "probabilities_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String probabilitiesJson;

    @Column(name = "root_cause_node", nullable = false)
    @NotNull
    @Size(max = 150)
    private String rootCauseNode;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String explanation;

    @Column(name = "analyzed_at", nullable = false)
    @NotNull
    private LocalDateTime analyzedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCausalModelId() { return causalModelId; }
    public void setCausalModelId(Long causalModelId) { this.causalModelId = causalModelId; }
    public String getAnomalyEvent() { return anomalyEvent; }
    public void setAnomalyEvent(String anomalyEvent) { this.anomalyEvent = anomalyEvent; }
    public String getProbabilitiesJson() { return probabilitiesJson; }
    public void setProbabilitiesJson(String probabilitiesJson) { this.probabilitiesJson = probabilitiesJson; }
    public String getRootCauseNode() { return rootCauseNode; }
    public void setRootCauseNode(String rootCauseNode) { this.rootCauseNode = rootCauseNode; }
    public String getExplanation() { return explanation; }
    public void setExplanation(String explanation) { this.explanation = explanation; }
    public LocalDateTime getAnalyzedAt() { return analyzedAt; }
    public void setAnalyzedAt(LocalDateTime analyzedAt) { this.analyzedAt = analyzedAt; }
}