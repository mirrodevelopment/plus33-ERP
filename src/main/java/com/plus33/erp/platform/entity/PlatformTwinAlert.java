/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTwinAlert.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTwinAlertController
 * Related Service   : PlatformTwinAlertService, PlatformTwinAlertServiceImpl
 * Related Repository: PlatformTwinAlertRepository
 * Related Entity    : PlatformTwinAlert
 * Related DTO       : N/A
 * Related Mapper    : PlatformTwinAlertMapper
 * Related DB Table  : platform_twin_alert
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTwinAlertRepository, PlatformTwinAlertMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_twin_alert'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTwinAlert}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_twin_alert'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_twin_alert}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_twin_alert")
public class PlatformTwinAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "instance_id", nullable = false)
    @NotNull
    private Long instanceId;

    @Column(name = "alert_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String alertType;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity = "WARNING";

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(name = "triggered_at", nullable = false)
    @NotNull
    private LocalDateTime triggeredAt = LocalDateTime.now();

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
     * Retrieves instance id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstanceId() { return instanceId; }
    /**
     * Performs the setInstanceId operation in this module.
     *
     * @param instanceId the instanceId input value
     */
    public void setInstanceId(Long instanceId) { this.instanceId = instanceId; }
    /**
     * Retrieves alert type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlertType() { return alertType; }
    /**
     * Performs the setAlertType operation in this module.
     *
     * @param alertType the alertType input value
     */
    public void setAlertType(String alertType) { this.alertType = alertType; }
    /**
     * Retrieves severity data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSeverity() { return severity; }
    /**
     * Performs the setSeverity operation in this module.
     *
     * @param severity the severity input value
     */
    public void setSeverity(String severity) { this.severity = severity; }
    /**
     * Retrieves message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getMessage() { return message; }
    /**
     * Performs the setMessage operation in this module.
     *
     * @param message the message input value
     */
    public void setMessage(String message) { this.message = message; }
    /**
     * Retrieves triggered at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTriggeredAt() { return triggeredAt; }
    /**
     * Performs the setTriggeredAt operation in this module.
     *
     * @param triggeredAt the triggeredAt input value
     */
    public void setTriggeredAt(LocalDateTime triggeredAt) { this.triggeredAt = triggeredAt; }
}