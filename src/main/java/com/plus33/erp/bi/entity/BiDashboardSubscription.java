/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiDashboardSubscription.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDashboardSubscriptionController
 * Related Service   : BiDashboardSubscriptionService, BiDashboardSubscriptionServiceImpl
 * Related Repository: BiDashboardSubscriptionRepository
 * Related Entity    : BiDashboardSubscription
 * Related DTO       : N/A
 * Related Mapper    : BiDashboardSubscriptionMapper
 * Related DB Table  : bi_dashboard_subscription
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDashboardSubscriptionRepository, BiDashboardSubscriptionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_dashboard_subscription'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDashboardSubscription}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_dashboard_subscription'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_dashboard_subscription}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_dashboard_subscription")
public class BiDashboardSubscription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "subscriber_user", nullable = false)
    private String subscriberUser;
    @Column(name = "schedule_cron", nullable = false)
    private String scheduleCron;
    @Column(name = "export_format", nullable = false)
    private String exportFormat = "PDF";
    @Column(nullable = false)
    private String status = "ACTIVE";
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "last_triggered_at")
    private LocalDateTime lastTriggeredAt;

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
     * Retrieves subscriber user data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSubscriberUser() { return subscriberUser; }
    /**
     * Performs the setSubscriberUser operation in this module.
     *
     * @param subscriberUser the subscriberUser input value
     */
    public void setSubscriberUser(String subscriberUser) { this.subscriberUser = subscriberUser; }
    /**
     * Retrieves schedule cron data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScheduleCron() { return scheduleCron; }
    /**
     * Performs the setScheduleCron operation in this module.
     *
     * @param scheduleCron the scheduleCron input value
     */
    public void setScheduleCron(String scheduleCron) { this.scheduleCron = scheduleCron; }
    /**
     * Retrieves export format data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExportFormat() { return exportFormat; }
    /**
     * Performs the setExportFormat operation in this module.
     *
     * @param exportFormat the exportFormat input value
     */
    public void setExportFormat(String exportFormat) { this.exportFormat = exportFormat; }
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
     * Performs the setCreatedAt operation in this module.
     *
     * @param createdAt the createdAt input value
     */
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    /**
     * Retrieves last triggered at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastTriggeredAt() { return lastTriggeredAt; }
    /**
     * Performs the setLastTriggeredAt operation in this module.
     *
     * @param lastTriggeredAt the lastTriggeredAt input value
     */
    public void setLastTriggeredAt(LocalDateTime lastTriggeredAt) { this.lastTriggeredAt = lastTriggeredAt; }
}