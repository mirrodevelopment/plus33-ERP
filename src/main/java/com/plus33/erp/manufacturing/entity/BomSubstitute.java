package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bom_substitutes")
public class BomSubstitute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_line_id", nullable = false)
    private BomLine bomLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_product_id", nullable = false)
    private Product substituteProduct;

    @Column(name = "substitute_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal substituteQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(nullable = false)
    private Integer priority = 1;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BomSubstitute() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public BomLine getBomLine() { return bomLine; }
    public void setBomLine(BomLine bomLine) { this.bomLine = bomLine; }
    public Product getSubstituteProduct() { return substituteProduct; }
    public void setSubstituteProduct(Product substituteProduct) { this.substituteProduct = substituteProduct; }
    public BigDecimal getSubstituteQuantity() { return substituteQuantity; }
    public void setSubstituteQuantity(BigDecimal substituteQuantity) { this.substituteQuantity = substituteQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public Integer getPriority() { return priority; }
    public void setPriority(Integer priority) { this.priority = priority; }
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    public LocalDate getEffectiveTo() { return effectiveTo; }
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
