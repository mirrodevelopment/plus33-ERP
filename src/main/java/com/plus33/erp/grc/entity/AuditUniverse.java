/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : AuditUniverse.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditUniverseController
 * Related Service   : AuditUniverseService, AuditUniverseServiceImpl
 * Related Repository: AuditUniverseRepository
 * Related Entity    : AuditUniverse
 * Related DTO       : N/A
 * Related Mapper    : AuditUniverseMapper
 * Related DB Table  : grc_audit_universe
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditUniverseRepository, AuditUniverseMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_audit_universe'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code AuditUniverse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_audit_universe'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_audit_universe}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_audit_universe")
public class AuditUniverse {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "company_id", nullable = false) private Long companyId;
    @Column(name = "entity_name", nullable = false, length = 150) private String entityName;
    @Column(name = "entity_type", nullable = false, length = 50) private String entityType;
    @Column(name = "risk_score") private java.math.BigDecimal riskScore = java.math.BigDecimal.ZERO;
    @Column(name = "last_audited") private java.time.LocalDate lastAudited;
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
     * Retrieves entity name data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEntityName() { return entityName; } public void setEntityName(String v) { this.entityName = v; }
    /**
     * Retrieves entity type data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEntityType() { return entityType; } public void setEntityType(String v) { this.entityType = v; }
    /**
     * Retrieves risk score data from the database.
     *
     * @param v the v input value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.math.BigDecimal getRiskScore() { return riskScore; } public void setRiskScore(java.math.BigDecimal v) { this.riskScore = v; }
    /**
     * Retrieves last audited data from the database.
     *
     * @param v the v input value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.time.LocalDate getLastAudited() { return lastAudited; } public void setLastAudited(java.time.LocalDate v) { this.lastAudited = v; }
}