package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_knowledge_source")
public class PlatformKnowledgeSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 150)
    private String sourceName;

    @Column(name = "source_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String sourceType;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getSourceName() { return sourceName; }
    public void setSourceName(String sourceName) { this.sourceName = sourceName; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}