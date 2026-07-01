package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_anomaly_alert")
public class PlatformAnomalyAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "alert_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String alertName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity = "WARNING";

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "trigger_message", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String triggerMessage;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getAlertName() { return alertName; }
    public void setAlertName(String alertName) { this.alertName = alertName; }
    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getTriggerMessage() { return triggerMessage; }
    public void setTriggerMessage(String triggerMessage) { this.triggerMessage = triggerMessage; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}