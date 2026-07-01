package com.plus33.erp.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_risks")
public class ProjectRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 20)
    private String probability = "LOW";

    @Column(nullable = false, length = 20)
    private String impact = "LOW";

    @Column(name = "mitigation_plan", columnDefinition = "TEXT")
    private String mitigationPlan;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getProbability() { return probability; }
    public void setProbability(String probability) { this.probability = probability; }
    public String getImpact() { return impact; }
    public void setImpact(String impact) { this.impact = impact; }
    public String getMitigationPlan() { return mitigationPlan; }
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
