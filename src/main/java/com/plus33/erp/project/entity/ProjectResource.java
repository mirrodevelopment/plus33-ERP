/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectResource.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectResourceController
 * Related Service   : ProjectResourceService, ProjectResourceServiceImpl
 * Related Repository: ProjectResourceRepository
 * Related Entity    : ProjectResource
 * Related DTO       : N/A
 * Related Mapper    : ProjectResourceMapper
 * Related DB Table  : project_resources
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectResourceRepository, ProjectResourceMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_resources'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectResource}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_resources'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_resources}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_resources")
public class ProjectResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(unique = true, length = 100)
    private String email;

    @Column(name = "capacity_hours_per_week", nullable = false)
    private BigDecimal capacityHoursPerWeek = new BigDecimal("40.00");

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getName() { return name; }
    /**
     * Performs the setName operation in this module.
     *
     * @param name the name input value
     */
    public void setName(String name) { this.name = name; }
    /**
     * Retrieves email data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEmail() { return email; }
    /**
     * Performs the setEmail operation in this module.
     *
     * @param email the email input value
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Retrieves capacity hours per week data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCapacityHoursPerWeek() { return capacityHoursPerWeek; }
    /**
     * Performs the setCapacityHoursPerWeek operation in this module.
     *
     * @param capacityHoursPerWeek the capacityHoursPerWeek input value
     */
    public void setCapacityHoursPerWeek(BigDecimal capacityHoursPerWeek) { this.capacityHoursPerWeek = capacityHoursPerWeek; }
}