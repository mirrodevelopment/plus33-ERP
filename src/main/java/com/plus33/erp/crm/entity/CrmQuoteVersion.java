/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.entity
 * File              : CrmQuoteVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CrmQuoteVersionController
 * Related Service   : CrmQuoteVersionService, CrmQuoteVersionServiceImpl
 * Related Repository: CrmQuoteVersionRepository
 * Related Entity    : CrmQuoteVersion
 * Related DTO       : N/A
 * Related Mapper    : CrmQuoteVersionMapper
 * Related DB Table  : crm_quote_versions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CrmQuoteVersionRepository, CrmQuoteVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'crm_quote_versions'. Defines persistent domain object for Crm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.crm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Crm Module</b>
 *
 * <p><b>Class  :</b> {@code CrmQuoteVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.crm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'crm_quote_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code crm_quote_versions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "crm_quote_versions")
public class CrmQuoteVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "quote_id", nullable = false)
    private Long quoteId;

    @Column(name = "version_number", nullable = false)
    private int versionNumber = 1;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "discount_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Column(name = "tax_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(nullable = false)
    private boolean locked = false;

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
     * Retrieves quote id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getQuoteId() { return quoteId; }
    /**
     * Performs the setQuoteId operation in this module.
     *
     * @param quoteId the quoteId input value
     */
    public void setQuoteId(Long quoteId) { this.quoteId = quoteId; }
    /**
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(int versionNumber) { this.versionNumber = versionNumber; }
    /**
     * Retrieves subtotal data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSubtotal() { return subtotal; }
    /**
     * Performs the setSubtotal operation in this module.
     *
     * @param subtotal the subtotal input value
     */
    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
    /**
     * Retrieves discount amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getDiscountAmount() { return discountAmount; }
    /**
     * Performs the setDiscountAmount operation in this module.
     *
     * @param discountAmount the discountAmount input value
     */
    public void setDiscountAmount(BigDecimal discountAmount) { this.discountAmount = discountAmount; }
    /**
     * Retrieves tax amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTaxAmount() { return taxAmount; }
    /**
     * Performs the setTaxAmount operation in this module.
     *
     * @param taxAmount the taxAmount input value
     */
    public void setTaxAmount(BigDecimal taxAmount) { this.taxAmount = taxAmount; }
    /**
     * Retrieves total amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getTotalAmount() { return totalAmount; }
    /**
     * Performs the setTotalAmount operation in this module.
     *
     * @param totalAmount the totalAmount input value
     */
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
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
     * Performs the isLocked operation in this module.
     *
     * @return true if operation succeeded, false otherwise
     */
    public boolean isLocked() { return locked; }
    /**
     * Performs the setLocked operation in this module.
     *
     * @param locked the locked input value
     */
    public void setLocked(boolean locked) { this.locked = locked; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}