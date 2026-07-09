/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : EnterpriseIssue.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterpriseIssueController
 * Related Service   : EnterpriseIssueService, EnterpriseIssueServiceImpl
 * Related Repository: EnterpriseIssueRepository
 * Related Entity    : EnterpriseIssue
 * Related DTO       : N/A
 * Related Mapper    : EnterpriseIssueMapper
 * Related DB Table  : grc_enterprise_issues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterpriseIssueRepository, EnterpriseIssueMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_enterprise_issues'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code EnterpriseIssue}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_enterprise_issues'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_enterprise_issues}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_enterprise_issues")
public class EnterpriseIssue {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "issue_number", nullable = false, unique = true, length = 50) private String issueNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 50) private String source;
    @Column(nullable = false, length = 30) private String status = "OPEN";
    @Column(nullable = false, length = 20) private String severity = "MEDIUM";
    @Column(name = "owner_id") private Long ownerId;
    @Column(name = "due_date") private LocalDate dueDate;
    @Column(name = "created_at", nullable = false, updatable = false) private LocalDateTime createdAt = LocalDateTime.now();
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
     * Retrieves issue number data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIssueNumber() { return issueNumber; } public void setIssueNumber(String v) { this.issueNumber = v; }
    /**
     * Retrieves title data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    /**
     * Retrieves source data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSource() { return source; } public void setSource(String v) { this.source = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves severity data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; } public void setSeverity(String v) { this.severity = v; }
    /**
     * Retrieves owner id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOwnerId() { return ownerId; } public void setOwnerId(Long v) { this.ownerId = v; }
    /**
     * Retrieves due date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getDueDate() { return dueDate; } public void setDueDate(LocalDate v) { this.dueDate = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}