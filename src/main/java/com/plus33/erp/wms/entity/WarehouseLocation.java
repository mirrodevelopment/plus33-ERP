package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "warehouse_locations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "location_code"}))
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

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public WarehouseZone getZone() { return zone; }
    public void setZone(WarehouseZone zone) { this.zone = zone; }
    public String getLocationCode() { return locationCode; }
    public void setLocationCode(String locationCode) { this.locationCode = locationCode; }
    public String getAisle() { return aisle; }
    public void setAisle(String aisle) { this.aisle = aisle; }
    public String getRack() { return rack; }
    public void setRack(String rack) { this.rack = rack; }
    public String getShelf() { return shelf; }
    public void setShelf(String shelf) { this.shelf = shelf; }
    public String getBin() { return bin; }
    public void setBin(String bin) { this.bin = bin; }
    public String getLocationType() { return locationType; }
    public void setLocationType(String locationType) { this.locationType = locationType; }
    public int getPickSequence() { return pickSequence; }
    public void setPickSequence(int pickSequence) { this.pickSequence = pickSequence; }
    public BigDecimal getMaxWeightKg() { return maxWeightKg; }
    public void setMaxWeightKg(BigDecimal maxWeightKg) { this.maxWeightKg = maxWeightKg; }
    public BigDecimal getMaxVolumeM3() { return maxVolumeM3; }
    public void setMaxVolumeM3(BigDecimal maxVolumeM3) { this.maxVolumeM3 = maxVolumeM3; }
    public int getMaxPallets() { return maxPallets; }
    public void setMaxPallets(int maxPallets) { this.maxPallets = maxPallets; }
    public String getVelocityClass() { return velocityClass; }
    public void setVelocityClass(String velocityClass) { this.velocityClass = velocityClass; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public boolean isPickable() { return isPickable; }
    public void setPickable(boolean pickable) { isPickable = pickable; }
    public boolean isReceivable() { return isReceivable; }
    public void setReceivable(boolean receivable) { isReceivable = receivable; }
    public boolean isMixedLot() { return isMixedLot; }
    public void setMixedLot(boolean mixedLot) { isMixedLot = mixedLot; }
    public boolean isMixedSku() { return isMixedSku; }
    public void setMixedSku(boolean mixedSku) { isMixedSku = mixedSku; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
