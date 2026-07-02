package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getStationId() { return stationId; }
    public void setStationId(Long stationId) { this.stationId = stationId; }
    public Integer getConnectorId() { return connectorId; }
    public void setConnectorId(Integer connectorId) { this.connectorId = connectorId; }
    public LocalDateTime getReservationStart() { return reservationStart; }
    public void setReservationStart(LocalDateTime reservationStart) { this.reservationStart = reservationStart; }
    public LocalDateTime getReservationEnd() { return reservationEnd; }
    public void setReservationEnd(LocalDateTime reservationEnd) { this.reservationEnd = reservationEnd; }
    public BigDecimal getPlannedEnergyKwh() { return plannedEnergyKwh; }
    public void setPlannedEnergyKwh(BigDecimal plannedEnergyKwh) { this.plannedEnergyKwh = plannedEnergyKwh; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public String getChargingStrategy() { return chargingStrategy; }
    public void setChargingStrategy(String chargingStrategy) { this.chargingStrategy = chargingStrategy; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}