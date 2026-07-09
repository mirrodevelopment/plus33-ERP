/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : SupplierQualification.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierQualificationController
 * Related Service   : SupplierQualificationService, SupplierQualificationServiceImpl
 * Related Repository: SupplierQualificationRepository
 * Related Entity    : SupplierQualification
 * Related DTO       : N/A
 * Related Mapper    : SupplierQualificationMapper
 * Related DB Table  : procurement_supplier_qualifications
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierQualificationRepository, SupplierQualificationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_supplier_qualifications'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierQualification}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_supplier_qualifications'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_supplier_qualifications}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_supplier_qualifications")
public class SupplierQualification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "supplier_id", nullable = false, unique = true)
    private Long supplierId;

    @Column(nullable = false, length = 30)
    private String status = "ONBOARDING";

    @Column(name = "risk_score_financial")
    private BigDecimal riskScoreFinancial = BigDecimal.ZERO;

    @Column(name = "risk_score_compliance")
    private BigDecimal riskScoreCompliance = BigDecimal.ZERO;

    @Column(name = "risk_score_esg")
    private BigDecimal riskScoreEsg = BigDecimal.ZERO;

    @Column(name = "consolidated_risk_level", nullable = false, length = 20)
    private String consolidatedRiskLevel = "LOW";

    @Column(name = "approved_vendor_list", nullable = false)
    private Boolean approvedVendorList = false;

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
     * Retrieves risk score financial data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRiskScoreFinancial() { return riskScoreFinancial; }
    /**
     * Performs the setRiskScoreFinancial operation in this module.
     *
     * @param riskScoreFinancial the riskScoreFinancial input value
     */
    public void setRiskScoreFinancial(BigDecimal riskScoreFinancial) { this.riskScoreFinancial = riskScoreFinancial; }
    /**
     * Retrieves risk score compliance data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRiskScoreCompliance() { return riskScoreCompliance; }
    /**
     * Performs the setRiskScoreCompliance operation in this module.
     *
     * @param riskScoreCompliance the riskScoreCompliance input value
     */
    public void setRiskScoreCompliance(BigDecimal riskScoreCompliance) { this.riskScoreCompliance = riskScoreCompliance; }
    /**
     * Retrieves risk score esg data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getRiskScoreEsg() { return riskScoreEsg; }
    /**
     * Performs the setRiskScoreEsg operation in this module.
     *
     * @param riskScoreEsg the riskScoreEsg input value
     */
    public void setRiskScoreEsg(BigDecimal riskScoreEsg) { this.riskScoreEsg = riskScoreEsg; }
    /**
     * Retrieves consolidated risk level data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getConsolidatedRiskLevel() { return consolidatedRiskLevel; }
    /**
     * Performs the setConsolidatedRiskLevel operation in this module.
     *
     * @param consolidatedRiskLevel the consolidatedRiskLevel input value
     */
    public void setConsolidatedRiskLevel(String consolidatedRiskLevel) { this.consolidatedRiskLevel = consolidatedRiskLevel; }
    /**
     * Retrieves a paginated list of approved vendor list records.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getApprovedVendorList() { return approvedVendorList; }
    /**
     * Performs the setApprovedVendorList operation in this module.
     *
     * @param approvedVendorList the approvedVendorList input value
     */
    public void setApprovedVendorList(Boolean approvedVendorList) { this.approvedVendorList = approvedVendorList; }
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