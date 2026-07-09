/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmLead.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmLeadController
 * Related Service   : CrmLeadService, CrmLeadServiceImpl
 * Related Repository: CrmLeadRepository
 * Related Entity    : CrmLead
 * Related DTO       : N/A
 * Related Mapper    : CrmLeadMapper
 * Related DB Table  : crm_leads
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmLeadRepository, CrmLeadMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_leads'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmLead}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_leads'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_leads}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_leads")
public class CrmLead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(name = "organization_name", length = 150)
    private String organizationName;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(nullable = false, length = 30)
    private String status = "NEW";

    @Column(nullable = false)
    private int score = 0;

    @Column(length = 50)
    private String source;

    @Column(name = "campaign_attribution", length = 100)
    private String campaignAttribution;

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
     * Retrieves first name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFirstName() { return firstName; }
    /**
     * Performs the setFirstName operation in this module.
     *
     * @param firstName the firstName input value
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }
    /**
     * Retrieves last name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLastName() { return lastName; }
    /**
     * Performs the setLastName operation in this module.
     *
     * @param lastName the lastName input value
     */
    public void setLastName(String lastName) { this.lastName = lastName; }
    /**
     * Retrieves organization name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOrganizationName() { return organizationName; }
    /**
     * Performs the setOrganizationName operation in this module.
     *
     * @param orgName the orgName input value
     */
    public void setOrganizationName(String orgName) { this.organizationName = orgName; }
    /**
     * Retrieves email data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEmail() { return email; }
    /**
     * Performs the setEmail operation in this module.
     *
     * @param email the email input value
     */
    public void setEmail(String email) { this.email = email; }
    /**
     * Retrieves phone data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPhone() { return phone; }
    /**
     * Performs the setPhone operation in this module.
     *
     * @param phone the phone input value
     */
    public void setPhone(String phone) { this.phone = phone; }
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
     * Retrieves score data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getScore() { return score; }
    /**
     * Performs the setScore operation in this module.
     *
     * @param score the score input value
     */
    public void setScore(int score) { this.score = score; }
    /**
     * Retrieves source data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSource() { return source; }
    /**
     * Performs the setSource operation in this module.
     *
     * @param source the source entity or DTO to convert
     */
    public void setSource(String source) { this.source = source; }
    /**
     * Retrieves campaign attribution data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCampaignAttribution() { return campaignAttribution; }
    /**
     * Performs the setCampaignAttribution operation in this module.
     *
     * @param campaignAttribution the campaignAttribution input value
     */
    public void setCampaignAttribution(String campaignAttribution) { this.campaignAttribution = campaignAttribution; }
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