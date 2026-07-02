package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getRouteId() { return routeId; }
    public void setRouteId(Long routeId) { this.routeId = routeId; }
    public BigDecimal getFuelCost() { return fuelCost; }
    public void setFuelCost(BigDecimal fuelCost) { this.fuelCost = fuelCost; }
    public BigDecimal getDriverCost() { return driverCost; }
    public void setDriverCost(BigDecimal driverCost) { this.driverCost = driverCost; }
    public BigDecimal getMaintenanceCost() { return maintenanceCost; }
    public void setMaintenanceCost(BigDecimal maintenanceCost) { this.maintenanceCost = maintenanceCost; }
    public BigDecimal getTollCost() { return tollCost; }
    public void setTollCost(BigDecimal tollCost) { this.tollCost = tollCost; }
    public BigDecimal getParkingCost() { return parkingCost; }
    public void setParkingCost(BigDecimal parkingCost) { this.parkingCost = parkingCost; }
    public BigDecimal getInsuranceCost() { return insuranceCost; }
    public void setInsuranceCost(BigDecimal insuranceCost) { this.insuranceCost = insuranceCost; }
    public BigDecimal getDepreciationCost() { return depreciationCost; }
    public void setDepreciationCost(BigDecimal depreciationCost) { this.depreciationCost = depreciationCost; }
    public BigDecimal getCarbonCost() { return carbonCost; }
    public void setCarbonCost(BigDecimal carbonCost) { this.carbonCost = carbonCost; }
    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}