/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRouteDeviancyLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteDeviancyLogController
 * Related Service   : PlatformRouteDeviancyLogService, PlatformRouteDeviancyLogServiceImpl
 * Related Repository: PlatformRouteDeviancyLogRepository
 * Related Entity    : PlatformRouteDeviancyLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteDeviancyLogMapper
 * Related DB Table  : platform_route_deviancy_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteDeviancyLogRepository, PlatformRouteDeviancyLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_route_deviancy_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformRouteDeviancyLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_route_deviancy_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_deviancy_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_route_deviancy_log")
public class PlatformRouteDeviancyLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transit_route_id", nullable = false)
    @NotNull
    private Long transitRouteId;

    @Column(name = "expected_route_wkt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String expectedRouteWkt;

    @Column(name = "actual_route_wkt", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String actualRouteWkt;

    @Column(name = "deviation_distance", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal deviationDistance;

    @Column(name = "deviation_duration_minutes", nullable = false)
    @NotNull
    private Integer deviationDurationMinutes;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String severity; // Low, Medium, High

    @Column(name = "automatic_recovery", nullable = false)
    @NotNull
    private Boolean automaticRecovery = false;

    @Column(name = "reroute_triggered", nullable = false)
    @NotNull
    private Boolean rerouteTriggered = false;

    @Column(name = "detected_at", nullable = false)
    @NotNull
    private LocalDateTime detectedAt = LocalDateTime.now();

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
     * Retrieves transit route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTransitRouteId() { return transitRouteId; }
    /**
     * Performs the setTransitRouteId operation in this module.
     *
     * @param transitRouteId the transitRouteId input value
     */
    public void setTransitRouteId(Long transitRouteId) { this.transitRouteId = transitRouteId; }
    /**
     * Retrieves expected route wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getExpectedRouteWkt() { return expectedRouteWkt; }
    /**
     * Performs the setExpectedRouteWkt operation in this module.
     *
     * @param expectedRouteWkt the expectedRouteWkt input value
     */
    public void setExpectedRouteWkt(String expectedRouteWkt) { this.expectedRouteWkt = expectedRouteWkt; }
    /**
     * Retrieves actual route wkt data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getActualRouteWkt() { return actualRouteWkt; }
    /**
     * Performs the setActualRouteWkt operation in this module.
     *
     * @param actualRouteWkt the actualRouteWkt input value
     */
    public void setActualRouteWkt(String actualRouteWkt) { this.actualRouteWkt = actualRouteWkt; }
    /**
     * Retrieves deviation distance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDeviationDistance() { return deviationDistance; }
    /**
     * Performs the setDeviationDistance operation in this module.
     *
     * @param deviationDistance the deviationDistance input value
     */
    public void setDeviationDistance(BigDecimal deviationDistance) { this.deviationDistance = deviationDistance; }
    /**
     * Retrieves deviation duration minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getDeviationDurationMinutes() { return deviationDurationMinutes; }
    /**
     * Performs the setDeviationDurationMinutes operation in this module.
     *
     * @param deviationDurationMinutes the deviationDurationMinutes input value
     */
    public void setDeviationDurationMinutes(Integer deviationDurationMinutes) { this.deviationDurationMinutes = deviationDurationMinutes; }
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
     * Retrieves automatic recovery data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getAutomaticRecovery() { return automaticRecovery; }
    /**
     * Performs the setAutomaticRecovery operation in this module.
     *
     * @param automaticRecovery the automaticRecovery input value
     */
    public void setAutomaticRecovery(Boolean automaticRecovery) { this.automaticRecovery = automaticRecovery; }
    /**
     * Retrieves reroute triggered data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRerouteTriggered() { return rerouteTriggered; }
    /**
     * Performs the setRerouteTriggered operation in this module.
     *
     * @param rerouteTriggered the rerouteTriggered input value
     */
    public void setRerouteTriggered(Boolean rerouteTriggered) { this.rerouteTriggered = rerouteTriggered; }
    /**
     * Retrieves detected at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getDetectedAt() { return detectedAt; }
    /**
     * Performs the setDetectedAt operation in this module.
     *
     * @param detectedAt the detectedAt input value
     */
    public void setDetectedAt(LocalDateTime detectedAt) { this.detectedAt = detectedAt; }
}