/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : EquipmentAsset.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EquipmentAssetController
 * Related Service   : EquipmentAssetService, EquipmentAssetServiceImpl
 * Related Repository: EquipmentAssetRepository
 * Related Entity    : EquipmentAsset
 * Related DTO       : N/A
 * Related Mapper    : EquipmentAssetMapper
 * Related DB Table  : equipment_assets
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EquipmentAssetRepository, EquipmentAssetMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'equipment_assets'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code EquipmentAsset}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'equipment_assets'.</p>
 *
 * <p><b>Database Table   :</b> {@code equipment_assets}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    /**
     * Retrieves asset code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAssetCode() { return assetCode; }
    /**
     * Performs the setAssetCode operation in this module.
     *
     * @param assetCode the assetCode input value
     */
    public void setAssetCode(String assetCode) { this.assetCode = assetCode; }
    /**
     * Retrieves equipment type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getEquipmentType() { return equipmentType; }
    /**
     * Performs the setEquipmentType operation in this module.
     *
     * @param equipmentType the equipmentType input value
     */
    public void setEquipmentType(String equipmentType) { this.equipmentType = equipmentType; }
    /**
     * Retrieves battery level pct data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getBatteryLevelPct() { return batteryLevelPct; }
    /**
     * Performs the setBatteryLevelPct operation in this module.
     *
     * @param batteryLevelPct the batteryLevelPct input value
     */
    public void setBatteryLevelPct(Integer batteryLevelPct) { this.batteryLevelPct = batteryLevelPct; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves last maintenance at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getLastMaintenanceAt() { return lastMaintenanceAt; }
    /**
     * Performs the setLastMaintenanceAt operation in this module.
     *
     * @param lastMaintenanceAt the lastMaintenanceAt input value
     */
    public void setLastMaintenanceAt(LocalDateTime lastMaintenanceAt) { this.lastMaintenanceAt = lastMaintenanceAt; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}