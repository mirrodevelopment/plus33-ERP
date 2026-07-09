/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectChangeRequest.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectChangeController
 * Related Service   : ProjectChangeService, ProjectChangeServiceImpl
 * Related Repository: ProjectChangeRepository
 * Related Entity    : ProjectChangeRequest
 * Related DTO       : ProjectChangeRequest
 * Related Mapper    : ProjectChangeMapper
 * Related DB Table  : project_change_requests
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectChangeRepository, ProjectChangeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_change_requests'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectChangeRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_change_requests'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_change_requests}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_change_requests")
public class ProjectChangeRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "request_number", nullable = false, unique = true, length = 50)
    private String requestNumber;

    @Column(name = "change_type", nullable = false, length = 30)
    private String changeType;

    @Column(name = "impact_analysis", columnDefinition = "TEXT")
    private String impactAnalysis;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

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
     * Retrieves request number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequestNumber() { return requestNumber; }
    /**
     * Performs the setRequestNumber operation in this module.
     *
     * @param requestNumber the requestNumber input value
     */
    public void setRequestNumber(String requestNumber) { this.requestNumber = requestNumber; }
    /**
     * Retrieves change type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChangeType() { return changeType; }
    /**
     * Performs the setChangeType operation in this module.
     *
     * @param changeType the changeType input value
     */
    public void setChangeType(String changeType) { this.changeType = changeType; }
    /**
     * Retrieves impact analysis data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getImpactAnalysis() { return impactAnalysis; }
    /**
     * Performs the setImpactAnalysis operation in this module.
     *
     * @param impactAnalysis the impactAnalysis input value
     */
    public void setImpactAnalysis(String impactAnalysis) { this.impactAnalysis = impactAnalysis; }
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