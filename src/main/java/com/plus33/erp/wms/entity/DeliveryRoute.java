/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : DeliveryRoute.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DeliveryRouteController
 * Related Service   : DeliveryRouteService, DeliveryRouteServiceImpl
 * Related Repository: DeliveryRouteRepository
 * Related Entity    : DeliveryRoute
 * Related DTO       : N/A
 * Related Mapper    : DeliveryRouteMapper
 * Related DB Table  : delivery_routes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DeliveryRouteRepository, DeliveryRouteMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'delivery_routes'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code DeliveryRoute}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'delivery_routes'.</p>
 *
 * <p><b>Database Table   :</b> {@code delivery_routes}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "delivery_routes")
public class DeliveryRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(name = "route_number", nullable = false, unique = true, length = 50)
    private String routeNumber;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "vehicle_number", length = 50)
    private String vehicleNumber;

    @Column(name = "max_capacity_kg", precision = 12, scale = 3)
    private BigDecimal maxCapacityKg;

    @Column(name = "current_load_kg", precision = 12, scale = 3)
    private BigDecimal currentLoadKg = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "PLANNED";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves carrier id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCarrierId() { return carrierId; }
    /**
     * Performs the setCarrierId operation in this module.
     *
     * @param carrierId the carrierId input value
     */
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }
    /**
     * Retrieves route number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRouteNumber() { return routeNumber; }
    /**
     * Performs the setRouteNumber operation in this module.
     *
     * @param routeNumber the routeNumber input value
     */
    public void setRouteNumber(String routeNumber) { this.routeNumber = routeNumber; }
    /**
     * Retrieves driver name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDriverName() { return driverName; }
    /**
     * Performs the setDriverName operation in this module.
     *
     * @param driverName the driverName input value
     */
    public void setDriverName(String driverName) { this.driverName = driverName; }
    /**
     * Retrieves vehicle number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVehicleNumber() { return vehicleNumber; }
    /**
     * Performs the setVehicleNumber operation in this module.
     *
     * @param vehicleNumber the vehicleNumber input value
     */
    public void setVehicleNumber(String vehicleNumber) { this.vehicleNumber = vehicleNumber; }
    /**
     * Retrieves max capacity kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxCapacityKg() { return maxCapacityKg; }
    /**
     * Performs the setMaxCapacityKg operation in this module.
     *
     * @param maxCapacityKg the maxCapacityKg input value
     */
    public void setMaxCapacityKg(BigDecimal maxCapacityKg) { this.maxCapacityKg = maxCapacityKg; }
    /**
     * Retrieves current load kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCurrentLoadKg() { return currentLoadKg; }
    /**
     * Performs the setCurrentLoadKg operation in this module.
     *
     * @param currentLoadKg the currentLoadKg input value
     */
    public void setCurrentLoadKg(BigDecimal currentLoadKg) { this.currentLoadKg = currentLoadKg; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}