/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.entity
 * File              : IntegrationOutbox.java
 * Purpose           : JPA Entity representing a persistent database record in Integration Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntegrationOutboxController
 * Related Service   : IntegrationOutboxService, IntegrationOutboxServiceImpl
 * Related Repository: IntegrationOutboxRepository
 * Related Entity    : IntegrationOutbox
 * Related DTO       : N/A
 * Related Mapper    : IntegrationOutboxMapper
 * Related DB Table  : integration_outbox
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : IntegrationOutboxRepository, IntegrationOutboxMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'integration_outbox'. Defines persistent domain object for Integration Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.integration.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code IntegrationOutbox}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'integration_outbox'.</p>
 *
 * <p><b>Database Table   :</b> {@code integration_outbox}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves event id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventId() { return eventId; }
    /**
     * Performs the setEventId operation in this module.
     *
     * @param eventId the eventId input value
     */
    public void setEventId(String eventId) { this.eventId = eventId; }
    /**
     * Retrieves event type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; }
    /**
     * Performs the setEventType operation in this module.
     *
     * @param eventType the eventType input value
     */
    public void setEventType(String eventType) { this.eventType = eventType; }
    /**
     * Retrieves topic data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTopic() { return topic; }
    /**
     * Performs the setTopic operation in this module.
     *
     * @param topic the topic input value
     */
    public void setTopic(String topic) { this.topic = topic; }
    /**
     * Retrieves payload data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayload() { return payload; }
    /**
     * Performs the setPayload operation in this module.
     *
     * @param payload the payload input value
     */
    public void setPayload(String payload) { this.payload = payload; }
    /**
     * Retrieves trace parent data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTraceParent() { return traceParent; }
    /**
     * Performs the setTraceParent operation in this module.
     *
     * @param traceParent the traceParent input value
     */
    public void setTraceParent(String traceParent) { this.traceParent = traceParent; }
    /**
     * Retrieves correlation id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrelationId() { return correlationId; }
    /**
     * Performs the setCorrelationId operation in this module.
     *
     * @param correlationId the correlationId input value
     */
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    /**
     * Retrieves tenant id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTenantId() { return tenantId; }
    /**
     * Performs the setTenantId operation in this module.
     *
     * @param tenantId the tenantId input value
     */
    public void setTenantId(String tenantId) { this.tenantId = tenantId; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves attempts data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getAttempts() { return attempts; }
    /**
     * Performs the setAttempts operation in this module.
     *
     * @param attempts the attempts input value
     */
    public void setAttempts(Integer attempts) { this.attempts = attempts; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}