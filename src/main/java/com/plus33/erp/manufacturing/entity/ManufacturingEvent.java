/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingEvent.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingEventController
 * Related Service   : ManufacturingEventService, ManufacturingEventServiceImpl
 * Related Repository: ManufacturingEventRepository
 * Related Entity    : ManufacturingEvent
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingEventMapper
 * Related DB Table  : manufacturing_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingEventRepository, ManufacturingEventMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_events'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingEvent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_events'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_events}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "manufacturing_events")
public class ManufacturingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "event_type", nullable = false, length = 80)
    private String eventType;

    @Column(name = "reference_type", nullable = false, length = 30)
    private String referenceType;

    @Column(name = "reference_id", nullable = false)
    private Long referenceId;

    @Column(name = "event_data", columnDefinition = "jsonb")
    @JdbcTypeCode(SqlTypes.JSON)
    private String eventData;

    @Column(name = "triggered_by")
    private Long triggeredBy;

    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();

    public ManufacturingEvent() {}

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
     * Retrieves reference type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceType() { return referenceType; }
    /**
     * Performs the setReferenceType operation in this module.
     *
     * @param referenceType the referenceType input value
     */
    public void setReferenceType(String referenceType) { this.referenceType = referenceType; }
    /**
     * Retrieves reference id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getReferenceId() { return referenceId; }
    /**
     * Performs the setReferenceId operation in this module.
     *
     * @param referenceId the referenceId input value
     */
    public void setReferenceId(Long referenceId) { this.referenceId = referenceId; }
    /**
     * Retrieves event data data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventData() { return eventData; }
    /**
     * Performs the setEventData operation in this module.
     *
     * @param eventData the eventData input value
     */
    public void setEventData(String eventData) { this.eventData = eventData; }
    /**
     * Retrieves triggered by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTriggeredBy() { return triggeredBy; }
    /**
     * Performs the setTriggeredBy operation in this module.
     *
     * @param triggeredBy the triggeredBy input value
     */
    public void setTriggeredBy(Long triggeredBy) { this.triggeredBy = triggeredBy; }
    /**
     * Retrieves occurred at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getOccurredAt() { return occurredAt; }
    /**
     * Performs the setOccurredAt operation in this module.
     *
     * @param occurredAt the occurredAt input value
     */
    public void setOccurredAt(LocalDateTime occurredAt) { this.occurredAt = occurredAt; }
}