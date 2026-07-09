/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmQuote.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteController
 * Related Service   : CrmQuoteService, CrmQuoteServiceImpl
 * Related Repository: CrmQuoteRepository
 * Related Entity    : CrmQuote
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteMapper
 * Related DB Table  : crm_quotes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteRepository, CrmQuoteMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_quotes'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuote}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_quotes'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quotes}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_quotes")
public class CrmQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "quote_number", nullable = false, unique = true, length = 50)
    private String quoteNumber;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "opportunity_id")
    private Long opportunityId;

    @Column(name = "active_version_number", nullable = false)
    private int activeVersionNumber = 1;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

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
     * Retrieves quote number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getQuoteNumber() { return quoteNumber; }
    /**
     * Performs the setQuoteNumber operation in this module.
     *
     * @param quoteNumber the quoteNumber input value
     */
    public void setQuoteNumber(String quoteNumber) { this.quoteNumber = quoteNumber; }
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
     * Retrieves opportunity id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOpportunityId() { return opportunityId; }
    /**
     * Performs the setOpportunityId operation in this module.
     *
     * @param opportunityId the opportunityId input value
     */
    public void setOpportunityId(Long opportunityId) { this.opportunityId = opportunityId; }
    /**
     * Retrieves active version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getActiveVersionNumber() { return activeVersionNumber; }
    /**
     * Performs the setActiveVersionNumber operation in this module.
     *
     * @param activeVersionNumber the activeVersionNumber input value
     */
    public void setActiveVersionNumber(int activeVersionNumber) { this.activeVersionNumber = activeVersionNumber; }
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