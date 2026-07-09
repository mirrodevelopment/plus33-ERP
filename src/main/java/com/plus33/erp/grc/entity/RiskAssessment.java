/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : RiskAssessment.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskAssessmentController
 * Related Service   : RiskAssessmentService, RiskAssessmentServiceImpl
 * Related Repository: RiskAssessmentRepository
 * Related Entity    : RiskAssessment
 * Related DTO       : N/A
 * Related Mapper    : RiskAssessmentMapper
 * Related DB Table  : grc_risk_assessments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskAssessmentRepository, RiskAssessmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_risk_assessments'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskAssessment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_risk_assessments'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_risk_assessments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_risk_assessments")
public class RiskAssessment {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "risk_id", nullable = false) private Long riskId;
    @Column(nullable = false) private BigDecimal probability;
    @Column(nullable = false) private BigDecimal impact;
    @Column(name = "residual_score", nullable = false) private BigDecimal residualScore;
    @Column(name = "assessment_date", nullable = false) private LocalDate assessmentDate;
    @Column(name = "assessor_id") private Long assessorId;
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves risk id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRiskId() { return riskId; } public void setRiskId(Long v) { this.riskId = v; }
    /**
     * Retrieves probability data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getProbability() { return probability; } public void setProbability(BigDecimal v) { this.probability = v; }
    /**
     * Retrieves impact data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getImpact() { return impact; } public void setImpact(BigDecimal v) { this.impact = v; }
    /**
     * Retrieves residual score data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getResidualScore() { return residualScore; } public void setResidualScore(BigDecimal v) { this.residualScore = v; }
    /**
     * Retrieves assessment date data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getAssessmentDate() { return assessmentDate; } public void setAssessmentDate(LocalDate v) { this.assessmentDate = v; }
    /**
     * Retrieves assessor id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssessorId() { return assessorId; } public void setAssessorId(Long v) { this.assessorId = v; }
}