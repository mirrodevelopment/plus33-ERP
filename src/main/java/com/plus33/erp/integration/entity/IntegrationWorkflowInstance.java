package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_workflow_instance")
public class IntegrationWorkflowInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "definition_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String definitionCode;

    @Column(name = "instance_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String instanceCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "RUNNING";

    @Column(name = "current_step")
    @Size(max = 100)
    private String currentStep;

    @Column(name = "variables_json", columnDefinition = "TEXT")
    private String variablesJson;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getDefinitionCode() { return definitionCode; }
    public void setDefinitionCode(String definitionCode) { this.definitionCode = definitionCode; }
    public String getInstanceCode() { return instanceCode; }
    public void setInstanceCode(String instanceCode) { this.instanceCode = instanceCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCurrentStep() { return currentStep; }
    public void setCurrentStep(String currentStep) { this.currentStep = currentStep; }
    public String getVariablesJson() { return variablesJson; }
    public void setVariablesJson(String variablesJson) { this.variablesJson = variablesJson; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}