package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_connector_execution")
public class IntegrationConnectorExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String connectorCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status;

    @Column(name = "started_at", nullable = false)
    @NotNull
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    @NotNull
    private LocalDateTime completedAt;

    @Column(name = "payload_sent", columnDefinition = "TEXT")
    private String payloadSent;

    @Column(name = "payload_received", columnDefinition = "TEXT")
    private String payloadReceived;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConnectorCode() { return connectorCode; }
    public void setConnectorCode(String connectorCode) { this.connectorCode = connectorCode; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public String getPayloadSent() { return payloadSent; }
    public void setPayloadSent(String payloadSent) { this.payloadSent = payloadSent; }
    public String getPayloadReceived() { return payloadReceived; }
    public void setPayloadReceived(String payloadReceived) { this.payloadReceived = payloadReceived; }
    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
}