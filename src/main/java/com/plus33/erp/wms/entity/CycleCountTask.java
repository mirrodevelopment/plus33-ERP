/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : CycleCountTask.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountTaskController
 * Related Service   : CycleCountTaskService, CycleCountTaskServiceImpl
 * Related Repository: CycleCountTaskRepository
 * Related Entity    : CycleCountTask
 * Related DTO       : N/A
 * Related Mapper    : CycleCountTaskMapper
 * Related DB Table  : cycle_count_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountTaskRepository, CycleCountTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cycle_count_tasks'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cycle_count_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "cycle_count_tasks")
public class CycleCountTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private CycleCountPlan plan;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private WarehouseLocation location;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(name = "lot_number", length = 50)
    private String lotNumber;

    @Column(name = "serial_number", length = 100)
    private String serialNumber;

    /**
     * System (book) quantity at task creation.
     * Hidden from counter in blind_count mode — enforced at the service layer.
     */
    @Column(name = "system_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal systemQuantity;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(nullable = false, length = 30)
    private String status = "PENDING";
    // PENDING, ASSIGNED, IN_PROGRESS, COUNTED, RECOUNT_REQUIRED, APPROVED, REJECTED

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "started_at")
    private LocalDateTime startedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "recount_reason", columnDefinition = "TEXT")
    private String recountReason;

    @Column(name = "recount_count", nullable = false)
    private int recountCount = 0;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() { this.updatedAt = LocalDateTime.now(); }

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
     * Retrieves plan data from the database.
     *
     * @return the CycleCountPlan result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public CycleCountPlan getPlan() { return plan; }
    /**
     * Performs the setPlan operation in this module.
     *
     * @param plan the plan input value
     */
    public void setPlan(CycleCountPlan plan) { this.plan = plan; }
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
     * Retrieves location data from the database.
     *
     * @return the WarehouseLocation result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public WarehouseLocation getLocation() { return location; }
    /**
     * Performs the setLocation operation in this module.
     *
     * @param location the location input value
     */
    public void setLocation(WarehouseLocation location) { this.location = location; }
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
     * Retrieves lot number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getLotNumber() { return lotNumber; }
    /**
     * Performs the setLotNumber operation in this module.
     *
     * @param lotNumber the lotNumber input value
     */
    public void setLotNumber(String lotNumber) { this.lotNumber = lotNumber; }
    /**
     * Retrieves serial number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getSerialNumber() { return serialNumber; }
    /**
     * Performs the setSerialNumber operation in this module.
     *
     * @param serialNumber the serialNumber input value
     */
    public void setSerialNumber(String serialNumber) { this.serialNumber = serialNumber; }
    /**
     * Retrieves system quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getSystemQuantity() { return systemQuantity; }
    /**
     * Performs the setSystemQuantity operation in this module.
     *
     * @param systemQuantity the systemQuantity input value
     */
    public void setSystemQuantity(BigDecimal systemQuantity) { this.systemQuantity = systemQuantity; }
    /**
     * Retrieves unit id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUnitId() { return unitId; }
    /**
     * Performs the setUnitId operation in this module.
     *
     * @param unitId the unitId input value
     */
    public void setUnitId(Long unitId) { this.unitId = unitId; }
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
     * Retrieves assigned to data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getAssignedTo() { return assignedTo; }
    /**
     * Performs the setAssignedTo operation in this module.
     *
     * @param assignedTo the assignedTo input value
     */
    public void setAssignedTo(Long assignedTo) { this.assignedTo = assignedTo; }
    /**
     * Retrieves assigned at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getAssignedAt() { return assignedAt; }
    /**
     * Performs the setAssignedAt operation in this module.
     *
     * @param assignedAt the assignedAt input value
     */
    public void setAssignedAt(LocalDateTime assignedAt) { this.assignedAt = assignedAt; }
    /**
     * Retrieves started at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartedAt() { return startedAt; }
    /**
     * Performs the setStartedAt operation in this module.
     *
     * @param startedAt the startedAt input value
     */
    public void setStartedAt(LocalDateTime startedAt) { this.startedAt = startedAt; }
    /**
     * Retrieves completed at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCompletedAt() { return completedAt; }
    /**
     * Performs the setCompletedAt operation in this module.
     *
     * @param completedAt the completedAt input value
     */
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    /**
     * Retrieves recount reason data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRecountReason() { return recountReason; }
    /**
     * Performs the setRecountReason operation in this module.
     *
     * @param recountReason the recountReason input value
     */
    public void setRecountReason(String recountReason) { this.recountReason = recountReason; }
    /**
     * Retrieves recount count data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getRecountCount() { return recountCount; }
    /**
     * Performs the setRecountCount operation in this module.
     *
     * @param recountCount the recountCount input value
     */
    public void setRecountCount(int recountCount) { this.recountCount = recountCount; }
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