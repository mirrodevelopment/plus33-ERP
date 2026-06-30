package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "advance_shipping_notices",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "asn_number"}))
public class AdvanceShippingNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "asn_number", nullable = false, length = 50)
    private String asnNumber;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "carrier_id")
    private Long carrierId;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, IN_TRANSIT, ARRIVED, RECEIVING, PARTIALLY_RECEIVED, RECEIVED, CANCELLED

    @Column(name = "expected_arrival")
    private LocalDate expectedArrival;

    @Column(name = "actual_arrival")
    private LocalDateTime actualArrival;

    @Column(name = "tracking_number", length = 100)
    private String trackingNumber;

    @Column(name = "po_reference", length = 100)
    private String poReference;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "dock_door_id")
    private Long dockDoorId;

    @Column(name = "checkin_id")
    private Long checkinId;

    @Column(name = "received_by")
    private Long receivedBy;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "asn", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<AsnLine> lines = new ArrayList<>();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getAsnNumber() { return asnNumber; }
    public void setAsnNumber(String asnNumber) { this.asnNumber = asnNumber; }
    public Long getSupplierId() { return supplierId; }
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    public Long getCarrierId() { return carrierId; }
    public void setCarrierId(Long carrierId) { this.carrierId = carrierId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDate getExpectedArrival() { return expectedArrival; }
    public void setExpectedArrival(LocalDate expectedArrival) { this.expectedArrival = expectedArrival; }
    public LocalDateTime getActualArrival() { return actualArrival; }
    public void setActualArrival(LocalDateTime actualArrival) { this.actualArrival = actualArrival; }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String trackingNumber) { this.trackingNumber = trackingNumber; }
    public String getPoReference() { return poReference; }
    public void setPoReference(String poReference) { this.poReference = poReference; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getDockDoorId() { return dockDoorId; }
    public void setDockDoorId(Long dockDoorId) { this.dockDoorId = dockDoorId; }
    public Long getCheckinId() { return checkinId; }
    public void setCheckinId(Long checkinId) { this.checkinId = checkinId; }
    public Long getReceivedBy() { return receivedBy; }
    public void setReceivedBy(Long receivedBy) { this.receivedBy = receivedBy; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public List<AsnLine> getLines() { return lines; }
    public void setLines(List<AsnLine> lines) { this.lines = lines; }
}
