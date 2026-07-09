/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEsgComplianceLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEsgComplianceLogController
 * Related Service   : PlatformEsgComplianceLogService, PlatformEsgComplianceLogServiceImpl
 * Related Repository: PlatformEsgComplianceLogRepository
 * Related Entity    : PlatformEsgComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEsgComplianceLogMapper
 * Related DB Table  : platform_esg_compliance_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEsgComplianceLogRepository, PlatformEsgComplianceLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_esg_compliance_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEsgComplianceLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_esg_compliance_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_esg_compliance_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_esg_compliance_log")
public class PlatformEsgComplianceLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String framework; // GRI, SASB, CSRD

    @Column(name = "compliance_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal complianceScore;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // COMPLIANT, WARNING, NON_COMPLIANT

    @Column(name = "finding_summary", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String findingSummary;

    @Column(name = "corrective_action", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String correctiveAction;

    @Column(name = "owner_name", nullable = false)
    @NotNull
    @Size(max = 100)
    private String ownerName;

    @Column(name = "next_review_date", nullable = false)
    @NotNull
    private LocalDateTime nextReviewDate;

    @Column(name = "audited_at", nullable = false)
    @NotNull
    private LocalDateTime auditedAt = LocalDateTime.now();

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
     * Retrieves framework data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFramework() { return framework; }
    /**
     * Performs the setFramework operation in this module.
     *
     * @param framework the framework input value
     */
    public void setFramework(String framework) { this.framework = framework; }
    /**
     * Retrieves compliance score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getComplianceScore() { return complianceScore; }
    /**
     * Performs the setComplianceScore operation in this module.
     *
     * @param complianceScore the complianceScore input value
     */
    public void setComplianceScore(BigDecimal complianceScore) { this.complianceScore = complianceScore; }
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
     * Retrieves finding summary data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFindingSummary() { return findingSummary; }
    /**
     * Performs the setFindingSummary operation in this module.
     *
     * @param findingSummary the findingSummary input value
     */
    public void setFindingSummary(String findingSummary) { this.findingSummary = findingSummary; }
    /**
     * Retrieves corrective action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrectiveAction() { return correctiveAction; }
    /**
     * Performs the setCorrectiveAction operation in this module.
     *
     * @param correctiveAction the correctiveAction input value
     */
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    /**
     * Retrieves owner name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOwnerName() { return ownerName; }
    /**
     * Performs the setOwnerName operation in this module.
     *
     * @param ownerName the ownerName input value
     */
    public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
    /**
     * Retrieves next review date data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getNextReviewDate() { return nextReviewDate; }
    /**
     * Performs the setNextReviewDate operation in this module.
     *
     * @param nextReviewDate the nextReviewDate input value
     */
    public void setNextReviewDate(LocalDateTime nextReviewDate) { this.nextReviewDate = nextReviewDate; }
    /**
     * Retrieves audited at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAuditedAt() { return auditedAt; }
    /**
     * Performs the setAuditedAt operation in this module.
     *
     * @param auditedAt the auditedAt input value
     */
    public void setAuditedAt(LocalDateTime auditedAt) { this.auditedAt = auditedAt; }
}