/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : ServiceBillingRecord.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceBillingRecordController
 * Related Service   : ServiceBillingRecordService, ServiceBillingRecordServiceImpl
 * Related Repository: ServiceBillingRecordRepository
 * Related Entity    : ServiceBillingRecord
 * Related DTO       : N/A
 * Related Mapper    : ServiceBillingRecordMapper
 * Related DB Table  : esm_billing_records
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceBillingRecordRepository, ServiceBillingRecordMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_billing_records'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceBillingRecord}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_billing_records'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_billing_records}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_billing_records")
public class ServiceBillingRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "work_order_id", nullable = false)
    private Long workOrderId;

    @Column(name = "billing_method", nullable = false, length = 30)
    private String billingMethod = "T_AND_M";

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 30)
    private String status = "READY_TO_BILL";

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
     * Retrieves work order id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWorkOrderId() { return workOrderId; }
    /**
     * Performs the setWorkOrderId operation in this module.
     *
     * @param workOrderId the workOrderId input value
     */
    public void setWorkOrderId(Long workOrderId) { this.workOrderId = workOrderId; }
    /**
     * Retrieves billing method data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getBillingMethod() { return billingMethod; }
    /**
     * Performs the setBillingMethod operation in this module.
     *
     * @param billingMethod the billingMethod input value
     */
    public void setBillingMethod(String billingMethod) { this.billingMethod = billingMethod; }
    /**
     * Retrieves amount data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getAmount() { return amount; }
    /**
     * Performs the setAmount operation in this module.
     *
     * @param amount the amount input value
     */
    public void setAmount(BigDecimal amount) { this.amount = amount; }
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
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}