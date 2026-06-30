package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "manufacturing_serial_genealogy")
public class ManufacturingSerialGenealogy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "serial_number", nullable = false, length = 100)
    private String serialNumber;

    @Column(name = "parent_serial_number", length = 100)
    private String parentSerialNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_product_id")
    private Product parentProduct;

    @Column(name = "genealogy_type", nullable = false, length = 20)
    private String genealogyType = "CHILD"; // PARENT, CHILD, SIBLING

    @Column(name = "produced_at", nullable = false)
    private LocalDateTime producedAt = LocalDateTime.now();

    @Column(name = "lot_number", length = 100)
    private String lotNumber;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ManufacturingSerialGenealogy() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public ProductionOrder getProductionOrder() { return productionOrder; }
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
    public String getSerialNumber() { return serialNumber; }
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    public String getParentSerialNumber() { return parentSerialNumber; }
    public void setParentSerialNumber(String parentSerialNumber) { this.parentSerialNumber = parentSerialNumber; }
    public Product getParentProduct() { return parentProduct; }
    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
    public String getGenealogyType() { return genealogyType; }
    public void setGenealogyType(String genealogyType) { this.genealogyType = genealogyType; }
    public LocalDateTime getProducedAt() { return producedAt; }
    public void setProducedAt(LocalDateTime producedAt) { this.producedAt = producedAt; }
    public String getLotNumber() { return lotNumber; }
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
