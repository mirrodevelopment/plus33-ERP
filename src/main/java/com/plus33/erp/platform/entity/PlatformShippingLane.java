/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformShippingLane.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformShippingLaneController
 * Related Service   : PlatformShippingLaneService, PlatformShippingLaneServiceImpl
 * Related Repository: PlatformShippingLaneRepository
 * Related Entity    : PlatformShippingLane
 * Related DTO       : N/A
 * Related Mapper    : PlatformShippingLaneMapper
 * Related DB Table  : platform_shipping_lane
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformShippingLaneRepository, PlatformShippingLaneMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_shipping_lane'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformShippingLane}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_shipping_lane'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_shipping_lane}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_shipping_lane")
public class PlatformShippingLane {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "source_node_id", nullable = false)
    @NotNull
    private Long sourceNodeId;

    @Column(name = "destination_node_id", nullable = false)
    @NotNull
    private Long destinationNodeId;

    @Column(name = "distance_km", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal distanceKm;

    @Column(name = "expected_duration_minutes", nullable = false)
    @NotNull
    private Integer expectedDurationMinutes;

    @Column(name = "transport_mode", nullable = false)
    @NotNull
    @Size(max = 50)
    private String transportMode;

    @Column(name = "cost_factor", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal costFactor = BigDecimal.valueOf(1.00);

    @Column(name = "carbon_factor", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal carbonFactor = BigDecimal.valueOf(1.00);

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "ACTIVE";

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
     * Retrieves source node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSourceNodeId() { return sourceNodeId; }
    /**
     * Performs the setSourceNodeId operation in this module.
     *
     * @param sourceNodeId the sourceNodeId input value
     */
    public void setSourceNodeId(Long sourceNodeId) { this.sourceNodeId = sourceNodeId; }
    /**
     * Retrieves destination node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDestinationNodeId() { return destinationNodeId; }
    /**
     * Performs the setDestinationNodeId operation in this module.
     *
     * @param destinationNodeId the destinationNodeId input value
     */
    public void setDestinationNodeId(Long destinationNodeId) { this.destinationNodeId = destinationNodeId; }
    /**
     * Retrieves distance km data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDistanceKm() { return distanceKm; }
    /**
     * Performs the setDistanceKm operation in this module.
     *
     * @param distanceKm the distanceKm input value
     */
    public void setDistanceKm(BigDecimal distanceKm) { this.distanceKm = distanceKm; }
    /**
     * Retrieves expected duration minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getExpectedDurationMinutes() { return expectedDurationMinutes; }
    /**
     * Performs the setExpectedDurationMinutes operation in this module.
     *
     * @param expectedDurationMinutes the expectedDurationMinutes input value
     */
    public void setExpectedDurationMinutes(Integer expectedDurationMinutes) { this.expectedDurationMinutes = expectedDurationMinutes; }
    /**
     * Retrieves transport mode data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTransportMode() { return transportMode; }
    /**
     * Performs the setTransportMode operation in this module.
     *
     * @param transportMode the transportMode input value
     */
    public void setTransportMode(String transportMode) { this.transportMode = transportMode; }
    /**
     * Retrieves cost factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCostFactor() { return costFactor; }
    /**
     * Performs the setCostFactor operation in this module.
     *
     * @param costFactor the costFactor input value
     */
    public void setCostFactor(BigDecimal costFactor) { this.costFactor = costFactor; }
    /**
     * Retrieves carbon factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCarbonFactor() { return carbonFactor; }
    /**
     * Performs the setCarbonFactor operation in this module.
     *
     * @param carbonFactor the carbonFactor input value
     */
    public void setCarbonFactor(BigDecimal carbonFactor) { this.carbonFactor = carbonFactor; }
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
}