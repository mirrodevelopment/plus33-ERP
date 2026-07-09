/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : SupplierScorecard.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierScorecardController
 * Related Service   : SupplierScorecardService, SupplierScorecardServiceImpl
 * Related Repository: SupplierScorecardRepository
 * Related Entity    : SupplierScorecard
 * Related DTO       : N/A
 * Related Mapper    : SupplierScorecardMapper
 * Related DB Table  : procurement_supplier_scorecards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierScorecardRepository, SupplierScorecardMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_supplier_scorecards'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierScorecard}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_supplier_scorecards'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_supplier_scorecards}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_supplier_scorecards")
public class SupplierScorecard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_id", nullable = false, unique = true)
    private Long supplierId;

    @Column(name = "on_time_delivery_rate", nullable = false)
    private BigDecimal onTimeDeliveryRate = new BigDecimal("100.00");

    @Column(name = "quality_defect_rate", nullable = false)
    private BigDecimal qualityDefectRate = BigDecimal.ZERO;

    @Column(name = "invoice_accuracy_rate", nullable = false)
    private BigDecimal invoiceAccuracyRate = new BigDecimal("100.00");

    @Column(name = "overall_rating", nullable = false)
    private BigDecimal overallRating = new BigDecimal("100.00");

    @Column(name = "recalculated_at", nullable = false)
    private LocalDateTime recalculatedAt = LocalDateTime.now();

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
     * Retrieves supplier id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getSupplierId() { return supplierId; }
    /**
     * Performs the setSupplierId operation in this module.
     *
     * @param supplierId the supplierId input value
     */
    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }
    /**
     * Retrieves on time delivery rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOnTimeDeliveryRate() { return onTimeDeliveryRate; }
    /**
     * Performs the setOnTimeDeliveryRate operation in this module.
     *
     * @param onTimeDeliveryRate the onTimeDeliveryRate input value
     */
    public void setOnTimeDeliveryRate(BigDecimal onTimeDeliveryRate) { this.onTimeDeliveryRate = onTimeDeliveryRate; }
    /**
     * Retrieves quality defect rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getQualityDefectRate() { return qualityDefectRate; }
    /**
     * Performs the setQualityDefectRate operation in this module.
     *
     * @param qualityDefectRate the qualityDefectRate input value
     */
    public void setQualityDefectRate(BigDecimal qualityDefectRate) { this.qualityDefectRate = qualityDefectRate; }
    /**
     * Retrieves invoice accuracy rate data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getInvoiceAccuracyRate() { return invoiceAccuracyRate; }
    /**
     * Performs the setInvoiceAccuracyRate operation in this module.
     *
     * @param invoiceAccuracyRate the invoiceAccuracyRate input value
     */
    public void setInvoiceAccuracyRate(BigDecimal invoiceAccuracyRate) { this.invoiceAccuracyRate = invoiceAccuracyRate; }
    /**
     * Retrieves a paginated list of overall rating records.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getOverallRating() { return overallRating; }
    /**
     * Performs the setOverallRating operation in this module.
     *
     * @param overallRating the overallRating input value
     */
    public void setOverallRating(BigDecimal overallRating) { this.overallRating = overallRating; }
    /**
     * Retrieves recalculated at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getRecalculatedAt() { return recalculatedAt; }
    /**
     * Performs the setRecalculatedAt operation in this module.
     *
     * @param recalculatedAt the recalculatedAt input value
     */
    public void setRecalculatedAt(LocalDateTime recalculatedAt) { this.recalculatedAt = recalculatedAt; }
}