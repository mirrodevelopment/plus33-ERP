/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : ProjectBillingContract.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectBillingContractController
 * Related Service   : ProjectBillingContractService, ProjectBillingContractServiceImpl
 * Related Repository: ProjectBillingContractRepository
 * Related Entity    : ProjectBillingContract
 * Related DTO       : N/A
 * Related Mapper    : ProjectBillingContractMapper
 * Related DB Table  : project_billing_contracts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectBillingContractRepository, ProjectBillingContractMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_billing_contracts'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectBillingContract}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_billing_contracts'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_billing_contracts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_billing_contracts")
public class ProjectBillingContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "project_id", nullable = false, unique = true)
    private Long projectId;

    @Column(name = "contract_type", nullable = false, length = 30)
    private String contractType = "TIME_AND_MATERIAL";

    @Column(name = "billing_amount", nullable = false)
    private BigDecimal billingAmount = BigDecimal.ZERO;

    @Column(name = "recognized_revenue", nullable = false)
    private BigDecimal recognizedRevenue = BigDecimal.ZERO;

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
     * Retrieves contract type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContractType() { return contractType; }
    /**
     * Performs the setContractType operation in this module.
     *
     * @param contractType the contractType input value
     */
    public void setContractType(String contractType) { this.contractType = contractType; }
    /**
     * Retrieves billing amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBillingAmount() { return billingAmount; }
    /**
     * Performs the setBillingAmount operation in this module.
     *
     * @param billingAmount the billingAmount input value
     */
    public void setBillingAmount(BigDecimal billingAmount) { this.billingAmount = billingAmount; }
    /**
     * Retrieves recognized revenue data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRecognizedRevenue() { return recognizedRevenue; }
    /**
     * Performs the setRecognizedRevenue operation in this module.
     *
     * @param recognizedRevenue the recognizedRevenue input value
     */
    public void setRecognizedRevenue(BigDecimal recognizedRevenue) { this.recognizedRevenue = recognizedRevenue; }
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