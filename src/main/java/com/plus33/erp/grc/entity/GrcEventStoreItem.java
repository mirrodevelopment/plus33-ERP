package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "grc_event_store")
public class GrcEventStoreItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "event_type", nullable = false, length = 100) private String eventType;
    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT") private String payloadJson;
    @Column(name = "event_version", nullable = false, length = 20) private String eventVersion = "1.0";
    @Column(name = "schema_version", nullable = false, length = 20) private String schemaVersion = "1.0";
    @Column(name = "correlation_id", length = 100) private String correlationId;
    @Column(name = "idempotency_key", unique = true, length = 100) private String idempotencyKey;
    @Column(name = "occurred_at", nullable = false, updatable = false) private LocalDateTime occurredAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getEventType() { return eventType; } public void setEventType(String v) { this.eventType = v; }
    public String getPayloadJson() { return payloadJson; } public void setPayloadJson(String v) { this.payloadJson = v; }
    public String getEventVersion() { return eventVersion; } public void setEventVersion(String v) { this.eventVersion = v; }
    public String getSchemaVersion() { return schemaVersion; } public void setSchemaVersion(String v) { this.schemaVersion = v; }
    public String getCorrelationId() { return correlationId; } public void setCorrelationId(String v) { this.correlationId = v; }
    public String getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(String v) { this.idempotencyKey = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
