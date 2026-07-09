/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : EnterpriseRisk.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterpriseRiskController
 * Related Service   : EnterpriseRiskService, EnterpriseRiskServiceImpl
 * Related Repository: EnterpriseRiskRepository
 * Related Entity    : EnterpriseRisk
 * Related DTO       : N/A
 * Related Mapper    : EnterpriseRiskMapper
 * Related DB Table  : grc_risks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterpriseRiskRepository, EnterpriseRiskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_risks'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code EnterpriseRisk}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_risks'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_risks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_risks")
public class EnterpriseRisk {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "risk_number", nullable = false, unique = true, length = 50) private String riskNumber;
    @Column(nullable = false, length = 200) private String title;
    @Column(nullable = false, length = 50) private String category;
    @Column(length = 100) private String domain;
    @Column(name = "inherent_score", nullable = false) private BigDecimal inherentScore = BigDecimal.ZERO;
    @Column(name = "residual_score", nullable = false) private BigDecimal residualScore = BigDecimal.ZERO;
    @Column(nullable = false, length = 30) private String status = "IDENTIFIED";
    @Column(name = "owner_employee_id") private Long ownerEmployeeId;
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
     * Retrieves risk number data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRiskNumber() { return riskNumber; } public void setRiskNumber(String v) { this.riskNumber = v; }
    /**
     * Retrieves title data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; } public void setTitle(String v) { this.title = v; }
    /**
     * Retrieves category data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCategory() { return category; } public void setCategory(String v) { this.category = v; }
    /**
     * Retrieves domain data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDomain() { return domain; } public void setDomain(String v) { this.domain = v; }
    /**
     * Retrieves inherent score data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInherentScore() { return inherentScore; } public void setInherentScore(BigDecimal v) { this.inherentScore = v; }
    /**
     * Retrieves residual score data from the database.
     *
     * @param v the v input value
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getResidualScore() { return residualScore; } public void setResidualScore(BigDecimal v) { this.residualScore = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
    /**
     * Retrieves owner employee id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOwnerEmployeeId() { return ownerEmployeeId; } public void setOwnerEmployeeId(Long v) { this.ownerEmployeeId = v; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}