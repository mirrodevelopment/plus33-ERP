package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_maintenance_trigger_log")
public class PlatformMaintenanceTriggerLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "trigger_source", nullable = false)
    @NotNull
    @Size(max = 100)
    private String triggerSource; // PREDICTIVE_ENGINE, MANUAL, THRESHOLD

    @Column(name = "predicted_failure_id")
    private Long predictedFailureId;

    @Column(name = "work_order_reference", nullable = false)
    @NotNull
    @Size(max = 100)
    private String workOrderReference;

    @Column(name = "maintenance_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String maintenanceStatus; // SCHEDULED, ASSIGNED, COMPLETED, CANCELLED

    @Column(name = "scheduled_time", nullable = false)
    @NotNull
    private LocalDateTime scheduledTime;

    @Column(name = "completion_time")
    private LocalDateTime completionTime;

    @Column(name = "technician_assignment")
    @Size(max = 100)
    private String technicianAssignment;

    @Column(name = "automatic_execution", nullable = false)
    @NotNull
    private Boolean automaticExecution = true;

    @Column(name = "rollback_status")
    @Size(max = 50)
    private String rollbackStatus;

    @Column(name = "triggered_at", nullable = false)
    @NotNull
    private LocalDateTime triggeredAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTriggerSource() { return triggerSource; }
    public void setTriggerSource(String triggerSource) { this.triggerSource = triggerSource; }
    public Long getPredictedFailureId() { return predictedFailureId; }
    public void setPredictedFailureId(Long predictedFailureId) { this.predictedFailureId = predictedFailureId; }
    public String getWorkOrderReference() { return workOrderReference; }
    public void setWorkOrderReference(String workOrderReference) { this.workOrderReference = workOrderReference; }
    public String getMaintenanceStatus() { return maintenanceStatus; }
    public void setMaintenanceStatus(String maintenanceStatus) { this.maintenanceStatus = maintenanceStatus; }
    public LocalDateTime getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(LocalDateTime scheduledTime) { this.scheduledTime = scheduledTime; }
    public LocalDateTime getCompletionTime() { return completionTime; }
    public void setCompletionTime(LocalDateTime completionTime) { this.completionTime = completionTime; }
    public String getTechnicianAssignment() { return technicianAssignment; }
    public void setTechnicianAssignment(String technicianAssignment) { this.technicianAssignment = technicianAssignment; }
    public Boolean getAutomaticExecution() { return automaticExecution; }
    public void setAutomaticExecution(Boolean automaticExecution) { this.automaticExecution = automaticExecution; }
    public String getRollbackStatus() { return rollbackStatus; }
    public void setRollbackStatus(String rollbackStatus) { this.rollbackStatus = rollbackStatus; }
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }
}