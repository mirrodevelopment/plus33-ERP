/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEcoDrivingLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEcoDrivingLogController
 * Related Service   : PlatformEcoDrivingLogService, PlatformEcoDrivingLogServiceImpl
 * Related Repository: PlatformEcoDrivingLogRepository
 * Related Entity    : PlatformEcoDrivingLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEcoDrivingLogMapper
 * Related DB Table  : platform_eco_driving_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEcoDrivingLogRepository, PlatformEcoDrivingLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_eco_driving_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEcoDrivingLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_eco_driving_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_eco_driving_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_eco_driving_log")
public class PlatformEcoDrivingLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "driver_id", nullable = false)
    @NotNull
    private Long driverId;

    @Column(name = "trip_id", nullable = false)
    @NotNull
    private Long tripId;

    @Column(name = "harsh_acceleration_count", nullable = false)
    @NotNull
    private Integer harshAccelerationCount;

    @Column(name = "harsh_braking_count", nullable = false)
    @NotNull
    private Integer harshBrakingCount;

    @Column(name = "harsh_cornering_count", nullable = false)
    @NotNull
    private Integer harshCorneringCount;

    @Column(name = "excessive_idle_seconds", nullable = false)
    @NotNull
    private Integer excessiveIdleSeconds;

    @Column(name = "overspeed_duration_secs", nullable = false)
    @NotNull
    private Integer overspeedDurationSecs;

    @Column(name = "cruise_control_usage_pct", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal cruiseControlUsagePct;

    @Column(name = "driver_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal driverScore;

    @Column(name = "trip_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal tripScore;

    @Column(name = "coaching_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String coachingStatus; // OK, NEEDS_COACHING, COACHED

    @Column(name = "logged_at", nullable = false)
    @NotNull
    private LocalDateTime loggedAt = LocalDateTime.now();

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
     * Retrieves driver id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDriverId() { return driverId; }
    /**
     * Performs the setDriverId operation in this module.
     *
     * @param driverId the driverId input value
     */
    public void setDriverId(Long driverId) { this.driverId = driverId; }
    /**
     * Retrieves trip id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTripId() { return tripId; }
    /**
     * Performs the setTripId operation in this module.
     *
     * @param tripId the tripId input value
     */
    public void setTripId(Long tripId) { this.tripId = tripId; }
    /**
     * Retrieves harsh acceleration count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getHarshAccelerationCount() { return harshAccelerationCount; }
    /**
     * Performs the setHarshAccelerationCount operation in this module.
     *
     * @param harshAccelerationCount the harshAccelerationCount input value
     */
    public void setHarshAccelerationCount(Integer harshAccelerationCount) { this.harshAccelerationCount = harshAccelerationCount; }
    /**
     * Retrieves harsh braking count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getHarshBrakingCount() { return harshBrakingCount; }
    /**
     * Performs the setHarshBrakingCount operation in this module.
     *
     * @param harshBrakingCount the harshBrakingCount input value
     */
    public void setHarshBrakingCount(Integer harshBrakingCount) { this.harshBrakingCount = harshBrakingCount; }
    /**
     * Retrieves harsh cornering count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getHarshCorneringCount() { return harshCorneringCount; }
    /**
     * Performs the setHarshCorneringCount operation in this module.
     *
     * @param harshCorneringCount the harshCorneringCount input value
     */
    public void setHarshCorneringCount(Integer harshCorneringCount) { this.harshCorneringCount = harshCorneringCount; }
    /**
     * Retrieves excessive idle seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getExcessiveIdleSeconds() { return excessiveIdleSeconds; }
    /**
     * Performs the setExcessiveIdleSeconds operation in this module.
     *
     * @param excessiveIdleSeconds the excessiveIdleSeconds input value
     */
    public void setExcessiveIdleSeconds(Integer excessiveIdleSeconds) { this.excessiveIdleSeconds = excessiveIdleSeconds; }
    /**
     * Retrieves overspeed duration secs data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getOverspeedDurationSecs() { return overspeedDurationSecs; }
    /**
     * Performs the setOverspeedDurationSecs operation in this module.
     *
     * @param overspeedDurationSecs the overspeedDurationSecs input value
     */
    public void setOverspeedDurationSecs(Integer overspeedDurationSecs) { this.overspeedDurationSecs = overspeedDurationSecs; }
    /**
     * Retrieves cruise control usage pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCruiseControlUsagePct() { return cruiseControlUsagePct; }
    /**
     * Performs the setCruiseControlUsagePct operation in this module.
     *
     * @param cruiseControlUsagePct the cruiseControlUsagePct input value
     */
    public void setCruiseControlUsagePct(BigDecimal cruiseControlUsagePct) { this.cruiseControlUsagePct = cruiseControlUsagePct; }
    /**
     * Retrieves driver score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDriverScore() { return driverScore; }
    /**
     * Performs the setDriverScore operation in this module.
     *
     * @param driverScore the driverScore input value
     */
    public void setDriverScore(BigDecimal driverScore) { this.driverScore = driverScore; }
    /**
     * Retrieves trip score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTripScore() { return tripScore; }
    /**
     * Performs the setTripScore operation in this module.
     *
     * @param tripScore the tripScore input value
     */
    public void setTripScore(BigDecimal tripScore) { this.tripScore = tripScore; }
    /**
     * Retrieves coaching status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCoachingStatus() { return coachingStatus; }
    /**
     * Performs the setCoachingStatus operation in this module.
     *
     * @param coachingStatus the coachingStatus input value
     */
    public void setCoachingStatus(String coachingStatus) { this.coachingStatus = coachingStatus; }
    /**
     * Retrieves logged at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoggedAt() { return loggedAt; }
    /**
     * Performs the setLoggedAt operation in this module.
     *
     * @param loggedAt the loggedAt input value
     */
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}