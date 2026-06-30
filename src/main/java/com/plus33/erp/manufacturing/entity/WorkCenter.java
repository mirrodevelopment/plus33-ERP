package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "work_centers")
public class WorkCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(length = 100)
    private String department;

    @Column(name = "work_center_type", nullable = false, length = 30)
    private String workCenterType = "MACHINE"; // MACHINE, LABOR, MIXED, SUBCONTRACT

    @Column(name = "machine_capacity", nullable = false, precision = 10, scale = 2)
    private BigDecimal machineCapacity = BigDecimal.ONE;

    @Column(name = "labor_capacity", nullable = false, precision = 10, scale = 2)
    private BigDecimal laborCapacity = BigDecimal.ONE;

    @Column(name = "hourly_machine_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyMachineRate = BigDecimal.ZERO;

    @Column(name = "hourly_labor_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal hourlyLaborRate = BigDecimal.ZERO;

    @Column(name = "overhead_rate", nullable = false, precision = 18, scale = 4)
    private BigDecimal overheadRate = BigDecimal.ZERO;

    @Column(name = "overhead_rate_type", nullable = false, length = 30)
    private String overheadRateType = "MACHINE_HOUR"; // MACHINE_HOUR, LABOR_HOUR, DIRECT_LABOR_COST

    @Column(name = "queue_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal queueTimeHours = BigDecimal.ZERO;

    @Column(name = "move_time_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal moveTimeHours = BigDecimal.ZERO;

    @Column(name = "efficiency_factor", nullable = false, precision = 7, scale = 4)
    private BigDecimal efficiencyFactor = new BigDecimal("100.00");

    @Column(name = "gl_account_id")
    private Long glAccountId;

    @Column(nullable = false)
    private Boolean active = Boolean.TRUE;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public WorkCenter() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public String getWorkCenterType() { return workCenterType; }
    public void setWorkCenterType(String workCenterType) { this.workCenterType = workCenterType; }
    public BigDecimal getMachineCapacity() { return machineCapacity; }
    public void setMachineCapacity(BigDecimal machineCapacity) { this.machineCapacity = machineCapacity; }
    public BigDecimal getLaborCapacity() { return laborCapacity; }
    public void setLaborCapacity(BigDecimal laborCapacity) { this.laborCapacity = laborCapacity; }
    public BigDecimal getHourlyMachineRate() { return hourlyMachineRate; }
    public void setHourlyMachineRate(BigDecimal hourlyMachineRate) { this.hourlyMachineRate = hourlyMachineRate; }
    public BigDecimal getHourlyLaborRate() { return hourlyLaborRate; }
    public void setHourlyLaborRate(BigDecimal hourlyLaborRate) { this.hourlyLaborRate = hourlyLaborRate; }
    public BigDecimal getOverheadRate() { return overheadRate; }
    public void setOverheadRate(BigDecimal overheadRate) { this.overheadRate = overheadRate; }
    public String getOverheadRateType() { return overheadRateType; }
    public void setOverheadRateType(String overheadRateType) { this.overheadRateType = overheadRateType; }
    public BigDecimal getQueueTimeHours() { return queueTimeHours; }
    public void setQueueTimeHours(BigDecimal queueTimeHours) { this.queueTimeHours = queueTimeHours; }
    public BigDecimal getMoveTimeHours() { return moveTimeHours; }
    public void setMoveTimeHours(BigDecimal moveTimeHours) { this.moveTimeHours = moveTimeHours; }
    public BigDecimal getEfficiencyFactor() { return efficiencyFactor; }
    public void setEfficiencyFactor(BigDecimal efficiencyFactor) { this.efficiencyFactor = efficiencyFactor; }
    public Long getGlAccountId() { return glAccountId; }
    public void setGlAccountId(Long glAccountId) { this.glAccountId = glAccountId; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
