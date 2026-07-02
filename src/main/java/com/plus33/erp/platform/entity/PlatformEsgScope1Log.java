package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public BigDecimal getFuelConsumedLiters() { return fuelConsumedLiters; }
    public void setFuelConsumedLiters(BigDecimal fuelConsumedLiters) { this.fuelConsumedLiters = fuelConsumedLiters; }
    public BigDecimal getCo2eKg() { return co2eKg; }
    public void setCo2eKg(BigDecimal co2eKg) { this.co2eKg = co2eKg; }
    public BigDecimal getCh4Kg() { return ch4Kg; }
    public void setCh4Kg(BigDecimal ch4Kg) { this.ch4Kg = ch4Kg; }
    public BigDecimal getN2oKg() { return n2oKg; }
    public void setN2oKg(BigDecimal n2oKg) { this.n2oKg = n2oKg; }
    public BigDecimal getEmissionFactor() { return emissionFactor; }
    public void setEmissionFactor(BigDecimal emissionFactor) { this.emissionFactor = emissionFactor; }
    public String getCalculationMethod() { return calculationMethod; }
    public void setCalculationMethod(String calculationMethod) { this.calculationMethod = calculationMethod; }
    public Long getTripId() { return tripId; }
    public void setTripId(Long tripId) { this.tripId = tripId; }
    public LocalDateTime getLoggedAt() { return loggedAt; }
    public void setLoggedAt(LocalDateTime loggedAt) { this.loggedAt = loggedAt; }
}