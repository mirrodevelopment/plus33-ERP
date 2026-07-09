/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiEventStore.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEventStoreController
 * Related Service   : BiEventStoreService, BiEventStoreServiceImpl
 * Related Repository: BiEventStoreRepository
 * Related Entity    : BiEventStore
 * Related DTO       : N/A
 * Related Mapper    : BiEventStoreMapper
 * Related DB Table  : bi_event_store
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiEventStoreRepository, BiEventStoreMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_event_store'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiEventStore}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_event_store'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_event_store}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_event_store")
public class BiEventStore {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
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
    /**
     * Retrieves company id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; } public void setCompanyId(Long v) { this.companyId = v; }
    /**
     * Retrieves event type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; } public void setEventType(String v) { this.eventType = v; }
    /**
     * Retrieves entity type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEntityType() { return entityType; } public void setEntityType(String v) { this.entityType = v; }
    /**
     * Retrieves entity id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEntityId() { return entityId; } public void setEntityId(Long v) { this.entityId = v; }
    /**
     * Retrieves payload json data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadJson() { return payloadJson; } public void setPayloadJson(String v) { this.payloadJson = v; }
    /**
     * Retrieves event version data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventVersion() { return eventVersion; } public void setEventVersion(String v) { this.eventVersion = v; }
    /**
     * Retrieves correlation id data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrelationId() { return correlationId; } public void setCorrelationId(String v) { this.correlationId = v; }
    /**
     * Retrieves idempotency key data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIdempotencyKey() { return idempotencyKey; } public void setIdempotencyKey(String v) { this.idempotencyKey = v; }
    /**
     * Retrieves performed by data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPerformedBy() { return performedBy; } public void setPerformedBy(String v) { this.performedBy = v; }
    /**
     * Retrieves occurred at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
}