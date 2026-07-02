package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_fuel_optimization_policy")
public class PlatformFuelOptimizationPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "policy_code", nullable = false, unique = true)
    @NotNull
    @Size(max = 100)
    private String policyCode;

    @Column(name = "optimization_strategy", nullable = false)
    @NotNull
    @Size(max = 100)
    private String optimizationStrategy; // LowRPM, EcoSpeed, CargoAware

    @Column(name = "vehicle_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String vehicleType;

    @Column(name = "engine_type", nullable = false)
    @NotNull
    @Size(max = 100)
    private String engineType;

    @Column(name = "fuel_type", nullable = false)
    @NotNull
    @Size(max = 50)
    private String fuelType;

    @Column(name = "idle_limit_seconds", nullable = false)
    @NotNull
    private Integer idleLimitSeconds;

    @Column(name = "target_fuel_consumption", nullable = false, precision = 10, scale = 2)
    @NotNull
    private BigDecimal targetFuelConsumption;

    @Column(name = "eco_speed_min", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal ecoSpeedMin;

    @Column(name = "eco_speed_max", nullable = false, precision = 5, scale = 2)
    @NotNull
    private BigDecimal ecoSpeedMax;

    @Column(nullable = false)
    @NotNull
    private Boolean enabled = true;

    @Column(name = "effective_from", nullable = false)
    @NotNull
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    @NotNull
    private LocalDateTime effectiveTo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public String getPolicyCode() { return policyCode; }
    public void setPolicyCode(String policyCode) { this.policyCode = policyCode; }
    public String getOptimizationStrategy() { return optimizationStrategy; }
    public void setOptimizationStrategy(String optimizationStrategy) { this.optimizationStrategy = optimizationStrategy; }
    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }
    public String getEngineType() { return engineType; }
    public void setEngineType(String engineType) { this.engineType = engineType; }
    public String getFuelType() { return fuelType; }
    public void setFuelType(String fuelType) { this.fuelType = fuelType; }
    public Integer getIdleLimitSeconds() { return idleLimitSeconds; }
    public void setIdleLimitSeconds(Integer idleLimitSeconds) { this.idleLimitSeconds = idleLimitSeconds; }
    public BigDecimal getTargetFuelConsumption() { return targetFuelConsumption; }
    public void setTargetFuelConsumption(BigDecimal targetFuelConsumption) { this.targetFuelConsumption = targetFuelConsumption; }
    public BigDecimal getEcoSpeedMin() { return ecoSpeedMin; }
    public void setEcoSpeedMin(BigDecimal ecoSpeedMin) { this.ecoSpeedMin = ecoSpeedMin; }
    public BigDecimal getEcoSpeedMax() { return ecoSpeedMax; }
    public void setEcoSpeedMax(BigDecimal ecoSpeedMax) { this.ecoSpeedMax = ecoSpeedMax; }
    public Boolean getEnabled() { return enabled; }
    public void setEnabled(Boolean enabled) { this.enabled = enabled; }
    public LocalDateTime getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDateTime effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDateTime getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDateTime effectiveTo) { this.effectiveTo = effectiveTo; }
}