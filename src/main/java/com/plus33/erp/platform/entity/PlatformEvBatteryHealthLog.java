/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEvBatteryHealthLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvBatteryHealthLogController
 * Related Service   : PlatformEvBatteryHealthLogService, PlatformEvBatteryHealthLogServiceImpl
 * Related Repository: PlatformEvBatteryHealthLogRepository
 * Related Entity    : PlatformEvBatteryHealthLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvBatteryHealthLogMapper
 * Related DB Table  : platform_ev_battery_health_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvBatteryHealthLogRepository, PlatformEvBatteryHealthLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ev_battery_health_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEvBatteryHealthLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ev_battery_health_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_battery_health_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ev_battery_health_log")
public class PlatformEvBatteryHealthLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "degradation_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal degradationPercentage;

    @Column(name = "internal_resistance_mohm", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal internalResistanceMohm;

    @Column(name = "cell_voltage_variance", nullable = false, precision = 5, scale = 3)
    @NotNull
    private BigDecimal cellVoltageVariance;

    @Column(name = "thermal_balance_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal thermalBalanceScore;

    @Column(name = "estimated_remaining_cycles", nullable = false)
    @NotNull
    private Integer estimatedRemainingCycles;

    @Column(name = "estimated_end_of_life", nullable = false)
    @NotNull
    private LocalDateTime estimatedEndOfLife;

    @Column(name = "health_score", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal healthScore;

    @Column(name = "diagnostic_version", nullable = false)
    @NotNull
    @Size(max = 50)
    private String diagnosticVersion;

    @Column(name = "prediction_confidence", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal predictionConfidence;

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
     * Retrieves vehicle id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getVehicleId() { return vehicleId; }
    /**
     * Performs the setVehicleId operation in this module.
     *
     * @param vehicleId the vehicleId input value
     */
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    /**
     * Retrieves degradation percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDegradationPercentage() { return degradationPercentage; }
    /**
     * Performs the setDegradationPercentage operation in this module.
     *
     * @param degradationPercentage the degradationPercentage input value
     */
    public void setDegradationPercentage(BigDecimal degradationPercentage) { this.degradationPercentage = degradationPercentage; }
    /**
     * Retrieves internal resistance mohm data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInternalResistanceMohm() { return internalResistanceMohm; }
    /**
     * Performs the setInternalResistanceMohm operation in this module.
     *
     * @param internalResistanceMohm the internalResistanceMohm input value
     */
    public void setInternalResistanceMohm(BigDecimal internalResistanceMohm) { this.internalResistanceMohm = internalResistanceMohm; }
    /**
     * Retrieves cell voltage variance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCellVoltageVariance() { return cellVoltageVariance; }
    /**
     * Performs the setCellVoltageVariance operation in this module.
     *
     * @param cellVoltageVariance the cellVoltageVariance input value
     */
    public void setCellVoltageVariance(BigDecimal cellVoltageVariance) { this.cellVoltageVariance = cellVoltageVariance; }
    /**
     * Retrieves thermal balance score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThermalBalanceScore() { return thermalBalanceScore; }
    /**
     * Performs the setThermalBalanceScore operation in this module.
     *
     * @param thermalBalanceScore the thermalBalanceScore input value
     */
    public void setThermalBalanceScore(BigDecimal thermalBalanceScore) { this.thermalBalanceScore = thermalBalanceScore; }
    /**
     * Retrieves estimated remaining cycles data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getEstimatedRemainingCycles() { return estimatedRemainingCycles; }
    /**
     * Performs the setEstimatedRemainingCycles operation in this module.
     *
     * @param estimatedRemainingCycles the estimatedRemainingCycles input value
     */
    public void setEstimatedRemainingCycles(Integer estimatedRemainingCycles) { this.estimatedRemainingCycles = estimatedRemainingCycles; }
    /**
     * Retrieves estimated end of life data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEstimatedEndOfLife() { return estimatedEndOfLife; }
    /**
     * Performs the setEstimatedEndOfLife operation in this module.
     *
     * @param estimatedEndOfLife the estimatedEndOfLife input value
     */
    public void setEstimatedEndOfLife(LocalDateTime estimatedEndOfLife) { this.estimatedEndOfLife = estimatedEndOfLife; }
    /**
     * Retrieves health score data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHealthScore() { return healthScore; }
    /**
     * Performs the setHealthScore operation in this module.
     *
     * @param healthScore the healthScore input value
     */
    public void setHealthScore(BigDecimal healthScore) { this.healthScore = healthScore; }
    /**
     * Retrieves diagnostic version data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDiagnosticVersion() { return diagnosticVersion; }
    /**
     * Performs the setDiagnosticVersion operation in this module.
     *
     * @param diagnosticVersion the diagnosticVersion input value
     */
    public void setDiagnosticVersion(String diagnosticVersion) { this.diagnosticVersion = diagnosticVersion; }
    /**
     * Retrieves prediction confidence data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPredictionConfidence() { return predictionConfidence; }
    /**
     * Performs the setPredictionConfidence operation in this module.
     *
     * @param predictionConfidence the predictionConfidence input value
     */
    public void setPredictionConfidence(BigDecimal predictionConfidence) { this.predictionConfidence = predictionConfidence; }
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