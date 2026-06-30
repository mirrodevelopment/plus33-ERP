package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
public class Machine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "asset_id")
    private Long assetId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "machine_type", length = 50)
    private String machineType;

    @Column(name = "max_speed_units", precision = 10, scale = 2)
    private BigDecimal maxSpeedUnits;

    @Column(name = "cycle_time_seconds", precision = 10, scale = 2)
    private BigDecimal cycleTimeSeconds;

    @Column(name = "setup_time_minutes", nullable = false, precision = 10, scale = 2)
    private BigDecimal setupTimeMinutes = BigDecimal.ZERO;

    @Column(name = "hourly_run_cost", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyRunCost = BigDecimal.ZERO;

    @Column(name = "hourly_idle_cost", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyIdleCost = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public Machine() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public Long getAssetId() { return assetId; }
    public void setAssetId(Long assetId) { this.assetId = assetId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getMachineType() { return machineType; }
    public void setMachineType(String machineType) { this.machineType = machineType; }
    public BigDecimal getMaxSpeedUnits() { return maxSpeedUnits; }
    public void setMaxSpeedUnits(BigDecimal maxSpeedUnits) { this.maxSpeedUnits = maxSpeedUnits; }
    public BigDecimal getCycleTimeSeconds() { return cycleTimeSeconds; }
    public void setCycleTimeSeconds(BigDecimal cycleTimeSeconds) { this.cycleTimeSeconds = cycleTimeSeconds; }
    public BigDecimal getSetupTimeMinutes() { return setupTimeMinutes; }
    public void setSetupTimeMinutes(BigDecimal setupTimeMinutes) { this.setupTimeMinutes = setupTimeMinutes; }
    public BigDecimal getHourlyRunCost() { return hourlyRunCost; }
    public void setHourlyRunCost(BigDecimal hourlyRunCost) { this.hourlyRunCost = hourlyRunCost; }
    public BigDecimal getHourlyIdleCost() { return hourlyIdleCost; }
    public void setHourlyIdleCost(BigDecimal hourlyIdleCost) { this.hourlyIdleCost = hourlyIdleCost; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
