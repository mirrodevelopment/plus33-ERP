/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformProcessEventLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformProcessEventLogController
 * Related Service   : PlatformProcessEventLogService, PlatformProcessEventLogServiceImpl
 * Related Repository: PlatformProcessEventLogRepository
 * Related Entity    : PlatformProcessEventLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformProcessEventLogMapper
 * Related DB Table  : platform_process_event_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformProcessEventLogRepository, PlatformProcessEventLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_process_event_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformProcessEventLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_process_event_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_process_event_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_process_event_log")
public class PlatformProcessEventLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "case_id", nullable = false)
    @NotNull
    private Long caseId;

    @Column(name = "activity_name", nullable = false)
    @NotNull
    @Size(max = 150)
    private String activityName;

    @Column(name = "transition_state", nullable = false)
    @NotNull
    @Size(max = 50)
    private String transitionState;

    @Column(name = "duration_ms", nullable = false)
    @NotNull
    private Long durationMs;

    @Column(name = "recorded_at", nullable = false)
    @NotNull
    private LocalDateTime recordedAt = LocalDateTime.now();

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
     * Retrieves case id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCaseId() { return caseId; }
    /**
     * Performs the setCaseId operation in this module.
     *
     * @param caseId the caseId input value
     */
    public void setCaseId(Long caseId) { this.caseId = caseId; }
    /**
     * Retrieves activity name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActivityName() { return activityName; }
    /**
     * Performs the setActivityName operation in this module.
     *
     * @param activityName the activityName input value
     */
    public void setActivityName(String activityName) { this.activityName = activityName; }
    /**
     * Retrieves transition state data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTransitionState() { return transitionState; }
    /**
     * Performs the setTransitionState operation in this module.
     *
     * @param transitionState the transitionState input value
     */
    public void setTransitionState(String transitionState) { this.transitionState = transitionState; }
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
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
}