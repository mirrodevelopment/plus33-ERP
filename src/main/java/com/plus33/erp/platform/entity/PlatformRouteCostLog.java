/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformRouteCostLog.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformRouteCostLogController
 * Related Service   : PlatformRouteCostLogService, PlatformRouteCostLogServiceImpl
 * Related Repository: PlatformRouteCostLogRepository
 * Related Entity    : PlatformRouteCostLog
 * Related DTO       : N/A
 * Related Mapper    : PlatformRouteCostLogMapper
 * Related DB Table  : platform_route_cost_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformRouteCostLogRepository, PlatformRouteCostLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_route_cost_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformRouteCostLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_route_cost_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_route_cost_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_route_cost_log")
public class PlatformRouteCostLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_id", nullable = false)
    @NotNull
    private Long routeId;

    @Column(name = "fuel_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal fuelCost;

    @Column(name = "driver_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal driverCost;

    @Column(name = "maintenance_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal maintenanceCost;

    @Column(name = "toll_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal tollCost;

    @Column(name = "parking_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal parkingCost;

    @Column(name = "insurance_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal insuranceCost;

    @Column(name = "depreciation_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal depreciationCost;

    @Column(name = "carbon_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal carbonCost;

    @Column(name = "total_cost", nullable = false, precision = 12, scale = 2)
    @NotNull
    private BigDecimal totalCost;

    @Column(nullable = false)
    @NotNull
    @Size(max = 3)
    private String currency = "USD";

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
     * Retrieves fuel cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelCost() { return fuelCost; }
    /**
     * Performs the setFuelCost operation in this module.
     *
     * @param fuelCost the fuelCost input value
     */
    public void setFuelCost(BigDecimal fuelCost) { this.fuelCost = fuelCost; }
    /**
     * Retrieves driver cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDriverCost() { return driverCost; }
    /**
     * Performs the setDriverCost operation in this module.
     *
     * @param driverCost the driverCost input value
     */
    public void setDriverCost(BigDecimal driverCost) { this.driverCost = driverCost; }
    /**
     * Retrieves maintenance cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaintenanceCost() { return maintenanceCost; }
    /**
     * Performs the setMaintenanceCost operation in this module.
     *
     * @param maintenanceCost the maintenanceCost input value
     */
    public void setMaintenanceCost(BigDecimal maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    /**
     * Retrieves toll cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTollCost() { return tollCost; }
    /**
     * Performs the setTollCost operation in this module.
     *
     * @param tollCost the tollCost input value
     */
    public void setTollCost(BigDecimal tollCost) { this.tollCost = tollCost; }
    /**
     * Retrieves parking cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getParkingCost() { return parkingCost; }
    /**
     * Performs the setParkingCost operation in this module.
     *
     * @param parkingCost the parkingCost input value
     */
    public void setParkingCost(BigDecimal parkingCost) { this.parkingCost = parkingCost; }
    /**
     * Retrieves insurance cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInsuranceCost() { return insuranceCost; }
    /**
     * Performs the setInsuranceCost operation in this module.
     *
     * @param insuranceCost the insuranceCost input value
     */
    public void setInsuranceCost(BigDecimal insuranceCost) { this.insuranceCost = insuranceCost; }
    /**
     * Retrieves depreciation cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDepreciationCost() { return depreciationCost; }
    /**
     * Performs the setDepreciationCost operation in this module.
     *
     * @param depreciationCost the depreciationCost input value
     */
    public void setDepreciationCost(BigDecimal depreciationCost) { this.depreciationCost = depreciationCost; }
    /**
     * Retrieves carbon cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCarbonCost() { return carbonCost; }
    /**
     * Performs the setCarbonCost operation in this module.
     *
     * @param carbonCost the carbonCost input value
     */
    public void setCarbonCost(BigDecimal carbonCost) { this.carbonCost = carbonCost; }
    /**
     * Retrieves total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalCost() { return totalCost; }
    /**
     * Performs the setTotalCost operation in this module.
     *
     * @param totalCost the totalCost input value
     */
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    /**
     * Retrieves currency data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCurrency() { return currency; }
    /**
     * Performs the setCurrency operation in this module.
     *
     * @param currency the currency input value
     */
    public void setCurrency(String currency) { this.currency = currency; }
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