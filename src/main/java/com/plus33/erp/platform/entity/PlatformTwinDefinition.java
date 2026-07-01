package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_twin_definition")
public class PlatformTwinDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "definition_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(name = "definition_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String definitionName;

    @Column(columnDefinition = "TEXT")
    private String description;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDefinitionCode() { return definitionCode; }
    public void setDefinitionCode(String definitionCode) { this.definitionCode = definitionCode; }
    public String getDefinitionName() { return definitionName; }
    public void setDefinitionName(String definitionName) { this.definitionName = definitionName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}