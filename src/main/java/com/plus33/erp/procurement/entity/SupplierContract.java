/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : SupplierContract.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierContractController
 * Related Service   : SupplierContractService, SupplierContractServiceImpl
 * Related Repository: SupplierContractRepository
 * Related Entity    : SupplierContract
 * Related DTO       : N/A
 * Related Mapper    : SupplierContractMapper
 * Related DB Table  : procurement_supplier_contracts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierContractRepository, SupplierContractMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_supplier_contracts'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierContract}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_supplier_contracts'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_supplier_contracts}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_supplier_contracts")
public class SupplierContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "supplier_id", nullable = false)
    private Long supplierId;

    @Column(name = "contract_number", nullable = false, unique = true, length = 50)
    private String contractNumber;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber = 1;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

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
     * Retrieves contract number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getContractNumber() { return contractNumber; }
    /**
     * Performs the setContractNumber operation in this module.
     *
     * @param contractNumber the contractNumber input value
     */
    public void setContractNumber(String contractNumber) { this.contractNumber = contractNumber; }
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
     * Retrieves version number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getVersionNumber() { return versionNumber; }
    /**
     * Performs the setVersionNumber operation in this module.
     *
     * @param versionNumber the versionNumber input value
     */
    public void setVersionNumber(Integer versionNumber) { this.versionNumber = versionNumber; }
    /**
     * Retrieves start date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getStartDate() { return startDate; }
    /**
     * Performs the setStartDate operation in this module.
     *
     * @param startDate inclusive start date for date-range filtering
     */
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    /**
     * Retrieves end date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getEndDate() { return endDate; }
    /**
     * Performs the setEndDate operation in this module.
     *
     * @param endDate inclusive end date for date-range filtering
     */
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}