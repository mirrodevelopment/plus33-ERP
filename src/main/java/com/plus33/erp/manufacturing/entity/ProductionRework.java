package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "production_rework")
public class ProductionRework {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "original_production_order_id", nullable = false)
    private ProductionOrder originalProductionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rework_production_order_id")
    private ProductionOrder reworkProductionOrder;

    @Column(name = "rework_number", nullable = false, length = 50)
    private String reworkNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "rework_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal reworkQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "rework_reason", columnDefinition = "TEXT")
    private String reworkReason;

    @Column(name = "rework_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal reworkCost = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "OPEN"; // OPEN, IN_PROGRESS, COMPLETED, SCRAPPED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionRework() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getOriginalProductionOrder() { return originalProductionOrder; }
    public void setOriginalProductionOrder(ProductionOrder originalProductionOrder) { this.originalProductionOrder = originalProductionOrder; }
    public ProductionOrder getReworkProductionOrder() { return reworkProductionOrder; }
    public void setReworkProductionOrder(ProductionOrder reworkProductionOrder) { this.reworkProductionOrder = reworkProductionOrder; }
    public String getReworkNumber() { return reworkNumber; }
    public void setReworkNumber(String reworkNumber) { this.reworkNumber = reworkNumber; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public BigDecimal getReworkQuantity() { return reworkQuantity; }
    public void setReworkQuantity(BigDecimal reworkQuantity) { this.reworkQuantity = reworkQuantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public String getReworkReason() { return reworkReason; }
    public void setReworkReason(String reworkReason) { this.reworkReason = reworkReason; }
    public BigDecimal getReworkCost() { return reworkCost; }
    public void setReworkCost(BigDecimal reworkCost) { this.reworkCost = reworkCost; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
