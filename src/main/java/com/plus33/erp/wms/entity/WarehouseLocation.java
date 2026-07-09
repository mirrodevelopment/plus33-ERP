/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseLocation.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLocationController
 * Related Service   : WarehouseLocationService, WarehouseLocationServiceImpl
 * Related Repository: WarehouseLocationRepository
 * Related Entity    : WarehouseLocation
 * Related DTO       : N/A
 * Related Mapper    : WarehouseLocationMapper
 * Related DB Table  : warehouse_locations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseLocationRepository, WarehouseLocationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_locations'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_locations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "location_code"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLocation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_locations'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_locations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class WarehouseLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private WarehouseZone zone;

    @Column(name = "location_code", nullable = false, length = 50)
    private String locationCode;

    @Column(length = 10)
    private String aisle;

    @Column(length = 10)
    private String rack;

    @Column(length = 10)
    private String shelf;

    @Column(length = 10)
    private String bin;

    @Column(name = "location_type", nullable = false, length = 30)
    private String locationType = "STANDARD";
    // STANDARD, BULK, FLOOR, RACK, MEZZANINE, STAGING, DOCK, VIRTUAL, CROSS_DOCK

    @Column(name = "pick_sequence", nullable = false)
    private int pickSequence = 0;

    @Column(name = "max_weight_kg", precision = 12, scale = 3)
    private BigDecimal maxWeightKg;

    @Column(name = "max_volume_m3", precision = 12, scale = 6)
    private BigDecimal maxVolumeM3;

    @Column(name = "max_pallets", nullable = false)
    private int maxPallets = 1;

    @Column(name = "velocity_class", nullable = false, length = 5)
    private String velocityClass = "C"; // A, B, C

    @Column(nullable = false)
    private boolean active = true;

    @Column(name = "is_pickable", nullable = false)
    private boolean isPickable = true;

    @Column(name = "is_receivable", nullable = false)
    private boolean isReceivable = true;

    @Column(name = "is_mixed_lot", nullable = false)
    private boolean isMixedLot = false;

    @Column(name = "is_mixed_sku", nullable = false)
    private boolean isMixedSku = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves zone data from the database.
     *
     * @return the WarehouseZone result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseZone getZone() { return zone; }
    /**
     * Performs the setZone operation in this module.
     *
     * @param zone the zone input value
     */
    public void setZone(WarehouseZone zone) { this.zone = zone; }
    /**
     * Retrieves location code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLocationCode() { return locationCode; }
    /**
     * Performs the setLocationCode operation in this module.
     *
     * @param locationCode the locationCode input value
     */
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    /**
     * Retrieves aisle data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getAisle() { return aisle; }
    /**
     * Performs the setAisle operation in this module.
     *
     * @param aisle the aisle input value
     */
    public void setAisle(String aisle) { this.aisle = aisle; }
    /**
     * Retrieves rack data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRack() { return rack; }
    /**
     * Performs the setRack operation in this module.
     *
     * @param rack the rack input value
     */
    public void setRack(String rack) { this.rack = rack; }
    /**
     * Retrieves shelf data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getShelf() { return shelf; }
    /**
     * Performs the setShelf operation in this module.
     *
     * @param shelf the shelf input value
     */
    public void setShelf(String shelf) { this.shelf = shelf; }
    /**
     * Retrieves bin data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBin() { return bin; }
    /**
     * Performs the setBin operation in this module.
     *
     * @param bin the bin input value
     */
    public void setBin(String bin) { this.bin = bin; }
    /**
     * Retrieves location type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLocationType() { return locationType; }
    /**
     * Performs the setLocationType operation in this module.
     *
     * @param locationType the locationType input value
     */
    public void setLocationType(String locationType) { this.locationType = locationType; }
    /**
     * Retrieves pick sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPickSequence() { return pickSequence; }
    /**
     * Performs the setPickSequence operation in this module.
     *
     * @param pickSequence the pickSequence input value
     */
    public void setPickSequence(int pickSequence) { this.pickSequence = pickSequence; }
    /**
     * Retrieves max weight kg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxWeightKg() { return maxWeightKg; }
    /**
     * Performs the setMaxWeightKg operation in this module.
     *
     * @param maxWeightKg the maxWeightKg input value
     */
    public void setMaxWeightKg(BigDecimal maxWeightKg) { this.maxWeightKg = maxWeightKg; }
    /**
     * Retrieves max volume m3 data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getMaxVolumeM3() { return maxVolumeM3; }
    /**
     * Performs the setMaxVolumeM3 operation in this module.
     *
     * @param maxVolumeM3 the maxVolumeM3 input value
     */
    public void setMaxVolumeM3(BigDecimal maxVolumeM3) { this.maxVolumeM3 = maxVolumeM3; }
    /**
     * Retrieves a paginated list of max pallets records.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getMaxPallets() { return maxPallets; }
    /**
     * Performs the setMaxPallets operation in this module.
     *
     * @param maxPallets the maxPallets input value
     */
    public void setMaxPallets(int maxPallets) { this.maxPallets = maxPallets; }
    /**
     * Retrieves velocity class data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getVelocityClass() { return velocityClass; }
    /**
     * Performs the setVelocityClass operation in this module.
     *
     * @param velocityClass the velocityClass input value
     */
    public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
    /**
     * Performs the isActive operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(boolean active) { this.active = active; }
    /**
     * Performs the isPickable operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isPickable() { return isPickable; }
    /**
     * Performs the setPickable operation in this module.
     *
     * @param pickable the pickable input value
     */
    public void setPickable(boolean pickable) { isPickable = pickable; }
    /**
     * Performs the isReceivable operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isReceivable() { return isReceivable; }
    /**
     * Performs the setReceivable operation in this module.
     *
     * @param receivable the receivable input value
     */
    public void setReceivable(boolean receivable) { isReceivable = receivable; }
    /**
     * Performs the isMixedLot operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isMixedLot() { return isMixedLot; }
    /**
     * Performs the setMixedLot operation in this module.
     *
     * @param mixedLot the mixedLot input value
     */
    public void setMixedLot(boolean mixedLot) { isMixedLot = mixedLot; }
    /**
     * Performs the isMixedSku operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isMixedSku() { return isMixedSku; }
    /**
     * Performs the setMixedSku operation in this module.
     *
     * @param mixedSku the mixedSku input value
     */
    public void setMixedSku(boolean mixedSku) { isMixedSku = mixedSku; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}