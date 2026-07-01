package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_graph_edge")
public class PlatformGraphEdge {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "source_node", nullable = false)
    @NotNull
    private Long sourceNode;

    @Column(name = "target_node", nullable = false)
    @NotNull
    private Long targetNode;

    @Column(name = "relationship_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String relationshipType;

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal confidence = BigDecimal.valueOf(100.00);

    @Column(nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal weight = BigDecimal.valueOf(1.00);

    @Column(name = "created_at", nullable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getSourceNode() { return sourceNode; }
    public void setSourceNode(Long sourceNode) { this.sourceNode = sourceNode; }
    public Long getTargetNode() { return targetNode; }
    public void setTargetNode(Long targetNode) { this.targetNode = targetNode; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    public BigDecimal getConfidence() { return confidence; }
    public void setConfidence(BigDecimal confidence) { this.confidence = confidence; }
    public BigDecimal getWeight() { return weight; }
    public void setWeight(BigDecimal weight) { this.weight = weight; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}