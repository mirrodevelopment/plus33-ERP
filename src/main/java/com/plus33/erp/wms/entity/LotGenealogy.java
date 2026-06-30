package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "lot_genealogy")
public class LotGenealogy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "parent_lot_number", nullable = false, length = 50)
    private String parentLotNumber;

    @Column(name = "child_lot_number", nullable = false, length = 50)
    private String childLotNumber;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "production_order_id")
    private Long productionOrderId;

    @Column(name = "shipment_id")
    private Long shipmentId;

    @Column(name = "customer_return_id")
    private Long customerReturnId;

    @Column(name = "transformation_type", nullable = false, length = 30)
    private String transformationType = "SPLIT";

    @Column(name = "split_ratio", precision = 18, scale = 6)
    private BigDecimal splitRatio;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getCompanyId() { return companyId; }
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    public String getParentLotNumber() { return parentLotNumber; }
    public void setParentLotNumber(String parentLotNumber) { this.parentLotNumber = parentLotNumber; }
    public String getChildLotNumber() { return childLotNumber; }
    public void setChildLotNumber(String childLotNumber) { this.childLotNumber = childLotNumber; }
    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }
    public Long getProductionOrderId() { return productionOrderId; }
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    public Long getShipmentId() { return shipmentId; }
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    public Long getCustomerReturnId() { return customerReturnId; }
    public void setCustomerReturnId(Long customerReturnId) { this.customerReturnId = customerReturnId; }
    public String getTransformationType() { return transformationType; }
    public void setTransformationType(String transformationType) { this.transformationType = transformationType; }
    public BigDecimal getSplitRatio() { return splitRatio; }
    public void setSplitRatio(BigDecimal splitRatio) { this.splitRatio = splitRatio; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
