/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : BomHeader.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomHeaderController
 * Related Service   : BomHeaderService, BomHeaderServiceImpl
 * Related Repository: BomHeaderRepository
 * Related Entity    : BomHeader
 * Related DTO       : N/A
 * Related Mapper    : BomHeaderMapper
 * Related DB Table  : bom_headers
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : BomHeaderRepository, BomHeaderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bom_headers'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.manufacturing.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.inventory.entity.UnitOfMeasure;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code BomHeader}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bom_headers'.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_headers}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bom_headers")
public class BomHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "bom_number", nullable = false, length = 50)
    private String bomNumber;

    @Column(length = 255)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "bom_type", nullable = false, length = 30)
    private BomType bomType = BomType.MANUFACTURING;

    @Column(nullable = false, length = 20)
    private String revision = "00";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private BomStatus status = BomStatus.DRAFT;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "base_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal baseQuantity = BigDecimal.ONE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "base_unit_id", nullable = false)
    private UnitOfMeasure baseUnit;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @OneToMany(mappedBy = "bomHeader", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("sortSequence ASC, lineNumber ASC")
    private List<BomLine> lines = new ArrayList<>();

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

    public BomHeader() {}

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
     * Retrieves bom number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBomNumber() { return bomNumber; }
    /**
     * Performs the setBomNumber operation in this module.
     *
     * @param bomNumber the bomNumber input value
     */
    public void setBomNumber(String bomNumber) { this.bomNumber = bomNumber; }
    /**
     * Retrieves description data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getDescription() { return description; }
    /**
     * Performs the setDescription operation in this module.
     *
     * @param description the description input value
     */
    public void setDescription(String description) { this.description = description; }
    /**
     * Retrieves bom type data from the database.
     *
     * @return the BomType result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomType getBomType() { return bomType; }
    /**
     * Performs the setBomType operation in this module.
     *
     * @param bomType the bomType input value
     */
    public void setBomType(BomType bomType) { this.bomType = bomType; }
    /**
     * Retrieves revision data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRevision() { return revision; }
    /**
     * Performs the setRevision operation in this module.
     *
     * @param revision the revision input value
     */
    public void setRevision(String revision) { this.revision = revision; }
    /**
     * Retrieves status data from the database.
     *
     * @return the BomStatus result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BomStatus getStatus() { return status; }
    /**
     * Performs the setStatus operation in this module.
     *
     * @param status status filter for narrowing query results
     */
    public void setStatus(BomStatus status) { this.status = status; }
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
     * Retrieves base quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBaseQuantity() { return baseQuantity; }
    /**
     * Performs the setBaseQuantity operation in this module.
     *
     * @param baseQuantity the baseQuantity input value
     */
    public void setBaseQuantity(BigDecimal baseQuantity) { this.baseQuantity = baseQuantity; }
    /**
     * Retrieves base unit data from the database.
     *
     * @return the UnitOfMeasure result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public UnitOfMeasure getBaseUnit() { return baseUnit; }
    /**
     * Performs the setBaseUnit operation in this module.
     *
     * @param baseUnit the baseUnit input value
     */
    public void setBaseUnit(UnitOfMeasure baseUnit) { this.baseUnit = baseUnit; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves approved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; }
    /**
     * Performs the setApprovedAt operation in this module.
     *
     * @param approvedAt the approvedAt input value
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    /**
     * Retrieves lines data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<BomLine> getLines() { return lines; }
    /**
     * Performs the setLines operation in this module.
     *
     * @param lines the lines input value
     */
    public void setLines(List<BomLine> lines) { this.lines = lines; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
    /**
     * Retrieves updated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}