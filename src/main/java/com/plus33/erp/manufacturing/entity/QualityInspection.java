package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "quality_inspections")
public class QualityInspection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id")
    private ProductionOrder productionOrder;

    @Column(name = "inspection_number", nullable = false, length = 50)
    private String inspectionNumber;

    @Column(name = "inspection_type", nullable = false, length = 30)
    private String inspectionType; // INCOMING, IN_PROCESS, FINAL, RECEIVING

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private InspectionStatus status = InspectionStatus.PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "lot_number", length = 100)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "inspected_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal inspectedQuantity = BigDecimal.ZERO;

    @Column(name = "passed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal passedQuantity = BigDecimal.ZERO;

    @Column(name = "failed_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal failedQuantity = BigDecimal.ZERO;

    @Column(name = "sample_size", precision = 18, scale = 6)
    private BigDecimal sampleSize;

    @Column(name = "sampling_plan", length = 100)
    private String samplingPlan;

    @Column(name = "disposition", length = 30)
    private String disposition; // ACCEPT, REJECT, REWORK, CONDITIONAL_ACCEPT

    @Column(name = "hold_production", nullable = false)
    private Boolean holdProduction = false;

    @Column(name = "non_conformance_report", columnDefinition = "TEXT")
    private String nonConformanceReport;

    @Column(name = "corrective_action", columnDefinition = "TEXT")
    private String correctiveAction;

    @Column(name = "inspected_by")
    private Long inspectedBy;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "inspected_at")
    private LocalDateTime inspectedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public QualityInspection() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public String getInspectionNumber() { return inspectionNumber; }
    public void setInspectionNumber(String inspectionNumber) { this.inspectionNumber = inspectionNumber; }
    public String getInspectionType() { return inspectionType; }
    public void setInspectionType(String inspectionType) { this.inspectionType = inspectionType; }
    public InspectionStatus getStatus() { return status; }
    public void setStatus(InspectionStatus status) { this.status = status; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public BigDecimal getInspectedQuantity() { return inspectedQuantity; }
    public void setInspectedQuantity(BigDecimal inspectedQuantity) { this.inspectedQuantity = inspectedQuantity; }
    public BigDecimal getPassedQuantity() { return passedQuantity; }
    public void setPassedQuantity(BigDecimal passedQuantity) { this.passedQuantity = passedQuantity; }
    public BigDecimal getFailedQuantity() { return failedQuantity; }
    public void setFailedQuantity(BigDecimal failedQuantity) { this.failedQuantity = failedQuantity; }
    public BigDecimal getSampleSize() { return sampleSize; }
    public void setSampleSize(BigDecimal sampleSize) { this.sampleSize = sampleSize; }
    public String getSamplingPlan() { return samplingPlan; }
    public void setSamplingPlan(String samplingPlan) { this.samplingPlan = samplingPlan; }
    public String getDisposition() { return disposition; }
    public void setDisposition(String disposition) { this.disposition = disposition; }
    public Boolean getHoldProduction() { return holdProduction; }
    public void setHoldProduction(Boolean holdProduction) { this.holdProduction = holdProduction; }
    public String getNonConformanceReport() { return nonConformanceReport; }
    public void setNonConformanceReport(String nonConformanceReport) { this.nonConformanceReport = nonConformanceReport; }
    public String getCorrectiveAction() { return correctiveAction; }
    public void setCorrectiveAction(String correctiveAction) { this.correctiveAction = correctiveAction; }
    public Long getInspectedBy() { return inspectedBy; }
    public void setInspectedBy(Long inspectedBy) { this.inspectedBy = inspectedBy; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getInspectedAt() { return inspectedAt; }
    public void setInspectedAt(LocalDateTime inspectedAt) { this.inspectedAt = inspectedAt; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
