/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmCommission.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmCommissionController
 * Related Service   : CrmCommissionService, CrmCommissionServiceImpl
 * Related Repository: CrmCommissionRepository
 * Related Entity    : CrmCommission
 * Related DTO       : N/A
 * Related Mapper    : CrmCommissionMapper
 * Related DB Table  : crm_commissions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmCommissionRepository, CrmCommissionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_commissions'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmCommission}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_commissions'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_commissions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_commissions")
public class CrmCommission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "sales_rep_id", nullable = false)
    private Long salesRepId;

    @Column(name = "opportunity_id", nullable = false)
    private Long opportunityId;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String status = "CALCULATED";

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
     * Retrieves sales rep id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSalesRepId() { return salesRepId; }
    /**
     * Performs the setSalesRepId operation in this module.
     *
     * @param repId the repId input value
     */
    public void setSalesRepId(Long repId) { this.salesRepId = repId; }
    /**
     * Retrieves opportunity id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOpportunityId() { return opportunityId; }
    /**
     * Performs the setOpportunityId operation in this module.
     *
     * @param oppId the oppId input value
     */
    public void setOpportunityId(Long oppId) { this.opportunityId = oppId; }
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