/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformGeofenceEvent.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformGeofenceEventController
 * Related Service   : PlatformGeofenceEventService, PlatformGeofenceEventServiceImpl
 * Related Repository: PlatformGeofenceEventRepository
 * Related Entity    : PlatformGeofenceEvent
 * Related DTO       : N/A
 * Related Mapper    : PlatformGeofenceEventMapper
 * Related DB Table  : platform_geofence_event
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformGeofenceEventRepository, PlatformGeofenceEventMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_geofence_event'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformGeofenceEvent}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_geofence_event'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_geofence_event}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_geofence_event")
public class PlatformGeofenceEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "geofence_id", nullable = false)
    @NotNull
    private Long geofenceId;

    @Column(name = "asset_id", nullable = false)
    @NotNull
    private Long assetId;

    @Column(name = "route_id")
    private Long routeId;

    @Column(name = "operator_id")
    @Size(max = 100)
    private String operatorId;

    @Column(name = "event_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String eventType; // ENTER, EXIT, DWELL, INSIDE, OUTSIDE, VIOLATION

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "speed_kmh", precision = 5, scale = 2)
    private BigDecimal speedKmh;

    @Column(name = "heading_degrees", precision = 5, scale = 2)
    private BigDecimal headingDegrees;

    @Column(name = "gps_accuracy_meters", precision = 5, scale = 2)
    private BigDecimal gpsAccuracyMeters;

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
     * Retrieves geofence id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGeofenceId() { return geofenceId; }
    /**
     * Performs the setGeofenceId operation in this module.
     *
     * @param geofenceId the geofenceId input value
     */
    public void setGeofenceId(Long geofenceId) { this.geofenceId = geofenceId; }
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
     * Retrieves route id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRouteId() { return routeId; }
    /**
     * Performs the setRouteId operation in this module.
     *
     * @param routeId the routeId input value
     */
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    /**
     * Retrieves operator id data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperatorId() { return operatorId; }
    /**
     * Performs the setOperatorId operation in this module.
     *
     * @param operatorId the operatorId input value
     */
    public void setOperatorId(String operatorId) { this.operatorId = operatorId; }
    /**
     * Retrieves event type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEventType() { return eventType; }
    /**
     * Performs the setEventType operation in this module.
     *
     * @param eventType the eventType input value
     */
    public void setEventType(String eventType) { this.eventType = eventType; }
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
     * Retrieves heading degrees data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getHeadingDegrees() { return headingDegrees; }
    /**
     * Performs the setHeadingDegrees operation in this module.
     *
     * @param headingDegrees the headingDegrees input value
     */
    public void setHeadingDegrees(BigDecimal headingDegrees) { this.headingDegrees = headingDegrees; }
    /**
     * Retrieves gps accuracy meters data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getGpsAccuracyMeters() { return gpsAccuracyMeters; }
    /**
     * Performs the setGpsAccuracyMeters operation in this module.
     *
     * @param gpsAccuracyMeters the gpsAccuracyMeters input value
     */
    public void setGpsAccuracyMeters(BigDecimal gpsAccuracyMeters) { this.gpsAccuracyMeters = gpsAccuracyMeters; }
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