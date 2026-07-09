/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : AuditFinding.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditFindingController
 * Related Service   : AuditFindingService, AuditFindingServiceImpl
 * Related Repository: AuditFindingRepository
 * Related Entity    : AuditFinding
 * Related DTO       : N/A
 * Related Mapper    : AuditFindingMapper
 * Related DB Table  : grc_audit_findings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditFindingRepository, AuditFindingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_audit_findings'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code AuditFinding}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_audit_findings'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_audit_findings}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_audit_findings")
public class AuditFinding {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "engagement_id", nullable = false) private Long engagementId;
    @Column(name = "finding_number", nullable = false, unique = true, length = 50) private String findingNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 20) private String severity = "MEDIUM";
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(columnDefinition = "TEXT") private String description;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves engagement id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEngagementId() { return engagementId; } public void setEngagementId(Long v) { this.engagementId = v; }
    /**
     * Retrieves finding number data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFindingNumber() { return findingNumber; } public void setFindingNumber(String v) { this.findingNumber = v; }
    /**
     * Retrieves title data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    /**
     * Retrieves severity data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves description data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; } public void setDescription(String v) { this.description = v; }
}