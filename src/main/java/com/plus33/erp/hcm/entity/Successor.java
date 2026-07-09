/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : Successor.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SuccessorController
 * Related Service   : SuccessorService, SuccessorServiceImpl
 * Related Repository: SuccessorRepository
 * Related Entity    : Successor
 * Related DTO       : N/A
 * Related Mapper    : SuccessorMapper
 * Related DB Table  : hcm_successors
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SuccessorRepository, SuccessorMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_successors'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code Successor}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_successors'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_successors}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_successors")
public class Successor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "talent_pool_id", nullable = false)
    private Long talentPoolId;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "readiness_score", nullable = false)
    private BigDecimal readinessScore = BigDecimal.ZERO;

    // Getters and setters
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
     * Retrieves talent pool id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTalentPoolId() { return talentPoolId; }
    /**
     * Performs the setTalentPoolId operation in this module.
     *
     * @param talentPoolId the talentPoolId input value
     */
    public void setTalentPoolId(Long talentPoolId) { this.talentPoolId = talentPoolId; }
    /**
     * Retrieves employee id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getEmployeeId() { return employeeId; }
    /**
     * Performs the setEmployeeId operation in this module.
     *
     * @param employeeId the employeeId input value
     */
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    /**
     * Retrieves readiness score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReadinessScore() { return readinessScore; }
    /**
     * Performs the setReadinessScore operation in this module.
     *
     * @param readinessScore the readinessScore input value
     */
    public void setReadinessScore(BigDecimal readinessScore) { this.readinessScore = readinessScore; }
}