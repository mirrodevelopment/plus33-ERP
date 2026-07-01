package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_twin_relation")
public class PlatformTwinRelation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_instance_id", nullable = false)
    @NotNull
    private Long sourceInstanceId;

    @Column(name = "target_instance_id", nullable = false)
    @NotNull
    private Long targetInstanceId;

    @Column(name = "relationship_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String relationshipType;

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public Long getSourceInstanceId() { return sourceInstanceId; }
    public void setSourceInstanceId(Long sourceInstanceId) { this.sourceInstanceId = sourceInstanceId; }
    public Long getTargetInstanceId() { return targetInstanceId; }
    public void setTargetInstanceId(Long targetInstanceId) { this.targetInstanceId = targetInstanceId; }
    public String getRelationshipType() { return relationshipType; }
    public void setRelationshipType(String relationshipType) { this.relationshipType = relationshipType; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}