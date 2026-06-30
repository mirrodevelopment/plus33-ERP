package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "replenishment_plans",
       uniqueConstraints = @UniqueConstraint(columnNames = {"warehouse_id", "product_id", "to_location_id"}))
public class ReplenishmentPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "from_zone_id")
    private Long fromZoneId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    /** MIN_MAX, KANBAN, FORWARD_PICK, DEMAND_BASED, MRP_DRIVEN */
    @Column(nullable = false, length = 30)
    private String strategy = "MIN_MAX";

    @Column(name = "min_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal minQuantity = BigDecimal.ZERO;

    @Column(name = "max_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal maxQuantity = BigDecimal.ZERO;

    @Column(name = "replenish_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal replenishQuantity = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(nullable = false)
    private boolean active = true;

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
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getFromZoneId() { return fromZoneId; }
    public void setFromZoneId(Long fromZoneId) { this.fromZoneId = fromZoneId; }
    public WarehouseLocation getToLocation() { return toLocation; }
    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }
    public String getStrategy() { return strategy; }
    public void setStrategy(String strategy) { this.strategy = strategy; }
    public BigDecimal getMinQuantity() { return minQuantity; }
    public void setMinQuantity(BigDecimal minQuantity) { this.minQuantity = minQuantity; }
    public BigDecimal getMaxQuantity() { return maxQuantity; }
    public void setMaxQuantity(BigDecimal maxQuantity) { this.maxQuantity = maxQuantity; }
    public BigDecimal getReplenishQuantity() { return replenishQuantity; }
    public void setReplenishQuantity(BigDecimal replenishQuantity) { this.replenishQuantity = replenishQuantity; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
