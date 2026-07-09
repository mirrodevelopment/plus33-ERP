/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEsgScope2Log.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEsgScope2LogController
 * Related Service   : PlatformEsgScope2LogService, PlatformEsgScope2LogServiceImpl
 * Related Repository: PlatformEsgScope2LogRepository
 * Related Entity    : PlatformEsgScope2Log
 * Related DTO       : N/A
 * Related Mapper    : PlatformEsgScope2LogMapper
 * Related DB Table  : platform_esg_scope2_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEsgScope2LogRepository, PlatformEsgScope2LogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_esg_scope2_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEsgScope2Log}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_esg_scope2_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_esg_scope2_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_esg_scope2_log")
public class PlatformEsgScope2Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "charging_station_id", nullable = false)
    @NotNull
    private Long chargingStationId;

    @Column(name = "energy_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal energyKwh;

    @Column(name = "grid_region", nullable = false)
    @NotNull
    @Size(max = 100)
    private String gridRegion;

    @Column(name = "grid_factor", nullable = false, precision = 10, scale = 4)
    @NotNull
    private BigDecimal gridFactor;

    @Column(name = "renewable_percentage", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal renewablePercentage;

    @Column(name = "market_based_co2e_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal marketBasedCo2eKg;

    @Column(name = "location_based_co2e_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal locationBasedCo2eKg;

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
     * Retrieves charging station id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getChargingStationId() { return chargingStationId; }
    /**
     * Performs the setChargingStationId operation in this module.
     *
     * @param chargingStationId the chargingStationId input value
     */
    public void setChargingStationId(Long chargingStationId) { this.chargingStationId = chargingStationId; }
    /**
     * Retrieves energy kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEnergyKwh() { return energyKwh; }
    /**
     * Performs the setEnergyKwh operation in this module.
     *
     * @param energyKwh the energyKwh input value
     */
    public void setEnergyKwh(BigDecimal energyKwh) { this.energyKwh = energyKwh; }
    /**
     * Retrieves grid region data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGridRegion() { return gridRegion; }
    /**
     * Performs the setGridRegion operation in this module.
     *
     * @param gridRegion the gridRegion input value
     */
    public void setGridRegion(String gridRegion) { this.gridRegion = gridRegion; }
    /**
     * Retrieves grid factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getGridFactor() { return gridFactor; }
    /**
     * Performs the setGridFactor operation in this module.
     *
     * @param gridFactor the gridFactor input value
     */
    public void setGridFactor(BigDecimal gridFactor) { this.gridFactor = gridFactor; }
    /**
     * Retrieves renewable percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRenewablePercentage() { return renewablePercentage; }
    /**
     * Performs the setRenewablePercentage operation in this module.
     *
     * @param renewablePercentage the renewablePercentage input value
     */
    public void setRenewablePercentage(BigDecimal renewablePercentage) { this.renewablePercentage = renewablePercentage; }
    /**
     * Retrieves market based co2e kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMarketBasedCo2eKg() { return marketBasedCo2eKg; }
    /**
     * Performs the setMarketBasedCo2eKg operation in this module.
     *
     * @param marketBasedCo2eKg the marketBasedCo2eKg input value
     */
    public void setMarketBasedCo2eKg(BigDecimal marketBasedCo2eKg) { this.marketBasedCo2eKg = marketBasedCo2eKg; }
    /**
     * Retrieves location based co2e kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLocationBasedCo2eKg() { return locationBasedCo2eKg; }
    /**
     * Performs the setLocationBasedCo2eKg operation in this module.
     *
     * @param locationBasedCo2eKg the locationBasedCo2eKg input value
     */
    public void setLocationBasedCo2eKg(BigDecimal locationBasedCo2eKg) { this.locationBasedCo2eKg = locationBasedCo2eKg; }
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