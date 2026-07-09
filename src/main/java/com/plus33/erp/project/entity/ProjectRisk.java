/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectRisk.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectRiskController
 * Related Service   : ProjectRiskService, ProjectRiskServiceImpl
 * Related Repository: ProjectRiskRepository
 * Related Entity    : ProjectRisk
 * Related DTO       : N/A
 * Related Mapper    : ProjectRiskMapper
 * Related DB Table  : project_risks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectRiskRepository, ProjectRiskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_risks'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectRisk}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_risks'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_risks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_risks")
public class ProjectRisk {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(nullable = false, length = 255)
    private String description;

    @Column(nullable = false, length = 20)
    private String probability = "LOW";

    @Column(nullable = false, length = 20)
    private String impact = "LOW";

    @Column(name = "mitigation_plan", columnDefinition = "TEXT")
    private String mitigationPlan;

    @Column(nullable = false, length = 30)
    private String status = "ACTIVE";

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
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves probability data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getProbability() { return probability; }
    /**
     * Performs the setProbability operation in this module.
     *
     * @param probability the probability input value
     */
    public void setProbability(String probability) { this.probability = probability; }
    /**
     * Retrieves impact data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getImpact() { return impact; }
    /**
     * Performs the setImpact operation in this module.
     *
     * @param impact the impact input value
     */
    public void setImpact(String impact) { this.impact = impact; }
    /**
     * Retrieves mitigation plan data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMitigationPlan() { return mitigationPlan; }
    /**
     * Performs the setMitigationPlan operation in this module.
     *
     * @param mitigationPlan the mitigationPlan input value
     */
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }
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