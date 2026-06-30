package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bom_headers")
public class BomHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "bom_number", nullable = false, length = 50)
    private String bomNumber;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "bom_type", nullable = false, length = 30)
    private BomType bomType = BomType.MANUFACTURING;

    @Column(nullable = false, length = 20)
    private String revision = "00";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BomStatus status = BomStatus.DRAFT;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal baseQuantity = BigDecimal.ONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_unit_id", nullable = false)
    private UnitOfMeasure baseUnit;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "bomHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortSequence ASC, lineNumber ASC")
    private List<BomLine> lines = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public BomHeader() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getBomNumber() { return bomNumber; }
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public BomType getBomType() { return bomType; }
    public void setBomType(BomType bomType) { this.bomType = bomType; }
    public String getRevision() { return revision; }
    public void setRevision(String revision) { this.revision = revision; }
    public BomStatus getStatus() { return status; }
    public void setStatus(BomStatus status) { this.status = status; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
    public UnitOfMeasure getBaseUnit() { return baseUnit; }
    public void setBaseUnit(UnitOfMeasure baseUnit) { this.baseUnit = baseUnit; }
    public Long getApprovedBy() { return approvedBy; }
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    public LocalDateTime getApprovedAt() { return approvedAt; }
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    public List<BomLine> getLines() { return lines; }
    public void setLines(List<BomLine> lines) { this.lines = lines; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
