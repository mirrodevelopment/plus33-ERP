/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformVehicleTelemetry.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformVehicleTelemetryController
 * Related Service   : PlatformVehicleTelemetryService, PlatformVehicleTelemetryServiceImpl
 * Related Repository: PlatformVehicleTelemetryRepository
 * Related Entity    : PlatformVehicleTelemetry
 * Related DTO       : N/A
 * Related Mapper    : PlatformVehicleTelemetryMapper
 * Related DB Table  : platform_vehicle_telemetry
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformVehicleTelemetryRepository, PlatformVehicleTelemetryMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_vehicle_telemetry'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformVehicleTelemetry}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_vehicle_telemetry'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_vehicle_telemetry}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_vehicle_telemetry")
public class PlatformVehicleTelemetry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "speed_kmh", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal speedKmh;

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
     * Retrieves latitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLatitude() { return latitude; }
    /**
     * Performs the setLatitude operation in this module.
     *
     * @param latitude the latitude input value
     */
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    /**
     * Retrieves longitude data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLongitude() { return longitude; }
    /**
     * Performs the setLongitude operation in this module.
     *
     * @param longitude the longitude input value
     */
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    /**
     * Retrieves speed kmh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSpeedKmh() { return speedKmh; }
    /**
     * Performs the setSpeedKmh operation in this module.
     *
     * @param speedKmh the speedKmh input value
     */
    public void setSpeedKmh(BigDecimal speedKmh) { this.speedKmh = speedKmh; }
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