/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : RouteStop.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RouteStopController
 * Related Service   : RouteStopService, RouteStopServiceImpl
 * Related Repository: RouteStopRepository
 * Related Entity    : RouteStop
 * Related DTO       : N/A
 * Related Mapper    : RouteStopMapper
 * Related DB Table  : route_stops
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RouteStopRepository, RouteStopMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'route_stops'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code RouteStop}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'route_stops'.</p>
 *
 * <p><b>Database Table   :</b> {@code route_stops}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "route_stops")
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id", nullable = false)
    private DeliveryRoute route;

    @Column(name = "stop_sequence", nullable = false)
    private int stopSequence;

    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

    @Column(name = "estimated_arrival")
    private LocalDateTime estimatedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";

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
     * Retrieves stop sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getStopSequence() { return stopSequence; }
    /**
     * Performs the setStopSequence operation in this module.
     *
     * @param stopSequence the stopSequence input value
     */
    public void setStopSequence(int stopSequence) { this.stopSequence = stopSequence; }
    /**
     * Retrieves shipment id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getShipmentId() { return shipmentId; }
    /**
     * Performs the setShipmentId operation in this module.
     *
     * @param shipmentId the shipmentId input value
     */
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    /**
     * Retrieves estimated arrival data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEstimatedArrival() { return estimatedArrival; }
    /**
     * Performs the setEstimatedArrival operation in this module.
     *
     * @param estimatedArrival the estimatedArrival input value
     */
    public void setEstimatedArrival(LocalDateTime estimatedArrival) { this.estimatedArrival = estimatedArrival; }
    /**
     * Retrieves actual arrival data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getActualArrival() { return actualArrival; }
    /**
     * Performs the setActualArrival operation in this module.
     *
     * @param actualArrival the actualArrival input value
     */
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
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