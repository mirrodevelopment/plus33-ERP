package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bom_lines")
public class BomLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_header_id", nullable = false)
    private BomHeader bomHeader;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_product_id", nullable = false)
    private Product componentProduct;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "component_type", nullable = false, length = 30)
    private String componentType = "MATERIAL"; // MATERIAL, PHANTOM, BY_PRODUCT, CO_PRODUCT, REFERENCE

    @Column(name = "scrap_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal scrapPercentage = BigDecimal.ZERO;

    @Column(name = "yield_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal yieldPercentage = new BigDecimal("100.00");

    @Column(nullable = false)
    private Boolean backflush = Boolean.FALSE;

    @Column(name = "reference_designator", length = 100)
    private String referenceDesignator;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "issue_method", nullable = false, length = 30)
    private String issueMethod = "MANUAL"; // MANUAL, BACKFLUSH, PRE_ISSUE

    @Column(name = "sort_sequence", nullable = false)
    private Integer sortSequence = 10;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BomLine() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BomHeader getBomHeader() { return bomHeader; }
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    public Integer getLineNumber() { return lineNumber; }
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
    public Product getComponentProduct() { return componentProduct; }
    public void setComponentProduct(Product componentProduct) { this.componentProduct = componentProduct; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public String getComponentType() { return componentType; }
    public void setComponentType(String componentType) { this.componentType = componentType; }
    public BigDecimal getScrapPercentage() { return scrapPercentage; }
    public void setScrapPercentage(BigDecimal scrapPercentage) { this.scrapPercentage = scrapPercentage; }
    public BigDecimal getYieldPercentage() { return yieldPercentage; }
    public void setYieldPercentage(BigDecimal yieldPercentage) { this.yieldPercentage = yieldPercentage; }
    public Boolean getBackflush() { return backflush; }
    public void setBackflush(Boolean backflush) { this.backflush = backflush; }
    public String getReferenceDesignator() { return referenceDesignator; }
    public void setReferenceDesignator(String referenceDesignator) { this.referenceDesignator = referenceDesignator; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public String getIssueMethod() { return issueMethod; }
    public void setIssueMethod(String issueMethod) { this.issueMethod = issueMethod; }
    public Integer getSortSequence() { return sortSequence; }
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
