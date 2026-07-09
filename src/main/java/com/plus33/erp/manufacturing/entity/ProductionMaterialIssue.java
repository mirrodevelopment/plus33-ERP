/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionMaterialIssue.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionMaterialIssueController
 * Related Service   : ProductionMaterialIssueService, ProductionMaterialIssueServiceImpl
 * Related Repository: ProductionMaterialIssueRepository
 * Related Entity    : ProductionMaterialIssue
 * Related DTO       : N/A
 * Related Mapper    : ProductionMaterialIssueMapper
 * Related DB Table  : production_material_issues
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ProductionMaterialIssueRepository, ProductionMaterialIssueMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_material_issues'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code ProductionMaterialIssue}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_material_issues'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_material_issues}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "production_material_issues")
public class ProductionMaterialIssue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_line_id")
    private BomLine bomLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_product_id", nullable = false)
    private Product componentProduct;

    @Column(name = "issue_type", nullable = false, length = 30)
    private String issueType = "ISSUE"; // ISSUE, RETURN, SCRAP, BACKFLUSH

    @Column(name = "issue_number", nullable = false, length = 50)
    private String issueNumber;

    @Column(name = "issued_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal issuedQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "lot_number", length = 100)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "stock_movement_id")
    private Long stockMovementId;

    @Column(name = "unit_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal totalCost = BigDecimal.ZERO;

    @Column(name = "journal_entry_id")
    private Long journalEntryId;

    @Column(name = "issued_by", nullable = false)
    private Long issuedBy;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt = LocalDateTime.now();

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionMaterialIssue() {}

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
     * Retrieves bom line data from the database.
     *
     * @return the BomLine result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomLine getBomLine() { return bomLine; }
    /**
     * Performs the setBomLine operation in this module.
     *
     * @param bomLine the bomLine input value
     */
    public void setBomLine(BomLine bomLine) { this.bomLine = bomLine; }
    /**
     * Retrieves component product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getComponentProduct() { return componentProduct; }
    /**
     * Performs the setComponentProduct operation in this module.
     *
     * @param componentProduct the componentProduct input value
     */
    public void setComponentProduct(Product componentProduct) { this.componentProduct = componentProduct; }
    /**
     * Retrieves issue type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIssueType() { return issueType; }
    /**
     * Performs the setIssueType operation in this module.
     *
     * @param issueType the issueType input value
     */
    public void setIssueType(String issueType) { this.issueType = issueType; }
    /**
     * Retrieves issue number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIssueNumber() { return issueNumber; }
    /**
     * Performs the setIssueNumber operation in this module.
     *
     * @param issueNumber the issueNumber input value
     */
    public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }
    /**
     * Retrieves issued quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getIssuedQuantity() { return issuedQuantity; }
    /**
     * Performs the setIssuedQuantity operation in this module.
     *
     * @param issuedQuantity the issuedQuantity input value
     */
    public void setIssuedQuantity(BigDecimal issuedQuantity) { this.issuedQuantity = issuedQuantity; }
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
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    /**
     * Retrieves stock movement id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getStockMovementId() { return stockMovementId; }
    /**
     * Performs the setStockMovementId operation in this module.
     *
     * @param stockMovementId the stockMovementId input value
     */
    public void setStockMovementId(Long stockMovementId) { this.stockMovementId = stockMovementId; }
    /**
     * Retrieves unit cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getUnitCost() { return unitCost; }
    /**
     * Performs the setUnitCost operation in this module.
     *
     * @param unitCost the unitCost input value
     */
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    /**
     * Retrieves total cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalCost() { return totalCost; }
    /**
     * Performs the setTotalCost operation in this module.
     *
     * @param totalCost the totalCost input value
     */
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }
    /**
     * Retrieves journal entry id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getJournalEntryId() { return journalEntryId; }
    /**
     * Performs the setJournalEntryId operation in this module.
     *
     * @param journalEntryId the journalEntryId input value
     */
    public void setJournalEntryId(Long journalEntryId) { this.journalEntryId = journalEntryId; }
    /**
     * Retrieves issued by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getIssuedBy() { return issuedBy; }
    /**
     * Performs the setIssuedBy operation in this module.
     *
     * @param issuedBy the issuedBy input value
     */
    public void setIssuedBy(Long issuedBy) { this.issuedBy = issuedBy; }
    /**
     * Retrieves issued at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getIssuedAt() { return issuedAt; }
    /**
     * Performs the setIssuedAt operation in this module.
     *
     * @param issuedAt the issuedAt input value
     */
    public void setIssuedAt(LocalDateTime issuedAt) { this.issuedAt = issuedAt; }
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