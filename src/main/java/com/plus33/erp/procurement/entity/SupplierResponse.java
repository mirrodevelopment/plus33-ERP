/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : SupplierResponse.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : SupplierResponse
 * Related DTO       : SupplierResponse
 * Related Mapper    : SupplierMapper
 * Related DB Table  : procurement_supplier_responses
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierRepository, SupplierMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_supplier_responses'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_supplier_responses'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_supplier_responses}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_supplier_responses")
public class SupplierResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "rfq_version_id", nullable = false)
    private Long rfqVersionId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "bid_amount", nullable = false)
    private BigDecimal bidAmount;

    @Column(name = "received_at", nullable = false, updatable = false)
    private LocalDateTime receivedAt = LocalDateTime.now();

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
     * Retrieves rfq version id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getRfqVersionId() { return rfqVersionId; }
    /**
     * Performs the setRfqVersionId operation in this module.
     *
     * @param rfqVersionId the rfqVersionId input value
     */
    public void setRfqVersionId(Long rfqVersionId) { this.rfqVersionId = rfqVersionId; }
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
     * Retrieves bid amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getBidAmount() { return bidAmount; }
    /**
     * Performs the setBidAmount operation in this module.
     *
     * @param bidAmount the bidAmount input value
     */
    public void setBidAmount(BigDecimal bidAmount) { this.bidAmount = bidAmount; }
    /**
     * Retrieves received at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReceivedAt() { return receivedAt; }
}