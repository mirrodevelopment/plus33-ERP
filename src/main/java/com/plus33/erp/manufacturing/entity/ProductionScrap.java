package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_scrap")
public class ProductionScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_operation_id")
    private ProductionOrderOperation productionOrderOperation;

    @Column(name = "scrap_number", nullable = false, length = 50)
    private String scrapNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "scrap_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrapQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "defect_code", length = 50)
    private String defectCode;

    @Column(name = "defect_description", columnDefinition = "TEXT")
    private String defectDescription;

    @Column(name = "scrap_disposition", nullable = false, length = 30)
    private String scrapDisposition = "DISCARD"; // DISCARD, REWORK, SALVAGE, RETURN_TO_SUPPLIER

    @Column(name = "unit_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_scrap_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal totalScrapCost = BigDecimal.ZERO;

    @Column(name = "journal_entry_id")
    private Long journalEntryId;

    @Column(name = "recorded_by", nullable = false)
    private Long recordedBy;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionScrap() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public ProductionOrderOperation getProductionOrderOperation() { return productionOrderOperation; }
    public void setProductionOrderOperation(ProductionOrderOperation poo) { this.productionOrderOperation = poo; }
    public String getScrapNumber() { return scrapNumber; }
    public void setScrapNumber(String scrapNumber) { this.scrapNumber = scrapNumber; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public BigDecimal getScrapQuantity() { return scrapQuantity; }
    public void setScrapQuantity(BigDecimal scrapQuantity) { this.scrapQuantity = scrapQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public String getDefectCode() { return defectCode; }
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
    public String getDefectDescription() { return defectDescription; }
    public void setDefectDescription(String defectDescription) { this.defectDescription = defectDescription; }
    public String getScrapDisposition() { return scrapDisposition; }
    public void setScrapDisposition(String scrapDisposition) { this.scrapDisposition = scrapDisposition; }
    public BigDecimal getUnitCost() { return unitCost; }
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    public BigDecimal getTotalScrapCost() { return totalScrapCost; }
    public void setTotalScrapCost(BigDecimal totalScrapCost) { this.totalScrapCost = totalScrapCost; }
    public Long getJournalEntryId() { return journalEntryId; }
    public void setJournalEntryId(Long journalEntryId) { this.journalEntryId = journalEntryId; }
    public Long getRecordedBy() { return recordedBy; }
    public void setRecordedBy(Long recordedBy) { this.recordedBy = recordedBy; }
    public LocalDateTime getRecordedAt() { return recordedAt; }
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
