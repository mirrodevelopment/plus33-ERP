package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "platform_agent_workflow")
public class PlatformAgentWorkflow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "workflow_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String workflowCode;

    @Column(name = "workflow_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String workflowName;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getWorkflowCode() { return workflowCode; }
    public void setWorkflowCode(String workflowCode) { this.workflowCode = workflowCode; }
    public String getWorkflowName() { return workflowName; }
    public void setWorkflowName(String workflowName) { this.workflowName = workflowName; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}