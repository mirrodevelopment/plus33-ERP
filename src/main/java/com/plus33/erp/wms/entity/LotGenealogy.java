/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : LotGenealogy.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LotGenealogyController
 * Related Service   : LotGenealogyService, LotGenealogyServiceImpl
 * Related Repository: LotGenealogyRepository
 * Related Entity    : LotGenealogy
 * Related DTO       : N/A
 * Related Mapper    : LotGenealogyMapper
 * Related DB Table  : lot_genealogy
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LotGenealogyRepository, LotGenealogyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'lot_genealogy'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code LotGenealogy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'lot_genealogy'.</p>
 *
 * <p><b>Database Table   :</b> {@code lot_genealogy}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
     * Retrieves company id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCompanyId() { return companyId; }
    /**
     * Performs the setCompanyId operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     */
    public void setCompanyId(Long companyId) { this.companyId = companyId; }
    /**
     * Retrieves parent lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getParentLotNumber() { return parentLotNumber; }
    /**
     * Performs the setParentLotNumber operation in this module.
     *
     * @param parentLotNumber the parentLotNumber input value
     */
    public void setParentLotNumber(String parentLotNumber) { this.parentLotNumber = parentLotNumber; }
    /**
     * Retrieves child lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getChildLotNumber() { return childLotNumber; }
    /**
     * Performs the setChildLotNumber operation in this module.
     *
     * @param childLotNumber the childLotNumber input value
     */
    public void setChildLotNumber(String childLotNumber) { this.childLotNumber = childLotNumber; }
    /**
     * Retrieves product id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductId() { return productId; }
    /**
     * Performs the setProductId operation in this module.
     *
     * @param productId the productId input value
     */
    public void setProductId(Long productId) { this.productId = productId; }
    /**
     * Retrieves production order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getProductionOrderId() { return productionOrderId; }
    /**
     * Performs the setProductionOrderId operation in this module.
     *
     * @param productionOrderId the productionOrderId input value
     */
    public void setProductionOrderId(Long productionOrderId) { this.productionOrderId = productionOrderId; }
    /**
     * Retrieves shipment id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getShipmentId() { return shipmentId; }
    /**
     * Performs the setShipmentId operation in this module.
     *
     * @param shipmentId the shipmentId input value
     */
    public void setShipmentId(Long shipmentId) { this.shipmentId = shipmentId; }
    /**
     * Retrieves customer return id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCustomerReturnId() { return customerReturnId; }
    /**
     * Performs the setCustomerReturnId operation in this module.
     *
     * @param customerReturnId the customerReturnId input value
     */
    public void setCustomerReturnId(Long customerReturnId) { this.customerReturnId = customerReturnId; }
    /**
     * Retrieves transformation type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTransformationType() { return transformationType; }
    /**
     * Performs the setTransformationType operation in this module.
     *
     * @param transformationType the transformationType input value
     */
    public void setTransformationType(String transformationType) { this.transformationType = transformationType; }
    /**
     * Retrieves split ratio data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSplitRatio() { return splitRatio; }
    /**
     * Performs the setSplitRatio operation in this module.
     *
     * @param splitRatio the splitRatio input value
     */
    public void setSplitRatio(BigDecimal splitRatio) { this.splitRatio = splitRatio; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}