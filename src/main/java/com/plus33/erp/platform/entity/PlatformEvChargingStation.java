/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEvChargingStation.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvChargingStationController
 * Related Service   : PlatformEvChargingStationService, PlatformEvChargingStationServiceImpl
 * Related Repository: PlatformEvChargingStationRepository
 * Related Entity    : PlatformEvChargingStation
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvChargingStationMapper
 * Related DB Table  : platform_ev_charging_station
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvChargingStationRepository, PlatformEvChargingStationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ev_charging_station'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformEvChargingStation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ev_charging_station'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_charging_station}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ev_charging_station")
public class PlatformEvChargingStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "station_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String stationCode;

    @Column(nullable = false)
    @NotNull
    @Size(max = 100)
    private String operator;

    @Column(name = "location_name", nullable = false)
    @NotNull
    @Size(max = 200)
    private String locationName;

    @Column(nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal latitude;

    @Column(nullable = false, precision = 10, scale = 6)
    @NotNull
    private BigDecimal longitude;

    @Column(name = "charger_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String chargerType; // AC, DC

    @Column(name = "connector_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String connectorType; // CCS2, CHAdeMO, Type2, NACS

    @Column(name = "max_power_kw", nullable = false, precision = 7, scale = 2)
    @NotNull
    private BigDecimal maxPowerKw;

    @Column(name = "simultaneous_connectors", nullable = false)
    @NotNull
    private Integer simultaneousConnectors;

    @Column(name = "availability_status", nullable = false)
    @NotNull
    @Size(max = 50)
    private String availabilityStatus; // AVAILABLE, OCCUPIED, OFFLINE

    @Column(name = "tariff_plan_code", nullable = false)
    @NotNull
    @Size(max = 100)
    private String tariffPlanCode;

    @Column(name = "renewable_supported", nullable = false)
    @NotNull
    private Boolean renewableSupported = false;

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
     * Retrieves station code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStationCode() { return stationCode; }
    /**
     * Performs the setStationCode operation in this module.
     *
     * @param stationCode the stationCode input value
     */
    public void setStationCode(String stationCode) { this.stationCode = stationCode; }
    /**
     * Retrieves operator data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOperator() { return operator; }
    /**
     * Performs the setOperator operation in this module.
     *
     * @param operator the operator input value
     */
    public void setOperator(String operator) { this.operator = operator; }
    /**
     * Retrieves location name data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLocationName() { return locationName; }
    /**
     * Performs the setLocationName operation in this module.
     *
     * @param locationName the locationName input value
     */
    public void setLocationName(String locationName) { this.locationName = locationName; }
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
     * Retrieves charger type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChargerType() { return chargerType; }
    /**
     * Performs the setChargerType operation in this module.
     *
     * @param chargerType the chargerType input value
     */
    public void setChargerType(String chargerType) { this.chargerType = chargerType; }
    /**
     * Retrieves connector type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConnectorType() { return connectorType; }
    /**
     * Performs the setConnectorType operation in this module.
     *
     * @param connectorType the connectorType input value
     */
    public void setConnectorType(String connectorType) { this.connectorType = connectorType; }
    /**
     * Retrieves max power kw data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxPowerKw() { return maxPowerKw; }
    /**
     * Performs the setMaxPowerKw operation in this module.
     *
     * @param maxPowerKw the maxPowerKw input value
     */
    public void setMaxPowerKw(BigDecimal maxPowerKw) { this.maxPowerKw = maxPowerKw; }
    /**
     * Retrieves simultaneous connectors data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSimultaneousConnectors() { return simultaneousConnectors; }
    /**
     * Performs the setSimultaneousConnectors operation in this module.
     *
     * @param simultaneousConnectors the simultaneousConnectors input value
     */
    public void setSimultaneousConnectors(Integer simultaneousConnectors) { this.simultaneousConnectors = simultaneousConnectors; }
    /**
     * Retrieves availability status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAvailabilityStatus() { return availabilityStatus; }
    /**
     * Performs the setAvailabilityStatus operation in this module.
     *
     * @param availabilityStatus the availabilityStatus input value
     */
    public void setAvailabilityStatus(String availabilityStatus) { this.availabilityStatus = availabilityStatus; }
    /**
     * Retrieves tariff plan code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTariffPlanCode() { return tariffPlanCode; }
    /**
     * Performs the setTariffPlanCode operation in this module.
     *
     * @param tariffPlanCode the tariffPlanCode input value
     */
    public void setTariffPlanCode(String tariffPlanCode) { this.tariffPlanCode = tariffPlanCode; }
    /**
     * Retrieves renewable supported data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getRenewableSupported() { return renewableSupported; }
    /**
     * Performs the setRenewableSupported operation in this module.
     *
     * @param renewableSupported the renewableSupported input value
     */
    public void setRenewableSupported(Boolean renewableSupported) { this.renewableSupported = renewableSupported; }
}