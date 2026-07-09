/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : ProductionScrap.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionScrapController
 * Related Service   : ProductionScrapService, ProductionScrapServiceImpl
 * Related Repository: ProductionScrapRepository
 * Related Entity    : ProductionScrap
 * Related DTO       : N/A
 * Related Mapper    : ProductionScrapMapper
 * Related DB Table  : production_scrap
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : ProductionScrapRepository, ProductionScrapMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'production_scrap'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code ProductionScrap}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'production_scrap'.</p>
 *
 * <p><b>Database Table   :</b> {@code production_scrap}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "production_scrap")
public class ProductionScrap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_id", nullable = false)
    private ProductionOrder productionOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "production_order_operation_id")
    private ProductionOrderOperation productionOrderOperation;

    @Column(name = "scrap_number", nullable = false, length = 50)
    private String scrapNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "scrap_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal scrapQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "defect_code", length = 50)
    private String defectCode;

    @Column(name = "defect_description", columnDefinition = "TEXT")
    private String defectDescription;

    @Column(name = "scrap_disposition", nullable = false, length = 30)
    private String scrapDisposition = "DISCARD"; // DISCARD, REWORK, SALVAGE, RETURN_TO_SUPPLIER

    @Column(name = "unit_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal unitCost = BigDecimal.ZERO;

    @Column(name = "total_scrap_cost", nullable = false, precision = 22, scale = 6)
    private BigDecimal totalScrapCost = BigDecimal.ZERO;

    @Column(name = "journal_entry_id")
    private Long journalEntryId;

    @Column(name = "recorded_by", nullable = false)
    private Long recordedBy;

    @Column(name = "recorded_at", nullable = false)
    private LocalDateTime recordedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ProductionScrap() {}

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
     * Retrieves production order operation data from the database.
     *
     * @return the ProductionOrderOperation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public ProductionOrderOperation getProductionOrderOperation() { return productionOrderOperation; }
    /**
     * Performs the setProductionOrderOperation operation in this module.
     *
     * @param poo the poo input value
     */
    public void setProductionOrderOperation(ProductionOrderOperation poo) { this.productionOrderOperation = poo; }
    /**
     * Retrieves scrap number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScrapNumber() { return scrapNumber; }
    /**
     * Performs the setScrapNumber operation in this module.
     *
     * @param scrapNumber the scrapNumber input value
     */
    public void setScrapNumber(String scrapNumber) { this.scrapNumber = scrapNumber; }
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
     * Retrieves scrap quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrapQuantity() { return scrapQuantity; }
    /**
     * Performs the setScrapQuantity operation in this module.
     *
     * @param scrapQuantity the scrapQuantity input value
     */
    public void setScrapQuantity(BigDecimal scrapQuantity) { this.scrapQuantity = scrapQuantity; }
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
     * Retrieves defect code data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefectCode() { return defectCode; }
    /**
     * Performs the setDefectCode operation in this module.
     *
     * @param defectCode the defectCode input value
     */
    public void setDefectCode(String defectCode) { this.defectCode = defectCode; }
    /**
     * Retrieves defect description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDefectDescription() { return defectDescription; }
    /**
     * Performs the setDefectDescription operation in this module.
     *
     * @param defectDescription the defectDescription input value
     */
    public void setDefectDescription(String defectDescription) { this.defectDescription = defectDescription; }
    /**
     * Retrieves scrap disposition data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getScrapDisposition() { return scrapDisposition; }
    /**
     * Performs the setScrapDisposition operation in this module.
     *
     * @param scrapDisposition the scrapDisposition input value
     */
    public void setScrapDisposition(String scrapDisposition) { this.scrapDisposition = scrapDisposition; }
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
     * Retrieves total scrap cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalScrapCost() { return totalScrapCost; }
    /**
     * Performs the setTotalScrapCost operation in this module.
     *
     * @param totalScrapCost the totalScrapCost input value
     */
    public void setTotalScrapCost(BigDecimal totalScrapCost) { this.totalScrapCost = totalScrapCost; }
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
     * Retrieves recorded by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRecordedBy() { return recordedBy; }
    /**
     * Performs the setRecordedBy operation in this module.
     *
     * @param recordedBy the recordedBy input value
     */
    public void setRecordedBy(Long recordedBy) { this.recordedBy = recordedBy; }
    /**
     * Retrieves recorded at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecordedAt() { return recordedAt; }
    /**
     * Performs the setRecordedAt operation in this module.
     *
     * @param recordedAt the recordedAt input value
     */
    public void setRecordedAt(LocalDateTime recordedAt) { this.recordedAt = recordedAt; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}