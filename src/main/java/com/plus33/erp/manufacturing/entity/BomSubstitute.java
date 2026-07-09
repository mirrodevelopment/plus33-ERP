/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.entity
 * File              : BomSubstitute.java
 * Purpose           : JPA Entity representing a persistent database record in Manufacturing Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BomSubstituteController
 * Related Service   : BomSubstituteService, BomSubstituteServiceImpl
 * Related Repository: BomSubstituteRepository
 * Related Entity    : BomSubstitute
 * Related DTO       : N/A
 * Related Mapper    : BomSubstituteMapper
 * Related DB Table  : bom_substitutes
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : BomSubstituteRepository, BomSubstituteMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'bom_substitutes'. Defines persistent domain object for Manufacturing Module with validation, relationship mappings, and lifecycle callbacks.
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
 * <p><b>Class  :</b> {@code BomSubstitute}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'bom_substitutes'.</p>
 *
 * <p><b>Database Table   :</b> {@code bom_substitutes}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "bom_substitutes")
public class BomSubstitute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bom_line_id", nullable = false)
    private BomLine bomLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substitute_product_id", nullable = false)
    private Product substituteProduct;

    @Column(name = "substitute_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal substituteQuantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(nullable = false)
    private Integer priority = 1;

    @Column(name = "effective_from")
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public BomSubstitute() {}

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
     * Retrieves substitute product data from the database.
     *
     * @return the Product result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Product getSubstituteProduct() { return substituteProduct; }
    /**
     * Performs the setSubstituteProduct operation in this module.
     *
     * @param substituteProduct the substituteProduct input value
     */
    public void setSubstituteProduct(Product substituteProduct) { this.substituteProduct = substituteProduct; }
    /**
     * Retrieves substitute quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSubstituteQuantity() { return substituteQuantity; }
    /**
     * Performs the setSubstituteQuantity operation in this module.
     *
     * @param substituteQuantity the substituteQuantity input value
     */
    public void setSubstituteQuantity(BigDecimal substituteQuantity) { this.substituteQuantity = substituteQuantity; }
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
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(Integer priority) { this.priority = priority; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}