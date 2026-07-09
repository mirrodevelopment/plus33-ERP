/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformAnomalyAlert.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAnomalyAlertController
 * Related Service   : PlatformAnomalyAlertService, PlatformAnomalyAlertServiceImpl
 * Related Repository: PlatformAnomalyAlertRepository
 * Related Entity    : PlatformAnomalyAlert
 * Related DTO       : N/A
 * Related Mapper    : PlatformAnomalyAlertMapper
 * Related DB Table  : platform_anomaly_alert
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAnomalyAlertRepository, PlatformAnomalyAlertMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_anomaly_alert'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAnomalyAlert}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_anomaly_alert'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_anomaly_alert}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_anomaly_alert")
public class PlatformAnomalyAlert {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "alert_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String alertName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity = "WARNING";

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

    @Column(name = "trigger_message", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String triggerMessage;

    @Column(nullable = false)
    @NotNull
    private LocalDateTime timestamp = LocalDateTime.now();

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
     * Retrieves alert name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlertName() { return alertName; }
    /**
     * Performs the setAlertName operation in this module.
     *
     * @param alertName the alertName input value
     */
    public void setAlertName(String alertName) { this.alertName = alertName; }
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
     * Retrieves trigger message data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTriggerMessage() { return triggerMessage; }
    /**
     * Performs the setTriggerMessage operation in this module.
     *
     * @param triggerMessage the triggerMessage input value
     */
    public void setTriggerMessage(String triggerMessage) { this.triggerMessage = triggerMessage; }
    /**
     * Retrieves timestamp data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getTimestamp() { return timestamp; }
    /**
     * Performs the setTimestamp operation in this module.
     *
     * @param timestamp the timestamp input value
     */
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
}