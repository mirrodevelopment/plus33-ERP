package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "integration_schema_registry", uniqueConstraints = @UniqueConstraint(columnNames = {"event_type", "schema_version"}))
public class IntegrationSchemaRegistry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type", nullable = false)
    @NotNull
    @Size(max = 250)
    private String eventType;

    @Column(name = "schema_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String schemaVersion;

    @Column(name = "schema_definition", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String schemaDefinition;

    @Column(name = "compatibility_rule", nullable = false)
    @NotNull
    @Size(max = 50)
    private String compatibilityRule = "BACKWARD";

    @Column(name = "deprecation_date")
    private LocalDateTime deprecationDate;

    @Column(name = "replacement_event")
    @Size(max = 250)
    private String replacementEvent;

    @Column(name = "created_at", nullable = false, updatable = false)
    @NotNull
    private LocalDateTime createdAt = LocalDateTime.now();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getEventType() { return eventType; }
    public void setEventType(String eventType) { this.eventType = eventType; }
    public String getSchemaVersion() { return schemaVersion; }
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }
    public String getSchemaDefinition() { return schemaDefinition; }
    public void setSchemaDefinition(String schemaDefinition) { this.schemaDefinition = schemaDefinition; }
    public String getCompatibilityRule() { return compatibilityRule; }
    public void setCompatibilityRule(String compatibilityRule) { this.compatibilityRule = compatibilityRule; }
    public LocalDateTime getDeprecationDate() { return deprecationDate; }
    public void setDeprecationDate(LocalDateTime deprecationDate) { this.deprecationDate = deprecationDate; }
    public String getReplacementEvent() { return replacementEvent; }
    public void setReplacementEvent(String replacementEvent) { this.replacementEvent = replacementEvent; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}