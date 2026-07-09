/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformVehicle.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformVehicleController
 * Related Service   : PlatformVehicleService, PlatformVehicleServiceImpl
 * Related Repository: PlatformVehicleRepository
 * Related Entity    : PlatformVehicle
 * Related DTO       : N/A
 * Related Mapper    : PlatformVehicleMapper
 * Related DB Table  : platform_vehicle
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformVehicleRepository, PlatformVehicleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_vehicle'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformVehicle}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_vehicle'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_vehicle}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_vehicle")
public class PlatformVehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "vehicle_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String vehicleCode;

    @Column(name = "capacity_kg", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal capacityKg;

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status = "AVAILABLE";

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
     * Retrieves vehicle code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVehicleCode() { return vehicleCode; }
    /**
     * Performs the setVehicleCode operation in this module.
     *
     * @param vehicleCode the vehicleCode input value
     */
    public void setVehicleCode(String vehicleCode) { this.vehicleCode = vehicleCode; }
    /**
     * Retrieves capacity kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCapacityKg() { return capacityKg; }
    /**
     * Performs the setCapacityKg operation in this module.
     *
     * @param capacityKg the capacityKg input value
     */
    public void setCapacityKg(BigDecimal capacityKg) { this.capacityKg = capacityKg; }
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