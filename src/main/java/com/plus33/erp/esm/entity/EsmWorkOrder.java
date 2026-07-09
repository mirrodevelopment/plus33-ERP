/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.entity
 * File              : EsmWorkOrder.java
 * Purpose           : JPA Entity representing a persistent database record in Esm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmWorkOrderController
 * Related Service   : EsmWorkOrderService, EsmWorkOrderServiceImpl
 * Related Repository: EsmWorkOrderRepository
 * Related Entity    : EsmWorkOrder
 * Related DTO       : N/A
 * Related Mapper    : EsmWorkOrderMapper
 * Related DB Table  : esm_work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmWorkOrderRepository, EsmWorkOrderMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'esm_work_orders'. Defines persistent domain object for Esm Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.esm.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmWorkOrder}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'esm_work_orders'.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_work_orders}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "esm_work_orders")
public class EsmWorkOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "case_id")
    private Long caseId;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "installed_asset_id")
    private Long installedAssetId;

    @Column(name = "work_order_number", nullable = false, unique = true, length = 50)
    private String workOrderNumber;

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(nullable = false, length = 20)
    private String priority = "MEDIUM";

    @Column(name = "technician_id")
    private Long technicianId;

    @Column(name = "scheduled_at")
    private LocalDateTime scheduledAt;

    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

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
     * Retrieves case id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCaseId() { return caseId; }
    /**
     * Performs the setCaseId operation in this module.
     *
     * @param caseId the caseId input value
     */
    public void setCaseId(Long caseId) { this.caseId = caseId; }
    /**
     * Retrieves customer id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCustomerId() { return customerId; }
    /**
     * Performs the setCustomerId operation in this module.
     *
     * @param customerId the customerId input value
     */
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    /**
     * Retrieves a paginated list of installed asset id records.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getInstalledAssetId() { return installedAssetId; }
    /**
     * Performs the setInstalledAssetId operation in this module.
     *
     * @param installedAssetId the installedAssetId input value
     */
    public void setInstalledAssetId(Long installedAssetId) { this.installedAssetId = installedAssetId; }
    /**
     * Retrieves work order number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWorkOrderNumber() { return workOrderNumber; }
    /**
     * Performs the setWorkOrderNumber operation in this module.
     *
     * @param workOrderNumber the workOrderNumber input value
     */
    public void setWorkOrderNumber(String workOrderNumber) { this.workOrderNumber = workOrderNumber; }
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
     * Retrieves priority data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(String priority) { this.priority = priority; }
    /**
     * Retrieves technician id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTechnicianId() { return technicianId; }
    /**
     * Performs the setTechnicianId operation in this module.
     *
     * @param technicianId the technicianId input value
     */
    public void setTechnicianId(Long technicianId) { this.technicianId = technicianId; }
    /**
     * Retrieves scheduled at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getScheduledAt() { return scheduledAt; }
    /**
     * Performs the setScheduledAt operation in this module.
     *
     * @param scheduledAt the scheduledAt input value
     */
    public void setScheduledAt(LocalDateTime scheduledAt) { this.scheduledAt = scheduledAt; }
    /**
     * Retrieves actual duration minutes data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getActualDurationMinutes() { return actualDurationMinutes; }
    /**
     * Performs the setActualDurationMinutes operation in this module.
     *
     * @param actualDurationMinutes the actualDurationMinutes input value
     */
    public void setActualDurationMinutes(Integer actualDurationMinutes) { this.actualDurationMinutes = actualDurationMinutes; }
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