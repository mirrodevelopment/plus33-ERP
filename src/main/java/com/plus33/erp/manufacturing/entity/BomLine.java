/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : BomLine.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomLineController
 * Related Service   : BomLineService, BomLineServiceImpl
 * Related Repository: BomLineRepository
 * Related Entity    : BomLine
 * Related DTO       : N/A
 * Related Mapper    : BomLineMapper
 * Related DB Table  : bom_lines
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : BomLineRepository, BomLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bom_lines'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code BomLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bom_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_lines}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bom_lines")
public class BomLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_header_id", nullable = false)
    private BomHeader bomHeader;

    @Column(name = "line_number", nullable = false)
    private Integer lineNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "component_product_id", nullable = false)
    private Product componentProduct;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "component_type", nullable = false, length = 30)
    private String componentType = "MATERIAL"; // MATERIAL, PHANTOM, BY_PRODUCT, CO_PRODUCT, REFERENCE

    @Column(name = "scrap_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal scrapPercentage = BigDecimal.ZERO;

    @Column(name = "yield_percentage", nullable = false, precision = 7, scale = 4)
    private BigDecimal yieldPercentage = new BigDecimal("100.00");

    @Column(nullable = false)
    private Boolean backflush = Boolean.FALSE;

    @Column(name = "reference_designator", length = 100)
    private String referenceDesignator;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "issue_method", nullable = false, length = 30)
    private String issueMethod = "MANUAL"; // MANUAL, BACKFLUSH, PRE_ISSUE

    @Column(name = "sort_sequence", nullable = false)
    private Integer sortSequence = 10;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BomLine() {}

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
     * Retrieves bom header data from the database.
     *
     * @return the BomHeader result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomHeader getBomHeader() { return bomHeader; }
    /**
     * Performs the setBomHeader operation in this module.
     *
     * @param bomHeader the bomHeader input value
     */
    public void setBomHeader(BomHeader bomHeader) { this.bomHeader = bomHeader; }
    /**
     * Retrieves line number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getLineNumber() { return lineNumber; }
    /**
     * Performs the setLineNumber operation in this module.
     *
     * @param lineNumber the lineNumber input value
     */
    public void setLineNumber(Integer lineNumber) { this.lineNumber = lineNumber; }
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
     * Retrieves component type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getComponentType() { return componentType; }
    /**
     * Performs the setComponentType operation in this module.
     *
     * @param componentType the componentType input value
     */
    public void setComponentType(String componentType) { this.componentType = componentType; }
    /**
     * Retrieves scrap percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getScrapPercentage() { return scrapPercentage; }
    /**
     * Performs the setScrapPercentage operation in this module.
     *
     * @param scrapPercentage the scrapPercentage input value
     */
    public void setScrapPercentage(BigDecimal scrapPercentage) { this.scrapPercentage = scrapPercentage; }
    /**
     * Retrieves yield percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getYieldPercentage() { return yieldPercentage; }
    /**
     * Performs the setYieldPercentage operation in this module.
     *
     * @param yieldPercentage the yieldPercentage input value
     */
    public void setYieldPercentage(BigDecimal yieldPercentage) { this.yieldPercentage = yieldPercentage; }
    /**
     * Retrieves backflush data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getBackflush() { return backflush; }
    /**
     * Performs the setBackflush operation in this module.
     *
     * @param backflush the backflush input value
     */
    public void setBackflush(Boolean backflush) { this.backflush = backflush; }
    /**
     * Retrieves reference designator data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getReferenceDesignator() { return referenceDesignator; }
    /**
     * Performs the setReferenceDesignator operation in this module.
     *
     * @param referenceDesignator the referenceDesignator input value
     */
    public void setReferenceDesignator(String referenceDesignator) { this.referenceDesignator = referenceDesignator; }
    /**
     * Retrieves effective from data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveFrom() { return effectiveFrom; }
    /**
     * Performs the setEffectiveFrom operation in this module.
     *
     * @param effectiveFrom the effectiveFrom input value
     */
    public void setEffectiveFrom(LocalDate effectiveFrom) { this.effectiveFrom = effectiveFrom; }
    /**
     * Retrieves effective to data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEffectiveTo() { return effectiveTo; }
    /**
     * Performs the setEffectiveTo operation in this module.
     *
     * @param effectiveTo the effectiveTo input value
     */
    public void setEffectiveTo(LocalDate effectiveTo) { this.effectiveTo = effectiveTo; }
    /**
     * Retrieves issue method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getIssueMethod() { return issueMethod; }
    /**
     * Performs the setIssueMethod operation in this module.
     *
     * @param issueMethod the issueMethod input value
     */
    public void setIssueMethod(String issueMethod) { this.issueMethod = issueMethod; }
    /**
     * Retrieves sort sequence data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getSortSequence() { return sortSequence; }
    /**
     * Performs the setSortSequence operation in this module.
     *
     * @param sortSequence the sortSequence input value
     */
    public void setSortSequence(Integer sortSequence) { this.sortSequence = sortSequence; }
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