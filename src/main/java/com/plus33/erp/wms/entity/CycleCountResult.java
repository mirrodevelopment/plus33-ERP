package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cycle_count_results")
public class CycleCountResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CycleCountTask task;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private WarehouseLocation location;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "system_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal systemQuantity;

    @Column(name = "counted_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal countedQuantity;

    /** DB GENERATED: counted_quantity - system_quantity */
    @Column(name = "variance_quantity", insertable = false, updatable = false, precision = 18, scale = 6)
    private BigDecimal varianceQuantity;

    @Column(name = "variance_pct", precision = 10, scale = 4)
    private BigDecimal variancePct;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "unit_cost", precision = 18, scale = 6)
    private BigDecimal unitCost;

    @Column(name = "variance_value", precision = 18, scale = 2)
    private BigDecimal varianceValue;

    @Column(name = "count_number", nullable = false)
    private int countNumber = 1;

    @Column(nullable = false, length = 30)
    private String status = "PENDING_APPROVAL";
    // PENDING_APPROVAL, AUTO_APPROVED, APPROVED, REJECTED, RECOUNT_REQUESTED

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "gl_journal_id")
    private Long glJournalId;

    @Column(name = "counted_by")
    private Long countedBy;

    @Column(name = "counted_at", nullable = false, updatable = false)
    private LocalDateTime countedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public CycleCountTask getTask() { return task; }
    public void setTask(CycleCountTask task) { this.task = task; }
    public Long getPlanId() { return planId; }
    public void setPlanId(Long planId) { this.planId = planId; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public WarehouseLocation getLocation() { return location; }
    public void setLocation(WarehouseLocation location) { this.location = location; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public BigDecimal getSystemQuantity() { return systemQuantity; }
    public void setSystemQuantity(BigDecimal systemQuantity) { this.systemQuantity = systemQuantity; }
    public BigDecimal getCountedQuantity() { return countedQuantity; }
    public void setCountedQuantity(BigDecimal countedQuantity) { this.countedQuantity = countedQuantity; }
    public BigDecimal getVarianceQuantity() { return varianceQuantity; }
    public BigDecimal getVariancePct() { return variancePct; }
    public void setVariancePct(BigDecimal variancePct) { this.variancePct = variancePct; }
    public Long getUnitId() { return unitId; }
    public void setUnitId(Long unitId) { this.unitId = unitId; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public BigDecimal getVarianceValue() { return varianceValue; }
    public void setVarianceValue(BigDecimal varianceValue) { this.varianceValue = varianceValue; }
    public int getCountNumber() { return countNumber; }
    public void setCountNumber(int countNumber) { this.countNumber = countNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getApprovalNotes() { return approvalNotes; }
    public void setApprovalNotes(String approvalNotes) { this.approvalNotes = approvalNotes; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public Long getGlJournalId() { return glJournalId; }
    public void setGlJournalId(Long glJournalId) { this.glJournalId = glJournalId; }
    public Long getCountedBy() { return countedBy; }
    public void setCountedBy(Long countedBy) { this.countedBy = countedBy; }
    public LocalDateTime getCountedAt() { return countedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
