package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "waves",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "wave_number"}))
public class Wave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "wave_number", nullable = false, length = 50)
    private String waveNumber;

    @Column(name = "wave_type", nullable = false, length = 30)
    private String waveType = "STANDARD";
    // STANDARD, BATCH, CLUSTER, ZONE, EMERGENCY

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";
    // DRAFT, RELEASED, PICKING, PARTIALLY_PICKED, PICKED, PACKING, SHIPPED, CLOSED, CANCELLED

    @Column(name = "picking_strategy", nullable = false, length = 50)
    private String pickingStrategy = "FEFO";

    @Column(nullable = false)
    private int priority = 5;

    @Column(name = "planned_date")
    private LocalDate plannedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "wave", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<PickingWork> pickingWorkList = new java.util.ArrayList<>();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Long getWarehouseId() { return warehouseId; }
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    public String getWaveNumber() { return waveNumber; }
    public void setWaveNumber(String waveNumber) { this.waveNumber = waveNumber; }
    public String getWaveType() { return waveType; }
    public void setWaveType(String waveType) { this.waveType = waveType; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getPickingStrategy() { return pickingStrategy; }
    public void setPickingStrategy(String pickingStrategy) { this.pickingStrategy = pickingStrategy; }
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    public LocalDate getPlannedDate() { return plannedDate; }
    public void setPlannedDate(LocalDate plannedDate) { this.plannedDate = plannedDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
    public LocalDateTime getReleasedAt() { return releasedAt; }
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    public java.util.List<PickingWork> getPickingWorkList() { return pickingWorkList; }
    public void setPickingWorkList(java.util.List<PickingWork> pickingWorkList) { this.pickingWorkList = pickingWorkList; }
}
