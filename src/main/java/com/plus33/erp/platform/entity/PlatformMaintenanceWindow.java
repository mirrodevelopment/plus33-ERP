/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformMaintenanceWindow.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformMaintenanceWindowController
 * Related Service   : PlatformMaintenanceWindowService, PlatformMaintenanceWindowServiceImpl
 * Related Repository: PlatformMaintenanceWindowRepository
 * Related Entity    : PlatformMaintenanceWindow
 * Related DTO       : N/A
 * Related Mapper    : PlatformMaintenanceWindowMapper
 * Related DB Table  : platform_maintenance_window
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformMaintenanceWindowRepository, PlatformMaintenanceWindowMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_maintenance_window'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformMaintenanceWindow}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_maintenance_window'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_maintenance_window}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_maintenance_window")
public class PlatformMaintenanceWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "start_time", nullable = false)
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull
    private LocalDateTime endTime;

    @Column(name = "affected_services", nullable = false)
    @NotNull
    @Size(max = 500)
    private String affectedServices;

    @Column(name = "notification_msg", nullable = false)
    @NotNull
    @Size(max = 500)
    private String notificationMsg;

    @Column(name = "allowed_users", nullable = false)
    @NotNull
    @Size(max = 500)
    private String allowedUsers;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

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
     * Retrieves version data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersion() { return version; }
    /**
     * Performs the setVersion operation in this module.
     *
     * @param version the version input value
     */
    public void setVersion(Integer version) { this.version = version; }
    /**
     * Retrieves start time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartTime() { return startTime; }
    /**
     * Performs the setStartTime operation in this module.
     *
     * @param startTime the startTime input value
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /**
     * Retrieves end time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEndTime() { return endTime; }
    /**
     * Performs the setEndTime operation in this module.
     *
     * @param endTime the endTime input value
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    /**
     * Retrieves affected services data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAffectedServices() { return affectedServices; }
    /**
     * Performs the setAffectedServices operation in this module.
     *
     * @param affectedServices the affectedServices input value
     */
    public void setAffectedServices(String affectedServices) { this.affectedServices = affectedServices; }
    /**
     * Retrieves notification msg data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotificationMsg() { return notificationMsg; }
    /**
     * Performs the setNotificationMsg operation in this module.
     *
     * @param notificationMsg the notificationMsg input value
     */
    public void setNotificationMsg(String notificationMsg) { this.notificationMsg = notificationMsg; }
    /**
     * Retrieves a paginated list of allowed users records.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAllowedUsers() { return allowedUsers; }
    /**
     * Performs the setAllowedUsers operation in this module.
     *
     * @param allowedUsers the allowedUsers input value
     */
    public void setAllowedUsers(String allowedUsers) { this.allowedUsers = allowedUsers; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
}