package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "equipment_assets")
public class EquipmentAsset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "asset_code", nullable = false, unique = true, length = 50)
    private String assetCode;

    @Column(name = "equipment_type", nullable = false, length = 30)
    private String equipmentType;

    @Column(name = "battery_level_pct")
    private Integer batteryLevelPct = 100;

    @Column(nullable = false, length = 30)
    private String status = "AVAILABLE";

    @Column(name = "last_maintenance_at")
    private LocalDateTime lastMaintenanceAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getAssetCode() { return assetCode; }
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }
    public String getEquipmentType() { return equipmentType; }
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
    public Integer getBatteryLevelPct() { return batteryLevelPct; }
    public void setBatteryLevelPct(Integer batteryLevelPct) { this.batteryLevelPct = batteryLevelPct; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getLastMaintenanceAt() { return lastMaintenanceAt; }
    public void setLastMaintenanceAt(LocalDateTime lastMaintenanceAt) { this.lastMaintenanceAt = lastMaintenanceAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
