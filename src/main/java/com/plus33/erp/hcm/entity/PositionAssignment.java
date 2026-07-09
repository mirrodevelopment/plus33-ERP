/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : PositionAssignment.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PositionAssignmentController
 * Related Service   : PositionAssignmentService, PositionAssignmentServiceImpl
 * Related Repository: PositionAssignmentRepository
 * Related Entity    : PositionAssignment
 * Related DTO       : N/A
 * Related Mapper    : PositionAssignmentMapper
 * Related DB Table  : hcm_position_assignments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PositionAssignmentRepository, PositionAssignmentMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_position_assignments'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code PositionAssignment}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_position_assignments'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_position_assignments}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_position_assignments")
public class PositionAssignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "position_id", nullable = false)
    private Long positionId;

    @Column(name = "employee_id")
    private Long employeeId;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    @Column(name = "is_current", nullable = false)
    private Boolean isCurrent = true;

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
     * Retrieves position id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPositionId() { return positionId; }
    /**
     * Performs the setPositionId operation in this module.
     *
     * @param positionId the positionId input value
     */
    public void setPositionId(Long positionId) { this.positionId = positionId; }
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
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves is current data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getIsCurrent() { return isCurrent; }
    /**
     * Performs the setIsCurrent operation in this module.
     *
     * @param isCurrent the isCurrent input value
     */
    public void setIsCurrent(Boolean isCurrent) { this.isCurrent = isCurrent; }
}