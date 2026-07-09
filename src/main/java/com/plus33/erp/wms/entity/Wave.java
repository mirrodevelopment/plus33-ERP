/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : Wave.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WaveController
 * Related Service   : WaveService, WaveServiceImpl
 * Related Repository: WaveRepository
 * Related Entity    : Wave
 * Related DTO       : N/A
 * Related Mapper    : WaveMapper
 * Related DB Table  : waves
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WaveRepository, WaveMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'waves'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "waves",
       uniqueConstraints = @UniqueConstraint(columnNames = {"company_id", "wave_number"}))
/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code Wave}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'waves'.</p>
 *
 * <p><b>Database Table   :</b> {@code waves}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public class Wave {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "wave_number", nullable = false, length = 50)
    private String waveNumber;

    @Column(name = "wave_type", nullable = false, length = 30)
    private String waveType = "STANDARD";
    // STANDARD, BATCH, CLUSTER, ZONE, EMERGENCY

    @Column(nullable = false, length = 30)
    private String status = "DRAFT";
    // DRAFT, RELEASED, PICKING, PARTIALLY_PICKED, PICKED, PACKING, SHIPPED, CLOSED, CANCELLED

    @Column(name = "picking_strategy", nullable = false, length = 50)
    private String pickingStrategy = "FEFO";

    @Column(nullable = false)
    private int priority = 5;

    @Column(name = "planned_date")
    private LocalDate plannedDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "created_by")
    private Long createdBy;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt = LocalDateTime.now();

    @OneToMany(mappedBy = "wave", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private java.util.List<PickingWork> pickingWorkList = new java.util.ArrayList<>();

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
     * Retrieves warehouse id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseId() { return warehouseId; }
    /**
     * Performs the setWarehouseId operation in this module.
     *
     * @param warehouseId the warehouseId input value
     */
    public void setWarehouseId(Long warehouseId) { this.warehouseId = warehouseId; }
    /**
     * Retrieves wave number data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWaveNumber() { return waveNumber; }
    /**
     * Performs the setWaveNumber operation in this module.
     *
     * @param waveNumber the waveNumber input value
     */
    public void setWaveNumber(String waveNumber) { this.waveNumber = waveNumber; }
    /**
     * Retrieves wave type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getWaveType() { return waveType; }
    /**
     * Performs the setWaveType operation in this module.
     *
     * @param waveType the waveType input value
     */
    public void setWaveType(String waveType) { this.waveType = waveType; }
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
     * Retrieves picking strategy data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPickingStrategy() { return pickingStrategy; }
    /**
     * Performs the setPickingStrategy operation in this module.
     *
     * @param pickingStrategy the pickingStrategy input value
     */
    public void setPickingStrategy(String pickingStrategy) { this.pickingStrategy = pickingStrategy; }
    /**
     * Retrieves priority data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public int getPriority() { return priority; }
    /**
     * Performs the setPriority operation in this module.
     *
     * @param priority the priority input value
     */
    public void setPriority(int priority) { this.priority = priority; }
    /**
     * Retrieves planned date data from the database.
     *
     * @return the LocalDate result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDate getPlannedDate() { return plannedDate; }
    /**
     * Performs the setPlannedDate operation in this module.
     *
     * @param plannedDate the plannedDate input value
     */
    public void setPlannedDate(LocalDate plannedDate) { this.plannedDate = plannedDate; }
    /**
     * Retrieves notes data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getNotes() { return notes; }
    /**
     * Performs the setNotes operation in this module.
     *
     * @param notes the notes input value
     */
    public void setNotes(String notes) { this.notes = notes; }
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
     * Retrieves released at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getReleasedAt() { return releasedAt; }
    /**
     * Performs the setReleasedAt operation in this module.
     *
     * @param releasedAt the releasedAt input value
     */
    public void setReleasedAt(LocalDateTime releasedAt) { this.releasedAt = releasedAt; }
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
    /**
     * Retrieves a paginated list of picking work list records.
     *
     * @throws ResourceNotFoundException if the entity is not found
     */
    public java.util.List<PickingWork> getPickingWorkList() { return pickingWorkList; }
    /**
     * Performs the setPickingWorkList operation in this module.
     *
     * @param pickingWorkList the pickingWorkList input value
     */
    public void setPickingWorkList(java.util.List<PickingWork> pickingWorkList) { this.pickingWorkList = pickingWorkList; }
}