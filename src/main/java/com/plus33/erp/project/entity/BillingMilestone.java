/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.entity
 * File              : BillingMilestone.java
 * Purpose           : JPA Entity representing a persistent database record in Project Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BillingMilestoneController
 * Related Service   : BillingMilestoneService, BillingMilestoneServiceImpl
 * Related Repository: BillingMilestoneRepository
 * Related Entity    : BillingMilestone
 * Related DTO       : N/A
 * Related Mapper    : BillingMilestoneMapper
 * Related DB Table  : project_billing_milestones
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BillingMilestoneRepository, BillingMilestoneMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'project_billing_milestones'. Defines persistent domain object for Project Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.project.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code BillingMilestone}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'project_billing_milestones'.</p>
 *
 * <p><b>Database Table   :</b> {@code project_billing_milestones}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "project_billing_milestones")
public class BillingMilestone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_id", nullable = false)
    private Long contractId;

    @Column(name = "milestone_name", nullable = false, length = 100)
    private String milestoneName;

    @Column(name = "milestone_percentage", nullable = false)
    private BigDecimal milestonePercentage;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Boolean billed = false;

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
     * Retrieves contract id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getContractId() { return contractId; }
    /**
     * Performs the setContractId operation in this module.
     *
     * @param contractId the contractId input value
     */
    public void setContractId(Long contractId) { this.contractId = contractId; }
    /**
     * Retrieves milestone name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMilestoneName() { return milestoneName; }
    /**
     * Performs the setMilestoneName operation in this module.
     *
     * @param milestoneName the milestoneName input value
     */
    public void setMilestoneName(String milestoneName) { this.milestoneName = milestoneName; }
    /**
     * Retrieves milestone percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMilestonePercentage() { return milestonePercentage; }
    /**
     * Performs the setMilestonePercentage operation in this module.
     *
     * @param milestonePercentage the milestonePercentage input value
     */
    public void setMilestonePercentage(BigDecimal milestonePercentage) { this.milestonePercentage = milestonePercentage; }
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
     * Retrieves billed data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getBilled() { return billed; }
    /**
     * Performs the setBilled operation in this module.
     *
     * @param billed the billed input value
     */
    public void setBilled(Boolean billed) { this.billed = billed; }
}