/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionRework.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionReworkController
 * Related Service   : ProductionReworkService, ProductionReworkServiceImpl
 * Related Repository: ProductionReworkRepository
 * Related Entity    : ProductionRework
 * Related DTO       : N/A
 * Related Mapper    : ProductionReworkMapper
 * Related DB Table  : production_rework
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ProductionReworkRepository, ProductionReworkMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_rework'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionRework}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_rework'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_rework}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getId() { return id; }
    /**
     * Performs the setId operation in this module.
     *
     * @param id the unique database ID of the resource
     */
    public void setId(Long id) { this.id = id; }
    /**
     * Retrieves original production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getOriginalProductionOrder() { return originalProductionOrder; }
    /**
     * Performs the setOriginalProductionOrder operation in this module.
     *
     * @param originalProductionOrder the originalProductionOrder input value
     */
    public void setOriginalProductionOrder(ProductionOrder originalProductionOrder) { this.originalProductionOrder = originalProductionOrder; }
    /**
     * Retrieves rework production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getReworkProductionOrder() { return reworkProductionOrder; }
    /**
     * Performs the setReworkProductionOrder operation in this module.
     *
     * @param reworkProductionOrder the reworkProductionOrder input value
     */
    public void setReworkProductionOrder(ProductionOrder reworkProductionOrder) { this.reworkProductionOrder = reworkProductionOrder; }
    /**
     * Retrieves rework number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReworkNumber() { return reworkNumber; }
    /**
     * Performs the setReworkNumber operation in this module.
     *
     * @param reworkNumber the reworkNumber input value
     */
    public void setReworkNumber(String reworkNumber) { this.reworkNumber = reworkNumber; }
    /**
     * Retrieves product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getProduct() { return product; }
    /**
     * Performs the setProduct operation in this module.
     *
     * @param product the product input value
     */
    public void setProduct(Product product) { this.product = product; }
    /**
     * Retrieves rework quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReworkQuantity() { return reworkQuantity; }
    /**
     * Performs the setReworkQuantity operation in this module.
     *
     * @param reworkQuantity the reworkQuantity input value
     */
    public void setReworkQuantity(BigDecimal reworkQuantity) { this.reworkQuantity = reworkQuantity; }
    /**
     * Retrieves unit data from the database.
     *
     * @return the UnitOfMeasure result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public UnitOfMeasure getUnit() { return unit; }
    /**
     * Performs the setUnit operation in this module.
     *
     * @param unit the unit input value
     */
    public void setUnit(UnitOfMeasure unit) { this.unit = unit; }
    /**
     * Retrieves rework reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReworkReason() { return reworkReason; }
    /**
     * Performs the setReworkReason operation in this module.
     *
     * @param reworkReason the reworkReason input value
     */
    public void setReworkReason(String reworkReason) { this.reworkReason = reworkReason; }
    /**
     * Retrieves rework cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getReworkCost() { return reworkCost; }
    /**
     * Performs the setReworkCost operation in this module.
     *
     * @param reworkCost the reworkCost input value
     */
    public void setReworkCost(BigDecimal reworkCost) { this.reworkCost = reworkCost; }
    /**
     * Retrieves status data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(String status) { this.status = status; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}