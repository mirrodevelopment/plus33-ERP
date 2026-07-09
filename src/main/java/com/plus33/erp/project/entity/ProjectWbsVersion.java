/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectWbsVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectWbsVersionController
 * Related Service   : ProjectWbsVersionService, ProjectWbsVersionServiceImpl
 * Related Repository: ProjectWbsVersionRepository
 * Related Entity    : ProjectWbsVersion
 * Related DTO       : N/A
 * Related Mapper    : ProjectWbsVersionMapper
 * Related DB Table  : project_wbs_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectWbsVersionRepository, ProjectWbsVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_wbs_versions'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectWbsVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_wbs_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_wbs_versions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_wbs_versions")
public class ProjectWbsVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wbs_id", nullable = false)
    private Long wbsId;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

    @Column(name = "effective_date", nullable = false)
    private LocalDate effectiveDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves wbs id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWbsId() { return wbsId; }
    /**
     * Performs the setWbsId operation in this module.
     *
     * @param wbsId the wbsId input value
     */
    public void setWbsId(Long wbsId) { this.wbsId = wbsId; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
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
     * Retrieves effective date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveDate() { return effectiveDate; }
    /**
     * Performs the setEffectiveDate operation in this module.
     *
     * @param effectiveDate the effectiveDate input value
     */
    public void setEffectiveDate(LocalDate effectiveDate) { this.effectiveDate = effectiveDate; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}