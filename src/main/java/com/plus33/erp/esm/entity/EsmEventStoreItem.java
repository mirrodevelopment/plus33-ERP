/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : EsmEventStoreItem.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmEventStoreItemController
 * Related Service   : EsmEventStoreItemService, EsmEventStoreItemServiceImpl
 * Related Repository: EsmEventStoreItemRepository
 * Related Entity    : EsmEventStoreItem
 * Related DTO       : N/A
 * Related Mapper    : EsmEventStoreItemMapper
 * Related DB Table  : esm_event_store
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmEventStoreItemRepository, EsmEventStoreItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_event_store'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmEventStoreItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_event_store'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_event_store}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_event_store")
public class EsmEventStoreItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(name = "payload_json", nullable = false, columnDefinition = "TEXT")
    private String payloadJson;

    @Column(name = "event_version", nullable = false, length = 20)
    private String eventVersion;

    @Column(name = "schema_version", nullable = false, length = 20)
    private String schemaVersion;

    @Column(name = "correlation_id", length = 100)
    private String correlationId;

    @Column(name = "causation_id", length = 100)
    private String causationId;

    @Column(name = "idempotency_key", unique = true, length = 100)
    private String idempotencyKey;

    @Column(name = "occurred_at", nullable = false, updatable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();

    // Getters and setters
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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
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
     * Retrieves payload json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadJson() { return payloadJson; }
    /**
     * Performs the setPayloadJson operation in this module.
     *
     * @param payloadJson the payloadJson input value
     */
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
    /**
     * Retrieves event version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventVersion() { return eventVersion; }
    /**
     * Performs the setEventVersion operation in this module.
     *
     * @param eventVersion the eventVersion input value
     */
    public void setEventVersion(String eventVersion) { this.eventVersion = eventVersion; }
    /**
     * Retrieves schema version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSchemaVersion() { return schemaVersion; }
    /**
     * Performs the setSchemaVersion operation in this module.
     *
     * @param schemaVersion the schemaVersion input value
     */
    public void setSchemaVersion(String schemaVersion) { this.schemaVersion = schemaVersion; }
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
     * Retrieves causation id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCausationId() { return causationId; }
    /**
     * Performs the setCausationId operation in this module.
     *
     * @param causationId the causationId input value
     */
    public void setCausationId(String causationId) { this.causationId = causationId; }
    /**
     * Retrieves idempotency key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIdempotencyKey() { return idempotencyKey; }
    /**
     * Performs the setIdempotencyKey operation in this module.
     *
     * @param idempotencyKey the idempotencyKey input value
     */
    public void setIdempotencyKey(String idempotencyKey) { this.idempotencyKey = idempotencyKey; }
    /**
     * Retrieves occurred at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
}