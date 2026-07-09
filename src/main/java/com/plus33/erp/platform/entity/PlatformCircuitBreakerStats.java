/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformCircuitBreakerStats.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformCircuitBreakerStatsController
 * Related Service   : PlatformCircuitBreakerStatsService, PlatformCircuitBreakerStatsServiceImpl
 * Related Repository: PlatformCircuitBreakerStatsRepository
 * Related Entity    : PlatformCircuitBreakerStats
 * Related DTO       : N/A
 * Related Mapper    : PlatformCircuitBreakerStatsMapper
 * Related DB Table  : platform_circuit_breaker_stats
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformCircuitBreakerStatsRepository, PlatformCircuitBreakerStatsMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_circuit_breaker_stats'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformCircuitBreakerStats}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_circuit_breaker_stats'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_circuit_breaker_stats}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_circuit_breaker_stats")
public class PlatformCircuitBreakerStats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "breaker_name", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String breakerName;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "CLOSED";

    @Column(name = "failures_count", nullable = false)
    @NotNull
    private Integer failuresCount = 0;

    @Column(name = "success_ratio", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal successRatio = BigDecimal.valueOf(100.00);

    @Column(name = "recovery_time_sec", nullable = false)
    @NotNull
    private Integer recoveryTimeSec = 60;

    @Column(name = "last_trip_time")
    private LocalDateTime lastTripTime;

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
     * Retrieves breaker name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBreakerName() { return breakerName; }
    /**
     * Performs the setBreakerName operation in this module.
     *
     * @param breakerName the breakerName input value
     */
    public void setBreakerName(String breakerName) { this.breakerName = breakerName; }
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
     * Retrieves failures count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getFailuresCount() { return failuresCount; }
    /**
     * Performs the setFailuresCount operation in this module.
     *
     * @param failuresCount the failuresCount input value
     */
    public void setFailuresCount(Integer failuresCount) { this.failuresCount = failuresCount; }
    /**
     * Retrieves success ratio data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSuccessRatio() { return successRatio; }
    /**
     * Performs the setSuccessRatio operation in this module.
     *
     * @param successRatio the successRatio input value
     */
    public void setSuccessRatio(BigDecimal successRatio) { this.successRatio = successRatio; }
    /**
     * Retrieves recovery time sec data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getRecoveryTimeSec() { return recoveryTimeSec; }
    /**
     * Performs the setRecoveryTimeSec operation in this module.
     *
     * @param recoveryTimeSec the recoveryTimeSec input value
     */
    public void setRecoveryTimeSec(Integer recoveryTimeSec) { this.recoveryTimeSec = recoveryTimeSec; }
    /**
     * Retrieves last trip time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastTripTime() { return lastTripTime; }
    /**
     * Performs the setLastTripTime operation in this module.
     *
     * @param lastTripTime the lastTripTime input value
     */
    public void setLastTripTime(LocalDateTime lastTripTime) { this.lastTripTime = lastTripTime; }
}