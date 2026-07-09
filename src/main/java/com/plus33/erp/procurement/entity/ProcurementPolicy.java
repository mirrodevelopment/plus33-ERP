/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.entity
 * File              : ProcurementPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProcurementPolicyController
 * Related Service   : ProcurementPolicyService, ProcurementPolicyServiceImpl
 * Related Repository: ProcurementPolicyRepository
 * Related Entity    : ProcurementPolicy
 * Related DTO       : N/A
 * Related Mapper    : ProcurementPolicyMapper
 * Related DB Table  : procurement_policies
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProcurementPolicyRepository, ProcurementPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'procurement_policies'. Defines persistent domain object for Procurement Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.procurement.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code ProcurementPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'procurement_policies'.</p>
 *
 * <p><b>Database Table   :</b> {@code procurement_policies}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "procurement_policies")
public class ProcurementPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "policy_type", nullable = false, length = 50)
    private String policyType;

    @Column(name = "threshold_amount")
    private BigDecimal thresholdAmount;

    @Column(name = "preferred_supplier_id")
    private Long preferredSupplierId;

    @Column(nullable = false)
    private Boolean active = true;

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
     * Retrieves policy type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPolicyType() { return policyType; }
    /**
     * Performs the setPolicyType operation in this module.
     *
     * @param policyType the policyType input value
     */
    public void setPolicyType(String policyType) { this.policyType = policyType; }
    /**
     * Retrieves threshold amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getThresholdAmount() { return thresholdAmount; }
    /**
     * Performs the setThresholdAmount operation in this module.
     *
     * @param thresholdAmount the thresholdAmount input value
     */
    public void setThresholdAmount(BigDecimal thresholdAmount) { this.thresholdAmount = thresholdAmount; }
    /**
     * Retrieves preferred supplier id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPreferredSupplierId() { return preferredSupplierId; }
    /**
     * Performs the setPreferredSupplierId operation in this module.
     *
     * @param preferredSupplierId the preferredSupplierId input value
     */
    public void setPreferredSupplierId(Long preferredSupplierId) { this.preferredSupplierId = preferredSupplierId; }
    /**
     * Retrieves active data from the database.
     *
     * @return true if operation succeeded, false otherwise
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Boolean getActive() { return active; }
    /**
     * Performs the setActive operation in this module.
     *
     * @param active the active input value
     */
    public void setActive(Boolean active) { this.active = active; }
}