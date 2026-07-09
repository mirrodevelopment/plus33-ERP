/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.entity
 * File              : PlatformEvChargingSchedule.java
 * Purpose           : JPA Entity representing a persistent database record in Platform Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformEvChargingScheduleController
 * Related Service   : PlatformEvChargingScheduleService, PlatformEvChargingScheduleServiceImpl
 * Related Repository: PlatformEvChargingScheduleRepository
 * Related Entity    : PlatformEvChargingSchedule
 * Related DTO       : N/A
 * Related Mapper    : PlatformEvChargingScheduleMapper
 * Related DB Table  : platform_ev_charging_schedule
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformEvChargingScheduleRepository, PlatformEvChargingScheduleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'platform_ev_charging_schedule'. Defines persistent domain object for Platform Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code PlatformEvChargingSchedule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'platform_ev_charging_schedule'.</p>
 *
 * <p><b>Database Table   :</b> {@code platform_ev_charging_schedule}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "platform_ev_charging_schedule")
public class PlatformEvChargingSchedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vehicle_id", nullable = false)
    @NotNull
    private Long vehicleId;

    @Column(name = "station_id", nullable = false)
    @NotNull
    private Long stationId;

    @Column(name = "connector_id", nullable = false)
    @NotNull
    private Integer connectorId;

    @Column(name = "reservation_start", nullable = false)
    @NotNull
    private LocalDateTime reservationStart;

    @Column(name = "reservation_end", nullable = false)
    @NotNull
    private LocalDateTime reservationEnd;

    @Column(name = "planned_energy_kwh", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal plannedEnergyKwh;

    @Column(nullable = false)
    @NotNull
    private Integer priority = 0;

    @Column(name = "charging_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String chargingStrategy; // Immediate, OffPeak, RenewablePreferred, CheapestAvailable, BalancedFleet

    @Column(nullable = false)
    @NotNull
    @Size(max = 50)
    private String status; // BOOKED, ACTIVE, COMPLETED, CANCELLED

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
     * Retrieves station id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getStationId() { return stationId; }
    /**
     * Performs the setStationId operation in this module.
     *
     * @param stationId the stationId input value
     */
    public void setStationId(Long stationId) { this.stationId = stationId; }
    /**
     * Retrieves connector id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getConnectorId() { return connectorId; }
    /**
     * Performs the setConnectorId operation in this module.
     *
     * @param connectorId the connectorId input value
     */
    public void setConnectorId(Integer connectorId) { this.connectorId = connectorId; }
    /**
     * Retrieves reservation start data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReservationStart() { return reservationStart; }
    /**
     * Performs the setReservationStart operation in this module.
     *
     * @param reservationStart the reservationStart input value
     */
    public void setReservationStart(LocalDateTime reservationStart) { this.reservationStart = reservationStart; }
    /**
     * Retrieves reservation end data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReservationEnd() { return reservationEnd; }
    /**
     * Performs the setReservationEnd operation in this module.
     *
     * @param reservationEnd the reservationEnd input value
     */
    public void setReservationEnd(LocalDateTime reservationEnd) { this.reservationEnd = reservationEnd; }
    /**
     * Retrieves planned energy kwh data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getPlannedEnergyKwh() { return plannedEnergyKwh; }
    /**
     * Performs the setPlannedEnergyKwh operation in this module.
     *
     * @param plannedEnergyKwh the plannedEnergyKwh input value
     */
    public void setPlannedEnergyKwh(BigDecimal plannedEnergyKwh) { this.plannedEnergyKwh = plannedEnergyKwh; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
    /**
     * Retrieves charging strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChargingStrategy() { return chargingStrategy; }
    /**
     * Performs the setChargingStrategy operation in this module.
     *
     * @param chargingStrategy the chargingStrategy input value
     */
    public void setChargingStrategy(String chargingStrategy) { this.chargingStrategy = chargingStrategy; }
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