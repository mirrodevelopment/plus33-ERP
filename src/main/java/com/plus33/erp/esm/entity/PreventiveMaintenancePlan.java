package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "esm_pm_plans")
public class PreventiveMaintenancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "installed_asset_id", nullable = false)
    private Long installedAssetId;

    @Column(name = "interval_days", nullable = false)
    private Integer intervalDays;

    @Column(name = "next_service_date", nullable = false)
    private LocalDate nextServiceDate;

    @Column(name = "trigger_type", nullable = false, length = 30)
    private String triggerType = "CALENDAR";

    @Column(nullable = false)
    private Boolean active = true;

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getInstalledAssetId() { return installedAssetId; }
    public void setInstalledAssetId(Long installedAssetId) { this.installedAssetId = installedAssetId; }
    public Integer getIntervalDays() { return intervalDays; }
    public void setIntervalDays(Integer intervalDays) { this.intervalDays = intervalDays; }
    public LocalDate getNextServiceDate() { return nextServiceDate; }
    public void setNextServiceDate(LocalDate nextServiceDate) { this.nextServiceDate = nextServiceDate; }
    public String getTriggerType() { return triggerType; }
    public void setTriggerType(String triggerType) { this.triggerType = triggerType; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}
