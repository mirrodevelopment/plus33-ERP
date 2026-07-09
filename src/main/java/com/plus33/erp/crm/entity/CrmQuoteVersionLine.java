/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmQuoteVersionLine.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteVersionLineController
 * Related Service   : CrmQuoteVersionLineService, CrmQuoteVersionLineServiceImpl
 * Related Repository: CrmQuoteVersionLineRepository
 * Related Entity    : CrmQuoteVersionLine
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteVersionLineMapper
 * Related DB Table  : crm_quote_version_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteVersionLineRepository, CrmQuoteVersionLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_quote_version_lines'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteVersionLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_quote_version_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quote_version_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_quote_version_lines")
public class CrmQuoteVersionLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_version_id", nullable = false)
    private Long quoteVersionId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false, precision = 18, scale = 6)
    private BigDecimal quantity;

    @Column(name = "unit_price", nullable = false, precision = 18, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "discount_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountPercentage = BigDecimal.ZERO;

    @Column(name = "tax_percentage", nullable = false, precision = 5, scale = 2)
    private BigDecimal taxPercentage = BigDecimal.ZERO;

    @Column(name = "line_total", nullable = false, precision = 18, scale = 2)
    private BigDecimal lineTotal;

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
     * Retrieves quote version id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getQuoteVersionId() { return quoteVersionId; }
    /**
     * Performs the setQuoteVersionId operation in this module.
     *
     * @param quoteVersionId the quoteVersionId input value
     */
    public void setQuoteVersionId(Long quoteVersionId) { this.quoteVersionId = quoteVersionId; }
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
     * Retrieves unit price data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getUnitPrice() { return unitPrice; }
    /**
     * Performs the setUnitPrice operation in this module.
     *
     * @param unitPrice the unitPrice input value
     */
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    /**
     * Retrieves discount percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    /**
     * Performs the setDiscountPercentage operation in this module.
     *
     * @param discountPercentage the discountPercentage input value
     */
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    /**
     * Retrieves tax percentage data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTaxPercentage() { return taxPercentage; }
    /**
     * Performs the setTaxPercentage operation in this module.
     *
     * @param taxPercentage the taxPercentage input value
     */
    public void setTaxPercentage(BigDecimal taxPercentage) { this.taxPercentage = taxPercentage; }
    /**
     * Retrieves line total data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLineTotal() { return lineTotal; }
    /**
     * Performs the setLineTotal operation in this module.
     *
     * @param lineTotal the lineTotal input value
     */
    public void setLineTotal(BigDecimal lineTotal) { this.lineTotal = lineTotal; }
}