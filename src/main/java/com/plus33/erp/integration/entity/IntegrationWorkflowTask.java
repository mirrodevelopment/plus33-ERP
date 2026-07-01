package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_workflow_task")
public class IntegrationWorkflowTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String instanceCode;

    @Column(name = "task_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String taskName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(nullable = false)
    @NotNull
    private Integer attempts = 0;

    @Column(name = "started_at", nullable = false)
    @NotNull
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getInstanceCode() { return instanceCode; }
    public void setInstanceCode(String instanceCode) { this.instanceCode = instanceCode; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getAttempts() { return attempts; }
    public void setAttempts(Integer attempts) { this.attempts = attempts; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}