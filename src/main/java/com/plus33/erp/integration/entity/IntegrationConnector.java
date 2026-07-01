package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_connector")
public class IntegrationConnector {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "connector_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String connectorCode;

    @Column(name = "connector_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String connectorType;

    @Size(max = 250)
    private String host;

    private Integer port;

    @Column(name = "credential_reference")
    @Size(max = 250)
    private String credentialReference;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "health_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String healthStatus = "HEALTHY";

    @Column(name = "last_heartbeat")
    private LocalDateTime lastHeartbeat;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getConnectorCode() { return connectorCode; }
    public void setConnectorCode(String connectorCode) { this.connectorCode = connectorCode; }
    public String getConnectorType() { return connectorType; }
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }
    public Integer getPort() { return port; }
    public void setPort(Integer port) { this.port = port; }
    public String getCredentialReference() { return credentialReference; }
    public void setCredentialReference(String credentialReference) { this.credentialReference = credentialReference; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getHealthStatus() { return healthStatus; }
    public void setHealthStatus(String healthStatus) { this.healthStatus = healthStatus; }
    public LocalDateTime getLastHeartbeat() { return lastHeartbeat; }
    public void setLastHeartbeat(LocalDateTime lastHeartbeat) { this.lastHeartbeat = lastHeartbeat; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}