/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.entity
 * File              : BiUsageLog.java
 * Purpose           : JPA Entity representing a persistent database record in Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiUsageLogController
 * Related Service   : BiUsageLogService, BiUsageLogServiceImpl
 * Related Repository: BiUsageLogRepository
 * Related Entity    : BiUsageLog
 * Related DTO       : N/A
 * Related Mapper    : BiUsageLogMapper
 * Related DB Table  : bi_usage_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiUsageLogRepository, BiUsageLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bi_usage_log'. Defines persistent domain object for Bi Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.bi.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiUsageLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bi_usage_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_usage_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bi_usage_log")
public class BiUsageLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "dashboard_code", nullable = false)
    private String dashboardCode;
    @Column(name = "action_type", nullable = false)
    private String actionType;
    @Column(name = "duration_ms", nullable = false)
    private Long durationMs;
    @Column(name = "company_id", nullable = false)
    private Long companyId;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "accessed_at", nullable = false, updatable = false)
    private LocalDateTime accessedAt = LocalDateTime.now();

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
     * Retrieves user id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getUserId() { return userId; }
    /**
     * Performs the setUserId operation in this module.
     *
     * @param userId authenticated user identifier
     */
    public void setUserId(String userId) { this.userId = userId; }
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
     * Retrieves action type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActionType() { return actionType; }
    /**
     * Performs the setActionType operation in this module.
     *
     * @param actionType the actionType input value
     */
    public void setActionType(String actionType) { this.actionType = actionType; }
    /**
     * Retrieves duration ms data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDurationMs() { return durationMs; }
    /**
     * Performs the setDurationMs operation in this module.
     *
     * @param durationMs the durationMs input value
     */
    public void setDurationMs(Long durationMs) { this.durationMs = durationMs; }
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
     * Retrieves ip address data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIpAddress() { return ipAddress; }
    /**
     * Performs the setIpAddress operation in this module.
     *
     * @param ipAddress the ipAddress input value
     */
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
    /**
     * Retrieves accessed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAccessedAt() { return accessedAt; }
    /**
     * Performs the setAccessedAt operation in this module.
     *
     * @param accessedAt the accessedAt input value
     */
    public void setAccessedAt(LocalDateTime accessedAt) { this.accessedAt = accessedAt; }
}