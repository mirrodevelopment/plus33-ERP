/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : Roster.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RosterController
 * Related Service   : RosterService, RosterServiceImpl
 * Related Repository: RosterRepository
 * Related Entity    : Roster
 * Related DTO       : N/A
 * Related Mapper    : RosterMapper
 * Related DB Table  : hcm_rosters
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RosterRepository, RosterMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_rosters'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code Roster}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_rosters'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_rosters}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_rosters")
public class Roster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "shift_date", nullable = false)
    private LocalDate shiftDate;

    @Column(name = "shift_pattern_id", nullable = false)
    private Long shiftPatternId;

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
     * Retrieves shift date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getShiftDate() { return shiftDate; }
    /**
     * Performs the setShiftDate operation in this module.
     *
     * @param shiftDate the shiftDate input value
     */
    public void setShiftDate(LocalDate shiftDate) { this.shiftDate = shiftDate; }
    /**
     * Retrieves shift pattern id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getShiftPatternId() { return shiftPatternId; }
    /**
     * Performs the setShiftPatternId operation in this module.
     *
     * @param shiftPatternId the shiftPatternId input value
     */
    public void setShiftPatternId(Long shiftPatternId) { this.shiftPatternId = shiftPatternId; }
}