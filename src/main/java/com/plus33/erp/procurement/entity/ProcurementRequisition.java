/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : ProcurementRequisition.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementRequisitionController
 * Related Service   : ProcurementRequisitionService, ProcurementRequisitionServiceImpl
 * Related Repository: ProcurementRequisitionRepository
 * Related Entity    : ProcurementRequisition
 * Related DTO       : N/A
 * Related Mapper    : ProcurementRequisitionMapper
 * Related DB Table  : procurement_requisitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementRequisitionRepository, ProcurementRequisitionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_requisitions'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementRequisition}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_requisitions'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_requisitions}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_requisitions")
public class ProcurementRequisition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "requisition_number", nullable = false, unique = true, length = 50)
    private String requisitionNumber;

    @Column(nullable = false, length = 30)
    private String status = "REQUISITION_DRAFT";

    @Column(name = "total_amount", nullable = false)
    private BigDecimal totalAmount = BigDecimal.ZERO;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

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
     * Retrieves requisition number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRequisitionNumber() { return requisitionNumber; }
    /**
     * Performs the setRequisitionNumber operation in this module.
     *
     * @param requisitionNumber the requisitionNumber input value
     */
    public void setRequisitionNumber(String requisitionNumber) { this.requisitionNumber = requisitionNumber; }
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
     * Retrieves created by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCreatedBy() { return createdBy; }
    /**
     * Performs the setCreatedBy operation in this module.
     *
     * @param createdBy the createdBy input value
     */
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }
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
    /**
     * Performs the setUpdatedAt operation in this module.
     *
     * @param updatedAt the updatedAt input value
     */
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}