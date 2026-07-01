package com.plus33.erp.project.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "project_task_dependencies")
public class ProjectDependency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "task_id", nullable = false)
    private Long taskId;

    @Column(name = "predecessor_task_id", nullable = false)
    private Long predecessorTaskId;

    @Column(name = "dependency_type", nullable = false, length = 20)
    private String dependencyType = "FS";

    @Column(name = "lag_days", nullable = false)
    private Integer lagDays = 0;

    @Column(name = "lead_days", nullable = false)
    private Integer leadDays = 0;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getTaskId() { return taskId; }
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    public Long getPredecessorTaskId() { return predecessorTaskId; }
    public void setPredecessorTaskId(Long predecessorTaskId) { this.predecessorTaskId = predecessorTaskId; }
    public String getDependencyType() { return dependencyType; }
    public void setDependencyType(String dependencyType) { this.dependencyType = dependencyType; }
    public Integer getLagDays() { return lagDays; }
    public void setLagDays(Integer lagDays) { this.lagDays = lagDays; }
    public Integer getLeadDays() { return leadDays; }
    public void setLeadDays(Integer leadDays) { this.leadDays = leadDays; }
}
