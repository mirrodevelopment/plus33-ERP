package com.plus33.erp.manufacturing.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "capacity_plans")
public class CapacityPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mrp_run_id", nullable = false)
    private MrpRun mrpRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_center_id", nullable = false)
    private WorkCenter workCenter;

    @Column(name = "planning_date", nullable = false)
    private LocalDate planningDate;

    @Column(name = "available_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal availableHours = BigDecimal.ZERO;

    @Column(name = "required_hours", nullable = false, precision = 10, scale = 2)
    private BigDecimal requiredHours = BigDecimal.ZERO;

    @Column(nullable = false)
    private Boolean overloaded = false;

    @Column(name = "machine_utilization_pct", precision = 7, scale = 4)
    private BigDecimal machineUtilizationPct;

    @Column(name = "labor_utilization_pct", precision = 7, scale = 4)
    private BigDecimal laborUtilizationPct;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public CapacityPlan() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public MrpRun getMrpRun() { return mrpRun; }
    public void setMrpRun(MrpRun mrpRun) { this.mrpRun = mrpRun; }
    public WorkCenter getWorkCenter() { return workCenter; }
    public void setWorkCenter(WorkCenter workCenter) { this.workCenter = workCenter; }
    public LocalDate getPlanningDate() { return planningDate; }
    public void setPlanningDate(LocalDate planningDate) { this.planningDate = planningDate; }
    public BigDecimal getAvailableHours() { return availableHours; }
    public void setAvailableHours(BigDecimal availableHours) { this.availableHours = availableHours; }
    public BigDecimal getRequiredHours() { return requiredHours; }
    public void setRequiredHours(BigDecimal requiredHours) { this.requiredHours = requiredHours; }
    public Boolean getOverloaded() { return overloaded; }
    public void setOverloaded(Boolean overloaded) { this.overloaded = overloaded; }
    public BigDecimal getMachineUtilizationPct() { return machineUtilizationPct; }
    public void setMachineUtilizationPct(BigDecimal machineUtilizationPct) { this.machineUtilizationPct = machineUtilizationPct; }
    public BigDecimal getLaborUtilizationPct() { return laborUtilizationPct; }
    public void setLaborUtilizationPct(BigDecimal laborUtilizationPct) { this.laborUtilizationPct = laborUtilizationPct; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
