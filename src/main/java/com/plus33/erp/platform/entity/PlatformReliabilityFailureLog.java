/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformReliabilityFailureLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformReliabilityFailureLogController
 * Related Service   : PlatformReliabilityFailureLogService, PlatformReliabilityFailureLogServiceImpl
 * Related Repository: PlatformReliabilityFailureLogRepository
 * Related Entity    : PlatformReliabilityFailureLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformReliabilityFailureLogMapper
 * Related DB Table  : platform_reliability_failure_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformReliabilityFailureLogRepository, PlatformReliabilityFailureLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_reliability_failure_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformReliabilityFailureLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_reliability_failure_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_reliability_failure_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_reliability_failure_log")
public class PlatformReliabilityFailureLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "asset_id", nullable = false)
    @NotNull
    private Long assetId;

    @Column(name = "mtbf_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal mtbfHours;

    @Column(name = "mttr_hours", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal mttrHours;

    @Column(name = "availability_rate", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal availabilityRate;

    @Column(name = "failure_rate", nullable = false, precision = 7, scale = 5)
    @NotNull
    private BigDecimal failureRate;

    @Column(name = "reliability_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal reliabilityScore;

    @Column(name = "repair_duration_minutes", nullable = false)
    @NotNull
    private Integer repairDurationMinutes;

    @Column(name = "downtime_duration_minutes", nullable = false)
    @NotNull
    private Integer downtimeDurationMinutes;

    @Column(name = "root_cause_category", nullable = false)
    @NotNull
    @Size(max = 200)
    private String rootCauseCategory;

    @Column(name = "failure_mode", nullable = false)
    @NotNull
    @Size(max = 200)
    private String failureMode;

    @Column(name = "corrective_action")
    @Size(max = 500)
    private String correctiveAction;

    @Column(name = "reported_at", nullable = false)
    @NotNull
    private LocalDateTime reportedAt = LocalDateTime.now();

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
     * Retrieves asset id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssetId() { return assetId; }
    /**
     * Performs the setAssetId operation in this module.
     *
     * @param assetId the assetId input value
     */
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    /**
     * Retrieves mtbf hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMtbfHours() { return mtbfHours; }
    /**
     * Performs the setMtbfHours operation in this module.
     *
     * @param mtbfHours the mtbfHours input value
     */
    public void setMtbfHours(BigDecimal mtbfHours) { this.mtbfHours = mtbfHours; }
    /**
     * Retrieves mttr hours data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMttrHours() { return mttrHours; }
    /**
     * Performs the setMttrHours operation in this module.
     *
     * @param mttrHours the mttrHours input value
     */
    public void setMttrHours(BigDecimal mttrHours) { this.mttrHours = mttrHours; }
    /**
     * Retrieves availability rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAvailabilityRate() { return availabilityRate; }
    /**
     * Performs the setAvailabilityRate operation in this module.
     *
     * @param availabilityRate the availabilityRate input value
     */
    public void setAvailabilityRate(BigDecimal availabilityRate) { this.availabilityRate = availabilityRate; }
    /**
     * Retrieves failure rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFailureRate() { return failureRate; }
    /**
     * Performs the setFailureRate operation in this module.
     *
     * @param failureRate the failureRate input value
     */
    public void setFailureRate(BigDecimal failureRate) { this.failureRate = failureRate; }
    /**
     * Retrieves reliability score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReliabilityScore() { return reliabilityScore; }
    /**
     * Performs the setReliabilityScore operation in this module.
     *
     * @param reliabilityScore the reliabilityScore input value
     */
    public void setReliabilityScore(BigDecimal reliabilityScore) { this.reliabilityScore = reliabilityScore; }
    /**
     * Retrieves repair duration minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRepairDurationMinutes() { return repairDurationMinutes; }
    /**
     * Performs the setRepairDurationMinutes operation in this module.
     *
     * @param repairDurationMinutes the repairDurationMinutes input value
     */
    public void setRepairDurationMinutes(Integer repairDurationMinutes) { this.repairDurationMinutes = repairDurationMinutes; }
    /**
     * Retrieves downtime duration minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDowntimeDurationMinutes() { return downtimeDurationMinutes; }
    /**
     * Performs the setDowntimeDurationMinutes operation in this module.
     *
     * @param downtimeDurationMinutes the downtimeDurationMinutes input value
     */
    public void setDowntimeDurationMinutes(Integer downtimeDurationMinutes) { this.downtimeDurationMinutes = downtimeDurationMinutes; }
    /**
     * Retrieves root cause category data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRootCauseCategory() { return rootCauseCategory; }
    /**
     * Performs the setRootCauseCategory operation in this module.
     *
     * @param rootCauseCategory the rootCauseCategory input value
     */
    public void setRootCauseCategory(String rootCauseCategory) { this.rootCauseCategory = rootCauseCategory; }
    /**
     * Retrieves failure mode data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFailureMode() { return failureMode; }
    /**
     * Performs the setFailureMode operation in this module.
     *
     * @param failureMode the failureMode input value
     */
    public void setFailureMode(String failureMode) { this.failureMode = failureMode; }
    /**
     * Retrieves corrective action data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCorrectiveAction() { return correctiveAction; }
    /**
     * Performs the setCorrectiveAction operation in this module.
     *
     * @param correctiveAction the correctiveAction input value
     */
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    /**
     * Retrieves reported at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReportedAt() { return reportedAt; }
    /**
     * Performs the setReportedAt operation in this module.
     *
     * @param reportedAt the reportedAt input value
     */
    public void setReportedAt(LocalDateTime reportedAt) { this.reportedAt = reportedAt; }
}