/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.entity
 * File              : EmployeeCompetency.java
 * Purpose           : JPA Entity representing a persistent database record in Hcm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeCompetencyController
 * Related Service   : EmployeeCompetencyService, EmployeeCompetencyServiceImpl
 * Related Repository: EmployeeCompetencyRepository
 * Related Entity    : EmployeeCompetency
 * Related DTO       : N/A
 * Related Mapper    : EmployeeCompetencyMapper
 * Related DB Table  : hcm_employee_competencies
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeCompetencyRepository, EmployeeCompetencyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'hcm_employee_competencies'. Defines persistent domain object for Hcm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.hcm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeCompetency}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'hcm_employee_competencies'.</p>
 *
 * <p><b>Database Table   :</b> {@code hcm_employee_competencies}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "hcm_employee_competencies")
public class EmployeeCompetency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "employee_id", nullable = false)
    private Long employeeId;

    @Column(name = "competency_id", nullable = false)
    private Long competencyId;

    @Column(nullable = false)
    private BigDecimal rating = BigDecimal.ZERO;

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
     * Retrieves competency id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompetencyId() { return competencyId; }
    /**
     * Performs the setCompetencyId operation in this module.
     *
     * @param competencyId the competencyId input value
     */
    public void setCompetencyId(Long competencyId) { this.competencyId = competencyId; }
    /**
     * Retrieves rating data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRating() { return rating; }
    /**
     * Performs the setRating operation in this module.
     *
     * @param rating the rating input value
     */
    public void setRating(BigDecimal rating) { this.rating = rating; }
}