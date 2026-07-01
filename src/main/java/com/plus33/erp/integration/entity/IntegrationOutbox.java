package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_outbox")
public class IntegrationOutbox {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "event_id", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String eventId;

    @Column(name = "event_type", nullable = false)
    @NotNull
    @Size(max = 250)
    private String eventType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 250)
    private String topic;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String payload;

    @Column(name = "trace_parent")
    @Size(max = 250)
    private String traceParent;

    @Column(name = "correlation_id")
    @Size(max = 100)
    private String correlationId;

    @Column(name = "tenant_id")
    @Size(max = 50)
    private String tenantId;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "PENDING";

    @Column(nullable = false)
    @NotNull
    private Integer attempts = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    @NotNull
    private LocalDateTime updatedAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getEventId() { return eventId; }
    public void setEventId(String eventId) { this.eventId = eventId; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getPayload() { return payload; }
    public void setPayload(String payload) { this.payload = payload; }
    public String getTraceParent() { return traceParent; }
    public void setTraceParent(String traceParent) { this.traceParent = traceParent; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getTenantId() { return tenantId; }
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Integer getAttempts() { return attempts; }
    public void setAttempts(Integer attempts) { this.attempts = attempts; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}