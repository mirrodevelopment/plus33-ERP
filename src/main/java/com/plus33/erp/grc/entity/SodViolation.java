/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.entity
 * File              : SodViolation.java
 * Purpose           : JPA Entity representing a persistent database record in Grc Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SodViolationController
 * Related Service   : SodViolationService, SodViolationServiceImpl
 * Related Repository: SodViolationRepository
 * Related Entity    : SodViolation
 * Related DTO       : N/A
 * Related Mapper    : SodViolationMapper
 * Related DB Table  : grc_sod_violations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SodViolationRepository, SodViolationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'grc_sod_violations'. Defines persistent domain object for Grc Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.grc.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code SodViolation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'grc_sod_violations'.</p>
 *
 * <p><b>Database Table   :</b> {@code grc_sod_violations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "grc_sod_violations")
public class SodViolation {
    /**
     * Retrieves id data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(name = "sod_rule_id", nullable = false) private Long sodRuleId;
    @Column(name = "user_id", nullable = false) private Long userId;
    @Column(name = "detected_at", nullable = false) private LocalDateTime detectedAt = LocalDateTime.now();
    @Column(nullable = false, length = 20) private String status = "OPEN";
    public Long getId() { return id; } public void setId(Long id) { this.id = id; }
    /**
     * Retrieves sod rule id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSodRuleId() { return sodRuleId; } public void setSodRuleId(Long v) { this.sodRuleId = v; }
    /**
     * Retrieves user id data from the database.
     *
     * @param v the v input value
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUserId() { return userId; } public void setUserId(Long v) { this.userId = v; }
    /**
     * Retrieves detected at data from the database.
     *
     * @param v the v input value
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDetectedAt() { return detectedAt; } public void setDetectedAt(LocalDateTime v) { this.detectedAt = v; }
    /**
     * Retrieves status data from the database.
     *
     * @param v the v input value
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; } public void setStatus(String v) { this.status = v; }
}