/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : AuditEngagement.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditEngagementController
 * Related Service   : AuditEngagementService, AuditEngagementServiceImpl
 * Related Repository: AuditEngagementRepository
 * Related Entity    : AuditEngagement
 * Related DTO       : N/A
 * Related Mapper    : AuditEngagementMapper
 * Related DB Table  : grc_audit_engagements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditEngagementRepository, AuditEngagementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_audit_engagements'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code AuditEngagement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_audit_engagements'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_audit_engagements}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_audit_engagements")
public class AuditEngagement {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "program_id", nullable = false) private Long programId;
    @Column(name = "audit_universe_id", nullable = false) private Long auditUniverseId;
    @Column(name = "engagement_number", nullable = false, unique = true, length = 50) private String engagementNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 30) private String status = "PLANNED";
    @Column(name = "lead_auditor_id") private Long leadAuditorId;
    @Column(name = "start_date") private LocalDate startDate;
    @Column(name = "end_date") private LocalDate endDate;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves program id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProgramId() { return programId; } public void setProgramId(Long v) { this.programId = v; }
    /**
     * Retrieves audit universe id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAuditUniverseId() { return auditUniverseId; } public void setAuditUniverseId(Long v) { this.auditUniverseId = v; }
    /**
     * Retrieves engagement number data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEngagementNumber() { return engagementNumber; } public void setEngagementNumber(String v) { this.engagementNumber = v; }
    /**
     * Retrieves title data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves lead auditor id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLeadAuditorId() { return leadAuditorId; } public void setLeadAuditorId(Long v) { this.leadAuditorId = v; }
    /**
     * Retrieves start date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getStartDate() { return startDate; } public void setStartDate(LocalDate v) { this.startDate = v; }
    /**
     * Retrieves end date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEndDate() { return endDate; } public void setEndDate(LocalDate v) { this.endDate = v; }
}