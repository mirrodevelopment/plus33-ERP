/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiDashboardShare.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDashboardShareController
 * Related Service   : BiDashboardShareService, BiDashboardShareServiceImpl
 * Related Repository: BiDashboardShareRepository
 * Related Entity    : BiDashboardShare
 * Related DTO       : N/A
 * Related Mapper    : BiDashboardShareMapper
 * Related DB Table  : bi_dashboard_share
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDashboardShareRepository, BiDashboardShareMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_dashboard_share'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDashboardShare}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_dashboard_share'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_dashboard_share}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_dashboard_share")
public class BiDashboardShare {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "shared_by", nullable = false)
    private String sharedBy;
    @Column(name = "recipient_user", nullable = false)
    private String recipientUser;
    @Column(name = "can_edit", nullable = false)
    private Boolean canEdit = false;
    @Column(name = "shared_at", nullable = false)
    private LocalDateTime sharedAt = LocalDateTime.now();

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
     * Retrieves dashboard code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDashboardCode() { return dashboardCode; }
    /**
     * Performs the setDashboardCode operation in this module.
     *
     * @param dashboardCode the dashboardCode input value
     */
    public void setDashboardCode(String dashboardCode) { this.dashboardCode = dashboardCode; }
    /**
     * Retrieves shared by data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSharedBy() { return sharedBy; }
    /**
     * Performs the setSharedBy operation in this module.
     *
     * @param sharedBy the sharedBy input value
     */
    public void setSharedBy(String sharedBy) { this.sharedBy = sharedBy; }
    /**
     * Retrieves recipient user data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecipientUser() { return recipientUser; }
    /**
     * Performs the setRecipientUser operation in this module.
     *
     * @param recipientUser the recipientUser input value
     */
    public void setRecipientUser(String recipientUser) { this.recipientUser = recipientUser; }
    /**
     * Retrieves can edit data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getCanEdit() { return canEdit; }
    /**
     * Performs the setCanEdit operation in this module.
     *
     * @param canEdit the canEdit input value
     */
    public void setCanEdit(Boolean canEdit) { this.canEdit = canEdit; }
    /**
     * Retrieves shared at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getSharedAt() { return sharedAt; }
    /**
     * Performs the setSharedAt operation in this module.
     *
     * @param sharedAt the sharedAt input value
     */
    public void setSharedAt(LocalDateTime sharedAt) { this.sharedAt = sharedAt; }
}