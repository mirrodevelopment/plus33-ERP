/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmOpportunity.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmOpportunityController
 * Related Service   : CrmOpportunityService, CrmOpportunityServiceImpl
 * Related Repository: CrmOpportunityRepository
 * Related Entity    : CrmOpportunity
 * Related DTO       : N/A
 * Related Mapper    : CrmOpportunityMapper
 * Related DB Table  : crm_opportunities
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmOpportunityRepository, CrmOpportunityMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_opportunities'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmOpportunity}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_opportunities'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_opportunities}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_opportunities")
public class CrmOpportunity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "lead_id")
    private Long leadId;

    @Column(nullable = false, length = 150)
    private String title;

    @Column(nullable = false, length = 30)
    private String stage = "NEW";

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount = BigDecimal.ZERO;

    @Column(nullable = false, precision = 5, scale = 2)
    private BigDecimal probability = BigDecimal.ZERO;

    @Column(name = "close_date")
    private LocalDate closeDate;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves lead id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getLeadId() { return leadId; }
    /**
     * Performs the setLeadId operation in this module.
     *
     * @param leadId the leadId input value
     */
    public void setLeadId(Long leadId) { this.leadId = leadId; }
    /**
     * Retrieves title data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTitle() { return title; }
    /**
     * Performs the setTitle operation in this module.
     *
     * @param title the title input value
     */
    public void setTitle(String title) { this.title = title; }
    /**
     * Retrieves stage data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStage() { return stage; }
    /**
     * Performs the setStage operation in this module.
     *
     * @param stage the stage input value
     */
    public void setStage(String stage) { this.stage = stage; }
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
     * Retrieves probability data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getProbability() { return probability; }
    /**
     * Performs the setProbability operation in this module.
     *
     * @param probability the probability input value
     */
    public void setProbability(BigDecimal probability) { this.probability = probability; }
    /**
     * Retrieves close date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getCloseDate() { return closeDate; }
    /**
     * Performs the setCloseDate operation in this module.
     *
     * @param closeDate the closeDate input value
     */
    public void setCloseDate(LocalDate closeDate) { this.closeDate = closeDate; }
    /**
     * Retrieves owner id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOwnerId() { return ownerId; }
    /**
     * Performs the setOwnerId operation in this module.
     *
     * @param ownerId the ownerId input value
     */
    public void setOwnerId(Long ownerId) { this.ownerId = ownerId; }
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
}