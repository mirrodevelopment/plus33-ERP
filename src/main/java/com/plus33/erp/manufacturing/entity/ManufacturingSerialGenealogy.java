/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ManufacturingSerialGenealogy.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingSerialGenealogyController
 * Related Service   : ManufacturingSerialGenealogyService, ManufacturingSerialGenealogyServiceImpl
 * Related Repository: ManufacturingSerialGenealogyRepository
 * Related Entity    : ManufacturingSerialGenealogy
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingSerialGenealogyMapper
 * Related DB Table  : manufacturing_serial_genealogy
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ManufacturingSerialGenealogyRepository, ManufacturingSerialGenealogyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'manufacturing_serial_genealogy'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingSerialGenealogy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'manufacturing_serial_genealogy'.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_serial_genealogy}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves parent serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParentSerialNumber() { return parentSerialNumber; }
    /**
     * Performs the setParentSerialNumber operation in this module.
     *
     * @param parentSerialNumber the parentSerialNumber input value
     */
    public void setParentSerialNumber(String parentSerialNumber) { this.parentSerialNumber = parentSerialNumber; }
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
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}