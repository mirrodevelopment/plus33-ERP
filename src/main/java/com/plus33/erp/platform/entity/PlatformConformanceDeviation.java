/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformConformanceDeviation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformConformanceDeviationController
 * Related Service   : PlatformConformanceDeviationService, PlatformConformanceDeviationServiceImpl
 * Related Repository: PlatformConformanceDeviationRepository
 * Related Entity    : PlatformConformanceDeviation
 * Related DTO       : N/A
 * Related Mapper    : PlatformConformanceDeviationMapper
 * Related DB Table  : platform_conformance_deviation
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformConformanceDeviationRepository, PlatformConformanceDeviationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_conformance_deviation'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformConformanceDeviation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_conformance_deviation'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_conformance_deviation}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_conformance_deviation")
public class PlatformConformanceDeviation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    @NotNull
    private Long caseId;

    @Column(name = "rule_id", nullable = false)
    @NotNull
    private Long ruleId;

    @Column(name = "deviation_details", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String deviationDetails;

    @Column(name = "sla_breach_risk", nullable = false)
    @NotNull
    private Boolean slaBreachRisk = false;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

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
     * Retrieves case id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCaseId() { return caseId; }
    /**
     * Performs the setCaseId operation in this module.
     *
     * @param caseId the caseId input value
     */
    public void setCaseId(Long caseId) { this.caseId = caseId; }
    /**
     * Retrieves rule id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRuleId() { return ruleId; }
    /**
     * Performs the setRuleId operation in this module.
     *
     * @param ruleId the ruleId input value
     */
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }
    /**
     * Retrieves deviation details data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDeviationDetails() { return deviationDetails; }
    /**
     * Performs the setDeviationDetails operation in this module.
     *
     * @param deviationDetails the deviationDetails input value
     */
    public void setDeviationDetails(String deviationDetails) { this.deviationDetails = deviationDetails; }
    /**
     * Retrieves sla breach risk data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getSlaBreachRisk() { return slaBreachRisk; }
    /**
     * Performs the setSlaBreachRisk operation in this module.
     *
     * @param slaBreachRisk the slaBreachRisk input value
     */
    public void setSlaBreachRisk(Boolean slaBreachRisk) { this.slaBreachRisk = slaBreachRisk; }
    /**
     * Retrieves detected at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDetectedAt() { return detectedAt; }
    /**
     * Performs the setDetectedAt operation in this module.
     *
     * @param detectedAt the detectedAt input value
     */
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}