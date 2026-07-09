/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : RiskAppetiteStatement.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RiskAppetiteStatementController
 * Related Service   : RiskAppetiteStatementService, RiskAppetiteStatementServiceImpl
 * Related Repository: RiskAppetiteStatementRepository
 * Related Entity    : RiskAppetiteStatement
 * Related DTO       : N/A
 * Related Mapper    : RiskAppetiteStatementMapper
 * Related DB Table  : grc_risk_appetite_statements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RiskAppetiteStatementRepository, RiskAppetiteStatementMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_risk_appetite_statements'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code RiskAppetiteStatement}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_risk_appetite_statements'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_risk_appetite_statements}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_risk_appetite_statements")
public class RiskAppetiteStatement {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "risk_category", nullable = false, length = 50) private String riskCategory;
    @Column(name = "max_residual_score", nullable = false) private BigDecimal maxResidualScore;
    @Column(name = "escalation_threshold", nullable = false) private BigDecimal escalationThreshold;
    @Column(name = "approval_authority", nullable = false, length = 100) private String approvalAuthority;
    @Column(name = "effective_from", nullable = false) private LocalDate effectiveFrom;
    @Column(name = "effective_to", nullable = false) private LocalDate effectiveTo;
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
     * Retrieves risk category data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRiskCategory() { return riskCategory; } public void setRiskCategory(String v) { this.riskCategory = v; }
    /**
     * Retrieves max residual score data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxResidualScore() { return maxResidualScore; } public void setMaxResidualScore(BigDecimal v) { this.maxResidualScore = v; }
    /**
     * Retrieves escalation threshold data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEscalationThreshold() { return escalationThreshold; } public void setEscalationThreshold(BigDecimal v) { this.escalationThreshold = v; }
    /**
     * Retrieves approval authority data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovalAuthority() { return approvalAuthority; } public void setApprovalAuthority(String v) { this.approvalAuthority = v; }
    /**
     * Retrieves effective from data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; } public void setEffectiveFrom(LocalDate v) { this.effectiveFrom = v; }
    /**
     * Retrieves effective to data from the database.
     *
     * @param v the v input value
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; } public void setEffectiveTo(LocalDate v) { this.effectiveTo = v; }
}