/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEsgScope1Log.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEsgScope1LogController
 * Related Service   : PlatformEsgScope1LogService, PlatformEsgScope1LogServiceImpl
 * Related Repository: PlatformEsgScope1LogRepository
 * Related Entity    : PlatformEsgScope1Log
 * Related DTO       : N/A
 * Related Mapper    : PlatformEsgScope1LogMapper
 * Related DB Table  : platform_esg_scope1_log
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEsgScope1LogRepository, PlatformEsgScope1LogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_esg_scope1_log'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEsgScope1Log}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_esg_scope1_log'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_esg_scope1_log}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_esg_scope1_log")
public class PlatformEsgScope1Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "fuel_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String fuelType; // Diesel, Petrol, LPG, CNG, Biodiesel, HVO

    @Column(name = "fuel_consumed_liters", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal fuelConsumedLiters;

    @Column(name = "co2e_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal co2eKg;

    @Column(name = "ch4_kg", nullable = false, precision = 10, scale = 3)
    @NotNull
    private BigDecimal ch4Kg;

    @Column(name = "n2o_kg", nullable = false, precision = 10, scale = 3)
    @NotNull
    private BigDecimal n2oKg;

    @Column(name = "emission_factor", nullable = false, precision = 10, scale = 4)
    @NotNull
    private BigDecimal emissionFactor;

    @Column(name = "calculation_method", nullable = false)
    @NotNull
    @Size(max = 100)
    private String calculationMethod;

    @Column(name = "trip_id", nullable = false)
    @NotNull
    private Long tripId;

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
     * Retrieves fuel type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getFuelType() { return fuelType; }
    /**
     * Performs the setFuelType operation in this module.
     *
     * @param fuelType the fuelType input value
     */
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    /**
     * Retrieves fuel consumed liters data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getFuelConsumedLiters() { return fuelConsumedLiters; }
    /**
     * Performs the setFuelConsumedLiters operation in this module.
     *
     * @param fuelConsumedLiters the fuelConsumedLiters input value
     */
    public void setFuelConsumedLiters(BigDecimal fuelConsumedLiters) { this.fuelConsumedLiters = fuelConsumedLiters; }
    /**
     * Retrieves co2e kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCo2eKg() { return co2eKg; }
    /**
     * Performs the setCo2eKg operation in this module.
     *
     * @param co2eKg the co2eKg input value
     */
    public void setCo2eKg(BigDecimal co2eKg) { this.co2eKg = co2eKg; }
    /**
     * Retrieves ch4 kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCh4Kg() { return ch4Kg; }
    /**
     * Performs the setCh4Kg operation in this module.
     *
     * @param ch4Kg the ch4Kg input value
     */
    public void setCh4Kg(BigDecimal ch4Kg) { this.ch4Kg = ch4Kg; }
    /**
     * Retrieves n2o kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getN2oKg() { return n2oKg; }
    /**
     * Performs the setN2oKg operation in this module.
     *
     * @param n2oKg the n2oKg input value
     */
    public void setN2oKg(BigDecimal n2oKg) { this.n2oKg = n2oKg; }
    /**
     * Retrieves emission factor data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getEmissionFactor() { return emissionFactor; }
    /**
     * Performs the setEmissionFactor operation in this module.
     *
     * @param emissionFactor the emissionFactor input value
     */
    public void setEmissionFactor(BigDecimal emissionFactor) { this.emissionFactor = emissionFactor; }
    /**
     * Retrieves calculation method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getCalculationMethod() { return calculationMethod; }
    /**
     * Performs the setCalculationMethod operation in this module.
     *
     * @param calculationMethod the calculationMethod input value
     */
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    /**
     * Retrieves trip id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTripId() { return tripId; }
    /**
     * Performs the setTripId operation in this module.
     *
     * @param tripId the tripId input value
     */
    public void setTripId(Long tripId) { this.tripId = tripId; }
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