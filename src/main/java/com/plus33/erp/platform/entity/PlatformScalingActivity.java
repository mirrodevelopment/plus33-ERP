package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_scaling_activity")
public class PlatformScalingActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "activity_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String activityType;

    @Column(name = "current_replicas", nullable = false)
    @NotNull
    private Integer currentReplicas;

    @Column(name = "desired_replicas", nullable = false)
    @NotNull
    private Integer desiredReplicas;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "COMPLETED";

    @Column(name = "started_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime startedAt = LocalDateTime.now();

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getActivityType() { return activityType; }
    public void setActivityType(String activityType) { this.activityType = activityType; }
    public Integer getCurrentReplicas() { return currentReplicas; }
    public void setCurrentReplicas(Integer currentReplicas) { this.currentReplicas = currentReplicas; }
    public Integer getDesiredReplicas() { return desiredReplicas; }
    public void setDesiredReplicas(Integer desiredReplicas) { this.desiredReplicas = desiredReplicas; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
}