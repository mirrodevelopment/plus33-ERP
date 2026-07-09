/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectCost.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectCostController
 * Related Service   : ProjectCostService, ProjectCostServiceImpl
 * Related Repository: ProjectCostRepository
 * Related Entity    : ProjectCost
 * Related DTO       : N/A
 * Related Mapper    : ProjectCostMapper
 * Related DB Table  : project_cost_ledger
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectCostRepository, ProjectCostMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_cost_ledger'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectCost}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_cost_ledger'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_cost_ledger}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_cost_ledger")
public class ProjectCost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "project_id", nullable = false)
    private Long projectId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "cost_type", nullable = false, length = 30)
    private String costType;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "source_module", length = 50)
    private String sourceModule;

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "recorded_at", nullable = false, updatable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves task id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTaskId() { return taskId; }
    /**
     * Performs the setTaskId operation in this module.
     *
     * @param taskId the taskId input value
     */
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    /**
     * Retrieves cost type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCostType() { return costType; }
    /**
     * Performs the setCostType operation in this module.
     *
     * @param costType the costType input value
     */
    public void setCostType(String costType) { this.costType = costType; }
    /**
     * Retrieves amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAmount() { return amount; }
    /**
     * Performs the setAmount operation in this module.
     *
     * @param amount the amount input value
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    /**
     * Retrieves source module data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSourceModule() { return sourceModule; }
    /**
     * Performs the setSourceModule operation in this module.
     *
     * @param sourceModule the sourceModule input value
     */
    public void setSourceModule(String sourceModule) { this.sourceModule = sourceModule; }
    /**
     * Retrieves source id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceId() { return sourceId; }
    /**
     * Performs the setSourceId operation in this module.
     *
     * @param sourceId the sourceId input value
     */
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
}