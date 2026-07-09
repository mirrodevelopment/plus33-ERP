/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmCase.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmCaseController
 * Related Service   : CrmCaseService, CrmCaseServiceImpl
 * Related Repository: CrmCaseRepository
 * Related Entity    : CrmCase
 * Related DTO       : N/A
 * Related Mapper    : CrmCaseMapper
 * Related DB Table  : crm_cases
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmCaseRepository, CrmCaseMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_cases'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmCase}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_cases'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_cases}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_cases")
public class CrmCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "case_number", nullable = false, unique = true, length = 50)
    private String caseNumber;

    @Column(nullable = false, length = 20)
    private String priority = "MEDIUM";

    @Column(nullable = false, length = 30)
    private String status = "NEW";

    @Column(length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "sla_breached", nullable = false)
    private boolean slaBreached = false;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

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
     * Retrieves case number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCaseNumber() { return caseNumber; }
    /**
     * Performs the setCaseNumber operation in this module.
     *
     * @param caseNumber the caseNumber input value
     */
    public void setCaseNumber(String caseNumber) { this.caseNumber = caseNumber; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(String priority) { this.priority = priority; }
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
     * Retrieves category data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCategory() { return category; }
    /**
     * Performs the setCategory operation in this module.
     *
     * @param category the category input value
     */
    public void setCategory(String category) { this.category = category; }
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
     * Performs the isSlaBreached operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isSlaBreached() { return slaBreached; }
    /**
     * Performs the setSlaBreached operation in this module.
     *
     * @param breached the breached input value
     */
    public void setSlaBreached(boolean breached) { this.slaBreached = breached; }
    /**
     * Retrieves resolved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getResolvedAt() { return resolvedAt; }
    /**
     * Performs the setResolvedAt operation in this module.
     *
     * @param resolvedAt the resolvedAt input value
     */
    public void setResolvedAt(LocalDateTime resolvedAt) { this.resolvedAt = resolvedAt; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}