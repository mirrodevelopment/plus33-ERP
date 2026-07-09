/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformTransitRoute.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformTransitRouteController
 * Related Service   : PlatformTransitRouteService, PlatformTransitRouteServiceImpl
 * Related Repository: PlatformTransitRouteRepository
 * Related Entity    : PlatformTransitRoute
 * Related DTO       : N/A
 * Related Mapper    : PlatformTransitRouteMapper
 * Related DB Table  : platform_transit_route
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformTransitRouteRepository, PlatformTransitRouteMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_transit_route'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformTransitRoute}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_transit_route'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_transit_route}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_transit_route")
public class PlatformTransitRoute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "origin_node_id", nullable = false)
    @NotNull
    private Long originNodeId;

    @Column(name = "dest_node_id", nullable = false)
    @NotNull
    private Long destNodeId;

    @Column(name = "route_path_json", nullable = false, columnDefinition = "TEXT")
    @NotNull
    private String routePathJson;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "IN_TRANSIT";

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
     * Retrieves origin node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getOriginNodeId() { return originNodeId; }
    /**
     * Performs the setOriginNodeId operation in this module.
     *
     * @param originNodeId the originNodeId input value
     */
    public void setOriginNodeId(Long originNodeId) { this.originNodeId = originNodeId; }
    /**
     * Retrieves dest node id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getDestNodeId() { return destNodeId; }
    /**
     * Performs the setDestNodeId operation in this module.
     *
     * @param destNodeId the destNodeId input value
     */
    public void setDestNodeId(Long destNodeId) { this.destNodeId = destNodeId; }
    /**
     * Retrieves route path json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRoutePathJson() { return routePathJson; }
    /**
     * Performs the setRoutePathJson operation in this module.
     *
     * @param routePathJson the routePathJson input value
     */
    public void setRoutePathJson(String routePathJson) { this.routePathJson = routePathJson; }
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