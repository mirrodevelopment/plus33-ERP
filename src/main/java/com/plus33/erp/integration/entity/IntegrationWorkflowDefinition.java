package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_workflow_definition")
public class IntegrationWorkflowDefinition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "definition_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String name;

    @Column(name = "layout_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String layoutJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDefinitionCode() { return definitionCode; }
    public void setDefinitionCode(String definitionCode) { this.definitionCode = definitionCode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLayoutJson() { return layoutJson; }
    public void setLayoutJson(String layoutJson) { this.layoutJson = layoutJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}