/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingBatchGenealogy.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingBatchGenealogyController
 * Related Service   : ManufacturingBatchGenealogyService, ManufacturingBatchGenealogyServiceImpl
 * Related Repository: ManufacturingBatchGenealogyRepository
 * Related Entity    : ManufacturingBatchGenealogy
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingBatchGenealogyMapper
 * Related DB Table  : manufacturing_batch_genealogy
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ManufacturingBatchGenealogyRepository, ManufacturingBatchGenealogyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_batch_genealogy'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingBatchGenealogy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_batch_genealogy'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_batch_genealogy}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves production order data from the database.
     *
     * @return the ProductionOrder result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrder getProductionOrder() { return productionOrder; }
    /**
     * Performs the setProductionOrder operation in this module.
     *
     * @param productionOrder the productionOrder input value
     */
    public void setProductionOrder(ProductionOrder productionOrder) { this.productionOrder = productionOrder; }
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
     * Retrieves batch number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBatchNumber() { return batchNumber; }
    /**
     * Performs the setBatchNumber operation in this module.
     *
     * @param batchNumber the batchNumber input value
     */
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    /**
     * Retrieves parent batch number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParentBatchNumber() { return parentBatchNumber; }
    /**
     * Performs the setParentBatchNumber operation in this module.
     *
     * @param parentBatchNumber the parentBatchNumber input value
     */
    public void setParentBatchNumber(String parentBatchNumber) { this.parentBatchNumber = parentBatchNumber; }
    /**
     * Retrieves parent product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getParentProduct() { return parentProduct; }
    /**
     * Performs the setParentProduct operation in this module.
     *
     * @param parentProduct the parentProduct input value
     */
    public void setParentProduct(Product parentProduct) { this.parentProduct = parentProduct; }
    /**
     * Retrieves quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQuantity() { return quantity; }
    /**
     * Performs the setQuantity operation in this module.
     *
     * @param quantity the quantity input value
     */
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }
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
     * Retrieves genealogy type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getGenealogyType() { return genealogyType; }
    /**
     * Performs the setGenealogyType operation in this module.
     *
     * @param genealogyType the genealogyType input value
     */
    public void setGenealogyType(String genealogyType) { this.genealogyType = genealogyType; }
    /**
     * Retrieves produced at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getProducedAt() { return producedAt; }
    /**
     * Performs the setProducedAt operation in this module.
     *
     * @param producedAt the producedAt input value
     */
    public void setProducedAt(LocalDateTime producedAt) { this.producedAt = producedAt; }
    /**
     * Retrieves expiry date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getExpiryDate() { return expiryDate; }
    /**
     * Performs the setExpiryDate operation in this module.
     *
     * @param expiryDate the expiryDate input value
     */
    public void setExpiryDate(LocalDate expiryDate) { this.expiryDate = expiryDate; }
    /**
     * Retrieves a paginated list of recall status records.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecallStatus() { return recallStatus; }
    /**
     * Performs the setRecallStatus operation in this module.
     *
     * @param recallStatus the recallStatus input value
     */
    public void setRecallStatus(String recallStatus) { this.recallStatus = recallStatus; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}