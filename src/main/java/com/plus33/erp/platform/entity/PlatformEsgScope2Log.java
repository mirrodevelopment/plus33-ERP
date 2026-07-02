package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public Long getChargingStationId() { return chargingStationId; }
    public void setChargingStationId(Long chargingStationId) { this.chargingStationId = chargingStationId; }
    public BigDecimal getEnergyKwh() { return energyKwh; }
    public void setEnergyKwh(BigDecimal energyKwh) { this.energyKwh = energyKwh; }
    public String getGridRegion() { return gridRegion; }
    public void setGridRegion(String gridRegion) { this.gridRegion = gridRegion; }
    public BigDecimal getGridFactor() { return gridFactor; }
    public void setGridFactor(BigDecimal gridFactor) { this.gridFactor = gridFactor; }
    public BigDecimal getRenewablePercentage() { return renewablePercentage; }
    public void setRenewablePercentage(BigDecimal renewablePercentage) { this.renewablePercentage = renewablePercentage; }
    public BigDecimal getMarketBasedCo2eKg() { return marketBasedCo2eKg; }
    public void setMarketBasedCo2eKg(BigDecimal marketBasedCo2eKg) { this.marketBasedCo2eKg = marketBasedCo2eKg; }
    public BigDecimal getLocationBasedCo2eKg() { return locationBasedCo2eKg; }
    public void setLocationBasedCo2eKg(BigDecimal locationBasedCo2eKg) { this.locationBasedCo2eKg = locationBasedCo2eKg; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}