/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectWbs.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectWbsController
 * Related Service   : ProjectWbsService, ProjectWbsServiceImpl
 * Related Repository: ProjectWbsRepository
 * Related Entity    : ProjectWbs
 * Related DTO       : N/A
 * Related Mapper    : ProjectWbsMapper
 * Related DB Table  : project_wbs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectWbsRepository, ProjectWbsMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_wbs'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectWbs}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_wbs'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_wbs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_wbs")
public class ProjectWbs {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "current_version", nullable = false)
    private Integer currentVersion = 1;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

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
     * Retrieves project id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProjectId() { return projectId; }
    /**
     * Performs the setProjectId operation in this module.
     *
     * @param projectId the projectId input value
     */
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    /**
     * Retrieves current version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getCurrentVersion() { return currentVersion; }
    /**
     * Performs the setCurrentVersion operation in this module.
     *
     * @param currentVersion the currentVersion input value
     */
    public void setCurrentVersion(Integer currentVersion) { this.currentVersion = currentVersion; }
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
}