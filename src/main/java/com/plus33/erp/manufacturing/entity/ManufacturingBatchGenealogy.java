package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "manufacturing_batch_genealogy")
public class ManufacturingBatchGenealogy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "batch_number", nullable = false, length = 100)
    private String batchNumber;

    @Column(name = "parent_batch_number", length = 100)
    private String parentBatchNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "genealogy_type", nullable = false, length = 20)
    private String genealogyType = "OUTPUT"; // INPUT, OUTPUT, BY_PRODUCT

    @Column(name = "produced_at", nullable = false)
    private LocalDateTime producedAt = LocalDateTime.now();

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "recall_status", nullable = false, length = 20)
    private String recallStatus = "CLEAR"; // CLEAR, UNDER_REVIEW, RECALLED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingBatchGenealogy() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public String getParentBatchNumber() { return parentBatchNumber; }
    public void setParentBatchNumber(String parentBatchNumber) { this.parentBatchNumber = parentBatchNumber; }
    public Product getParentProduct() { return parentProduct; }
    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
    public UnitOfMeasure getUnit() { return unit; }
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    public String getGenealogyType() { return genealogyType; }
    public void setGenealogyType(String genealogyType) { this.genealogyType = genealogyType; }
    public LocalDateTime getProducedAt() { return producedAt; }
    public void setProducedAt(LocalDateTime producedAt) { this.producedAt = producedAt; }
    public LocalDate getExpiryDate() { return expiryDate; }
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    public String getRecallStatus() { return recallStatus; }
    public void setRecallStatus(String recallStatus) { this.recallStatus = recallStatus; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
