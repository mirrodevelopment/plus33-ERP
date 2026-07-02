package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_device_compliance_log")
public class PlatformDeviceComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String result; // PASS, FAIL, WARNING, UNKNOWN

    @Column(name = "execution_time", nullable = false)
    @NotNull
    private LocalDateTime executionTime = LocalDateTime.now();

    @Column(name = "duration_ms", nullable = false)
    @NotNull
    private Long durationMs;

    @Column(columnDefinition = "TEXT")
    private String details;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    public Long getPolicyId() { return policyId; }
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
    public LocalDateTime getExecutionTime() { return executionTime; }
    public void setExecutionTime(LocalDateTime executionTime) { this.executionTime = executionTime; }
    public Long getDurationMs() { return durationMs; }
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
    public String getDetails() { return details; }
    public void setDetails(String details) { this.details = details; }
}