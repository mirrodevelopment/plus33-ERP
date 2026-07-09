/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.entity
 * File              : PayrollAuditEvent.java
 * Purpose           : JPA Entity representing a persistent database record in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollAuditEventController
 * Related Service   : PayrollAuditEventService, PayrollAuditEventServiceImpl
 * Related Repository: PayrollAuditEventRepository
 * Related Entity    : PayrollAuditEvent
 * Related DTO       : N/A
 * Related Mapper    : PayrollAuditEventMapper
 * Related DB Table  : payroll_audit_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PayrollAuditEventRepository, PayrollAuditEventMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payroll_audit_events'. Defines persistent domain object for Workforce Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollAuditEvent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payroll_audit_events'.</p>
 *
 * <p><b>Database Table   :</b> {@code payroll_audit_events}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "payroll_audit_events")
public class PayrollAuditEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "payroll_run_id")
    private Long payrollRunId;

    @Column(name = "event_type", nullable = false, length = 100)
    private String eventType;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, length = 100)
    private String actor;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public PayrollAuditEvent() {}

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
     * Retrieves payroll run id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPayrollRunId() { return payrollRunId; }
    /**
     * Performs the setPayrollRunId operation in this module.
     *
     * @param payrollRunId the payrollRunId input value
     */
    public void setPayrollRunId(Long payrollRunId) { this.payrollRunId = payrollRunId; }
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
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves actor data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActor() { return actor; }
    /**
     * Performs the setActor operation in this module.
     *
     * @param actor the actor input value
     */
    public void setActor(String actor) { this.actor = actor; }
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
}