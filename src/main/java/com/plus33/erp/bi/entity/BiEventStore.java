package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "bi_event_store")
public class BiEventStore {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id") private Long companyId;
    @Column(name = "event_type", nullable = false, length = 100) private String eventType;
    @Column(name = "entity_type", length = 100) private String entityType;
    @Column(name = "entity_id") private Long entityId;
    @Column(name = "payload_json", columnDefinition = "TEXT") private String payloadJson;
    @Column(name = "event_version", nullable = false, length = 20) private String eventVersion = "1.0";
    @Column(name = "correlation_id", length = 100) private String correlationId;
    @Column(name = "idempotency_key", unique = true, length = 100) private String idempotencyKey;
    @Column(name = "performed_by", length = 100) private String performedBy;
    @Column(name = "occurred_at", nullable = false, updatable = false) private LocalDateTime occurredAt = LocalDateTime.now();

    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    public String getEventType() { return eventType; } public void setEventType(String v) { this.eventType = v; }
    public String getEntityType() { return entityType; } public void setEntityType(String v) { this.entityType = v; }
    public Long getEntityId() { return entityId; } public void setEntityId(Long v) { this.entityId = v; }
    public String getPayloadJson() { return payloadJson; } public void setPayloadJson(String v) { this.payloadJson = v; }
    public String getEventVersion() { return eventVersion; } public void setEventVersion(String v) { this.eventVersion = v; }
    public String getCorrelationId() { return correlationId; } public void setCorrelationId(String v) { this.correlationId = v; }
    public String getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(String v) { this.idempotencyKey = v; }
    public String getPerformedBy() { return performedBy; } public void setPerformedBy(String v) { this.performedBy = v; }
    public LocalDateTime getOccurredAt() { return occurredAt; }
}
