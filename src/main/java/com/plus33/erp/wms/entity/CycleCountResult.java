/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : CycleCountResult.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountResultController
 * Related Service   : CycleCountResultService, CycleCountResultServiceImpl
 * Related Repository: CycleCountResultRepository
 * Related Entity    : CycleCountResult
 * Related DTO       : N/A
 * Related Mapper    : CycleCountResultMapper
 * Related DB Table  : cycle_count_results
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CycleCountResultRepository, CycleCountResultMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'cycle_count_results'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'cycle_count_results'.</p>
 *
 * <p><b>Database Table   :</b> {@code cycle_count_results}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "cycle_count_results")
public class CycleCountResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private CycleCountTask task;

    @Column(name = "plan_id", nullable = false)
    private Long planId;

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

    @Column(name = "system_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal systemQuantity;

    @Column(name = "counted_quantity", nullable = false, precision = 18, scale = 6)
    private BigDecimal countedQuantity;

    /** DB GENERATED: counted_quantity - system_quantity */
    @Column(name = "variance_quantity", insertable = false, updatable = false, precision = 18, scale = 6)
    private BigDecimal varianceQuantity;

    @Column(name = "variance_pct", precision = 10, scale = 4)
    private BigDecimal variancePct;

    @Column(name = "unit_id", nullable = false)
    private Long unitId;

    @Column(name = "unit_cost", precision = 18, scale = 6)
    private BigDecimal unitCost;

    @Column(name = "variance_value", precision = 18, scale = 2)
    private BigDecimal varianceValue;

    @Column(name = "count_number", nullable = false)
    private int countNumber = 1;

    @Column(nullable = false, length = 30)
    private String status = "PENDING_APPROVAL";
    // PENDING_APPROVAL, AUTO_APPROVED, APPROVED, REJECTED, RECOUNT_REQUESTED

    @Column(name = "approval_notes", columnDefinition = "TEXT")
    private String approvalNotes;

    @Column(name = "approved_by")
    private Long approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "gl_journal_id")
    private Long glJournalId;

    @Column(name = "counted_by")
    private Long countedBy;

    @Column(name = "counted_at", nullable = false, updatable = false)
    private LocalDateTime countedAt = LocalDateTime.now();

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
     * Retrieves task data from the database.
     *
     * @return the CycleCountTask result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public CycleCountTask getTask() { return task; }
    /**
     * Performs the setTask operation in this module.
     *
     * @param task the task input value
     */
    public void setTask(CycleCountTask task) { this.task = task; }
    /**
     * Retrieves plan id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getPlanId() { return planId; }
    /**
     * Performs the setPlanId operation in this module.
     *
     * @param planId the planId input value
     */
    public void setPlanId(Long planId) { this.planId = planId; }
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
     * Retrieves counted quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getCountedQuantity() { return countedQuantity; }
    /**
     * Performs the setCountedQuantity operation in this module.
     *
     * @param countedQuantity the countedQuantity input value
     */
    public void setCountedQuantity(BigDecimal countedQuantity) { this.countedQuantity = countedQuantity; }
    /**
     * Retrieves variance quantity data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getVarianceQuantity() { return varianceQuantity; }
    /**
     * Retrieves variance pct data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getVariancePct() { return variancePct; }
    /**
     * Performs the setVariancePct operation in this module.
     *
     * @param variancePct the variancePct input value
     */
    public void setVariancePct(BigDecimal variancePct) { this.variancePct = variancePct; }
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
     * Retrieves unit cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getUnitCost() { return unitCost; }
    /**
     * Performs the setUnitCost operation in this module.
     *
     * @param unitCost the unitCost input value
     */
    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }
    /**
     * Retrieves variance value data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getVarianceValue() { return varianceValue; }
    /**
     * Performs the setVarianceValue operation in this module.
     *
     * @param varianceValue the varianceValue input value
     */
    public void setVarianceValue(BigDecimal varianceValue) { this.varianceValue = varianceValue; }
    /**
     * Retrieves count number data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getCountNumber() { return countNumber; }
    /**
     * Performs the setCountNumber operation in this module.
     *
     * @param countNumber the countNumber input value
     */
    public void setCountNumber(int countNumber) { this.countNumber = countNumber; }
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
     * Retrieves approval notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getApprovalNotes() { return approvalNotes; }
    /**
     * Performs the setApprovalNotes operation in this module.
     *
     * @param approvalNotes the approvalNotes input value
     */
    public void setApprovalNotes(String approvalNotes) { this.approvalNotes = approvalNotes; }
    /**
     * Retrieves approved by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getApprovedBy() { return approvedBy; }
    /**
     * Performs the setApprovedBy operation in this module.
     *
     * @param approvedBy the approvedBy input value
     */
    public void setApprovedBy(Long approvedBy) { this.approvedBy = approvedBy; }
    /**
     * Retrieves approved at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getApprovedAt() { return approvedAt; }
    /**
     * Performs the setApprovedAt operation in this module.
     *
     * @param approvedAt the approvedAt input value
     */
    public void setApprovedAt(LocalDateTime approvedAt) { this.approvedAt = approvedAt; }
    /**
     * Retrieves gl journal id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getGlJournalId() { return glJournalId; }
    /**
     * Performs the setGlJournalId operation in this module.
     *
     * @param glJournalId the glJournalId input value
     */
    public void setGlJournalId(Long glJournalId) { this.glJournalId = glJournalId; }
    /**
     * Retrieves counted by data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getCountedBy() { return countedBy; }
    /**
     * Performs the setCountedBy operation in this module.
     *
     * @param countedBy the countedBy input value
     */
    public void setCountedBy(Long countedBy) { this.countedBy = countedBy; }
    /**
     * Retrieves counted at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCountedAt() { return countedAt; }
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