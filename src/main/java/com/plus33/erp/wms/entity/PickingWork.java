package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "picking_work")
public class PickingWork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wave_id", nullable = false)
    private Wave wave;

    @Column(name = "source_type", nullable = false, length = 30)
    private String sourceType = "SALES_ORDER";
    // SALES_ORDER, PRODUCTION_ORDER, TRANSFER, SERVICE_ORDER

    @Column(name = "source_id")
    private Long sourceId;

    @Column(name = "source_line_id")
    private Long sourceLineId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "pick_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal pickQuantity;

    @Column(name = "picked_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal pickedQuantity = BigDecimal.ZERO;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_location_id")
    private WarehouseLocation fromLocation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_location_id")
    private WarehouseLocation toLocation;

    @Column(name = "strategy_used", length = 50)
    private String strategyUsed;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, ASSIGNED, IN_PROGRESS, PARTIALLY_PICKED, COMPLETED, CANCELLED, SHORTED

    @Column(name = "assigned_to")
    private Long assignedTo;

    /**
     * Optimistic lock — detects concurrent picking of the same work record.
     * Hibernate will throw OptimisticLockException if two pickers race on the same row.
     */
    @Version
    @Column(nullable = false)
    private Long version = 0L;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

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
    public Wave getWave() { return wave; }
    public void setWave(Wave wave) { this.wave = wave; }
    public String getSourceType() { return sourceType; }
    public void setSourceType(String sourceType) { this.sourceType = sourceType; }
    public Long getSourceId() { return sourceId; }
    public void setSourceId(Long sourceId) { this.sourceId = sourceId; }
    public Long getSourceLineId() { return sourceLineId; }
    public void setSourceLineId(Long sourceLineId) { this.sourceLineId = sourceLineId; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public BigDecimal getPickQuantity() { return pickQuantity; }
    public void setPickQuantity(BigDecimal pickQuantity) { this.pickQuantity = pickQuantity; }
    public BigDecimal getPickedQuantity() { return pickedQuantity; }
    public void setPickedQuantity(BigDecimal pickedQuantity) { this.pickedQuantity = pickedQuantity; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public WarehouseLocation getFromLocation() { return fromLocation; }
    public void setFromLocation(WarehouseLocation fromLocation) { this.fromLocation = fromLocation; }
    public WarehouseLocation getToLocation() { return toLocation; }
    public void setToLocation(WarehouseLocation toLocation) { this.toLocation = toLocation; }
    public String getStrategyUsed() { return strategyUsed; }
    public void setStrategyUsed(String strategyUsed) { this.strategyUsed = strategyUsed; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Long getAssignedTo() { return assignedTo; }
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
    public Long getVersion() { return version; }
    public void setVersion(Long version) { this.version = version; }
    public LocalDateTime getStartedAt() { return startedAt; }
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
