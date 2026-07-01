package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_process_event_log")
public class PlatformProcessEventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    @NotNull
    private Long caseId;

    @Column(name = "activity_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String activityName;

    @Column(name = "transition_state", nullable = false)
    @NotNull
    @Size(max = 50)
    private String transitionState;

    @Column(name = "duration_ms", nullable = false)
    @NotNull
    private Long durationMs;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCaseId() { return caseId; }
    public void setCaseId(Long caseId) { this.caseId = caseId; }
    public String getActivityName() { return activityName; }
    public void setActivityName(String activityName) { this.activityName = activityName; }
    public String getTransitionState() { return transitionState; }
    public void setTransitionState(String transitionState) { this.transitionState = transitionState; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}