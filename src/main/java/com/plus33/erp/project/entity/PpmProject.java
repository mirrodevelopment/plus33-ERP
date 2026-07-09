/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : PpmProject.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PpmProjectController
 * Related Service   : PpmProjectService, PpmProjectServiceImpl
 * Related Repository: PpmProjectRepository
 * Related Entity    : PpmProject
 * Related DTO       : N/A
 * Related Mapper    : PpmProjectMapper
 * Related DB Table  : ppm_projects
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PpmProjectRepository, PpmProjectMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'ppm_projects'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code PpmProject}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'ppm_projects'.</p>
 *
 * <p><b>Database Table   :</b> {@code ppm_projects}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "ppm_projects")
public class PpmProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "project_number", nullable = false, unique = true, length = 50)
    private String projectNumber;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(name = "budget_amount", nullable = false)
    private BigDecimal budgetAmount = BigDecimal.ZERO;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "program_id")
    private Long programId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves customer id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCustomerId() { return customerId; }
    /**
     * Performs the setCustomerId operation in this module.
     *
     * @param customerId the customerId input value
     */
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    /**
     * Retrieves project number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProjectNumber() { return projectNumber; }
    /**
     * Performs the setProjectNumber operation in this module.
     *
     * @param projectNumber the projectNumber input value
     */
    public void setProjectNumber(String projectNumber) { this.projectNumber = projectNumber; }
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
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves budget amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBudgetAmount() { return budgetAmount; }
    /**
     * Performs the setBudgetAmount operation in this module.
     *
     * @param budgetAmount the budgetAmount input value
     */
    public void setBudgetAmount(BigDecimal budgetAmount) { this.budgetAmount = budgetAmount; }
    /**
     * Retrieves start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getStartDate() { return startDate; }
    /**
     * Performs the setStartDate operation in this module.
     *
     * @param startDate inclusive start date for date-range filtering
     */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    /**
     * Retrieves end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEndDate() { return endDate; }
    /**
     * Performs the setEndDate operation in this module.
     *
     * @param endDate inclusive end date for date-range filtering
     */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    /**
     * Retrieves program id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProgramId() { return programId; }
    /**
     * Performs the setProgramId operation in this module.
     *
     * @param programId the programId input value
     */
    public void setProgramId(Long programId) { this.programId = programId; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}