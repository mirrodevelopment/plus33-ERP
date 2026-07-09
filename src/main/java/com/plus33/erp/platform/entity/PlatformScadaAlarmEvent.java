/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformScadaAlarmEvent.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformScadaAlarmEventController
 * Related Service   : PlatformScadaAlarmEventService, PlatformScadaAlarmEventServiceImpl
 * Related Repository: PlatformScadaAlarmEventRepository
 * Related Entity    : PlatformScadaAlarmEvent
 * Related DTO       : N/A
 * Related Mapper    : PlatformScadaAlarmEventMapper
 * Related DB Table  : platform_scada_alarm_event
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformScadaAlarmEventRepository, PlatformScadaAlarmEventMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_scada_alarm_event'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformScadaAlarmEvent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_scada_alarm_event'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_scada_alarm_event}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_scada_alarm_event")
public class PlatformScadaAlarmEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false)
    @NotNull
    private Long deviceId;

    @Column(name = "policy_id", nullable = false)
    @NotNull
    private Long policyId;

    @Column(name = "alarm_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String alarmStatus = "ACTIVE"; // Acknowledged, Shelved, Suppressed, Returned To Normal

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String message;

    @Column(name = "triggered_at", nullable = false)
    @NotNull
    private LocalDateTime triggeredAt = LocalDateTime.now();

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

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
     * Retrieves device id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDeviceId() { return deviceId; }
    /**
     * Performs the setDeviceId operation in this module.
     *
     * @param deviceId the deviceId input value
     */
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    /**
     * Retrieves policy id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPolicyId() { return policyId; }
    /**
     * Performs the setPolicyId operation in this module.
     *
     * @param policyId the policyId input value
     */
    public void setPolicyId(Long policyId) { this.policyId = policyId; }
    /**
     * Retrieves alarm status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAlarmStatus() { return alarmStatus; }
    /**
     * Performs the setAlarmStatus operation in this module.
     *
     * @param alarmStatus the alarmStatus input value
     */
    public void setAlarmStatus(String alarmStatus) { this.alarmStatus = alarmStatus; }
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
}