package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "shipments",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "shipment_number"}))
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "shipment_number", nullable = false, length = 50)
    private String shipmentNumber;

    @Column(name = "wave_id")
    private Long waveId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";
    // DRAFT, PACKED, LOADED, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED, CANCELLED

    @Column(name = "ship_method", length = 50)
    private String shipMethod;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "pro_number", length = 100)
    private String proNumber;

    @Column(name = "estimated_delivery")
    private LocalDate estimatedDelivery;

    @Column(name = "actual_delivery")
    private LocalDateTime actualDelivery;

    @Column(name = "total_weight_kg", precision = 12, scale = 3)
    private BigDecimal totalWeightKg;

    @Column(name = "total_volume_m3", precision = 12, scale = 6)
    private BigDecimal totalVolumeM3;

    @Column(name = "freight_cost", precision = 18, scale = 2)
    private BigDecimal freightCost;

    @Column(name = "freight_currency", nullable = false, length = 3)
    private String freightCurrency = "EUR";

    @Column(name = "pod_reference", length = 100)
    private String podReference;

    @Column(name = "pod_signed_by", length = 100)
    private String podSignedBy;

    @Column(name = "pod_at")
    private LocalDateTime podAt;

    @Column(name = "dock_door_id")
    private Long dockDoorId;

    @Column(name = "dispatched_by")
    private Long dispatchedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "shipment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<ShipmentLine> lines = new java.util.ArrayList<>();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getShipmentNumber() { return shipmentNumber; }
    public void setShipmentNumber(String shipmentNumber) { this.shipmentNumber = shipmentNumber; }
    public Long getWaveId() { return waveId; }
    public void setWaveId(Long waveId) { this.waveId = waveId; }
    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getShipMethod() { return shipMethod; }
    public void setShipMethod(String shipMethod) { this.shipMethod = shipMethod; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getProNumber() { return proNumber; }
    public void setProNumber(String proNumber) { this.proNumber = proNumber; }
    public LocalDate getEstimatedDelivery() { return estimatedDelivery; }
    public void setEstimatedDelivery(LocalDate estimatedDelivery) { this.estimatedDelivery = estimatedDelivery; }
    public LocalDateTime getActualDelivery() { return actualDelivery; }
    public void setActualDelivery(LocalDateTime actualDelivery) { this.actualDelivery = actualDelivery; }
    public BigDecimal getTotalWeightKg() { return totalWeightKg; }
    public void setTotalWeightKg(BigDecimal totalWeightKg) { this.totalWeightKg = totalWeightKg; }
    public BigDecimal getTotalVolumeM3() { return totalVolumeM3; }
    public void setTotalVolumeM3(BigDecimal totalVolumeM3) { this.totalVolumeM3 = totalVolumeM3; }
    public BigDecimal getFreightCost() { return freightCost; }
    public void setFreightCost(BigDecimal freightCost) { this.freightCost = freightCost; }
    public String getFreightCurrency() { return freightCurrency; }
    public void setFreightCurrency(String freightCurrency) { this.freightCurrency = freightCurrency; }
    public String getPodReference() { return podReference; }
    public void setPodReference(String podReference) { this.podReference = podReference; }
    public String getPodSignedBy() { return podSignedBy; }
    public void setPodSignedBy(String podSignedBy) { this.podSignedBy = podSignedBy; }
    public LocalDateTime getPodAt() { return podAt; }
    public void setPodAt(LocalDateTime podAt) { this.podAt = podAt; }
    public Long getDockDoorId() { return dockDoorId; }
    public void setDockDoorId(Long dockDoorId) { this.dockDoorId = dockDoorId; }
    public Long getDispatchedBy() { return dispatchedBy; }
    public void setDispatchedBy(Long dispatchedBy) { this.dispatchedBy = dispatchedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public java.util.List<ShipmentLine> getLines() { return lines; }
    public void setLines(java.util.List<ShipmentLine> lines) { this.lines = lines; }
}
