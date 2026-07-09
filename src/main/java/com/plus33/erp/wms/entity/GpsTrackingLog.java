/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : GpsTrackingLog.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GpsTrackingLogController
 * Related Service   : GpsTrackingLogService, GpsTrackingLogServiceImpl
 * Related Repository: GpsTrackingLogRepository
 * Related Entity    : GpsTrackingLog
 * Related DTO       : N/A
 * Related Mapper    : GpsTrackingLogMapper
 * Related DB Table  : gps_tracking_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GpsTrackingLogRepository, GpsTrackingLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'gps_tracking_logs'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code GpsTrackingLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'gps_tracking_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code gps_tracking_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "gps_tracking_logs")
public class GpsTrackingLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private DeliveryRoute route;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    private BigDecimal longitude;

    @Column(name = "speed_kmh", precision = 5, scale = 2)
    private BigDecimal speedKmh;

    @Column(name = "logged_at", nullable = false, updatable = false)
    private LocalDateTime loggedAt = LocalDateTime.now();

    // Getters and setters
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
     * Retrieves route data from the database.
     *
     * @return the DeliveryRoute result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public DeliveryRoute getRoute() { return route; }
    /**
     * Performs the setRoute operation in this module.
     *
     * @param route the route input value
     */
    public void setRoute(DeliveryRoute route) { this.route = route; }
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
     * Retrieves logged at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLoggedAt() { return loggedAt; }
}