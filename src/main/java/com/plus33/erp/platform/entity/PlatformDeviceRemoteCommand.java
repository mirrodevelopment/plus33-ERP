package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_device_remote_command")
public class PlatformDeviceRemoteCommand {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "node_id", nullable = false)
    @NotNull
    private Long nodeId;

    @Column(name = "command_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String commandType; // Restart, Shutdown, Sync, CollectLogs, RotateCertificate

    @Column(columnDefinition = "TEXT")
    private String parameters;

    @Column(nullable = false)
    @NotNull
    @Size(max = 256)
    private String signature;

    @Column(name = "timeout_seconds", nullable = false)
    @NotNull
    private Integer timeoutSeconds = 60;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // DISPATCHED, EXECUTED, FAILED, TIMED_OUT

    @Column(name = "response_payload", columnDefinition = "TEXT")
    private String responsePayload;

    @Column(name = "exit_code")
    private Integer exitCode;

    @Column(name = "execution_duration_ms")
    private Long executionDurationMs;

    @Column(name = "dispatched_at", nullable = false)
    @NotNull
    private LocalDateTime dispatchedAt = LocalDateTime.now();

    @Column(name = "executed_at")
    private LocalDateTime executedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getNodeId() { return nodeId; }
    public void setNodeId(Long nodeId) { this.nodeId = nodeId; }
    public String getCommandType() { return commandType; }
    public void setCommandType(String commandType) { this.commandType = commandType; }
    public String getParameters() { return parameters; }
    public void setParameters(String parameters) { this.parameters = parameters; }
    public String getSignature() { return signature; }
    public void setSignature(String signature) { this.signature = signature; }
    public Integer getTimeoutSeconds() { return timeoutSeconds; }
    public void setTimeoutSeconds(Integer timeoutSeconds) { this.timeoutSeconds = timeoutSeconds; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getResponsePayload() { return responsePayload; }
    public void setResponsePayload(String responsePayload) { this.responsePayload = responsePayload; }
    public Integer getExitCode() { return exitCode; }
    public void setExitCode(Integer exitCode) { this.exitCode = exitCode; }
    public Long getExecutionDurationMs() { return executionDurationMs; }
    public void setExecutionDurationMs(Long executionDurationMs) { this.executionDurationMs = executionDurationMs; }
    public LocalDateTime getDispatchedAt() { return dispatchedAt; }
    public void setDispatchedAt(LocalDateTime dispatchedAt) { this.dispatchedAt = dispatchedAt; }
    public LocalDateTime getExecutedAt() { return executedAt; }
    public void setExecutedAt(LocalDateTime executedAt) { this.executedAt = executedAt; }
}