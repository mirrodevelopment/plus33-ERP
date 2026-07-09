/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : WarehouseLaborLog.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseLaborLogController
 * Related Service   : WarehouseLaborLogService, WarehouseLaborLogServiceImpl
 * Related Repository: WarehouseLaborLogRepository
 * Related Entity    : WarehouseLaborLog
 * Related DTO       : N/A
 * Related Mapper    : WarehouseLaborLogMapper
 * Related DB Table  : warehouse_labor_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseLaborLogRepository, WarehouseLaborLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'warehouse_labor_logs'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseLaborLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'warehouse_labor_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_labor_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "warehouse_labor_logs")
public class WarehouseLaborLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "warehouse_id", nullable = false)
    private Long warehouseId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "task_type", nullable = false, length = 30)
    private String taskType;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "travel_time_seconds")
    private Integer travelTimeSeconds = 0;

    @Column(name = "idle_time_seconds")
    private Integer idleTimeSeconds = 0;

    @Column(name = "items_handled", precision = 18, scale = 6)
    private BigDecimal itemsHandled = BigDecimal.ZERO;

    @Column(name = "labor_cost", precision = 18, scale = 2)
    private BigDecimal laborCost = BigDecimal.ZERO;

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
     * Retrieves user id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getUserId() { return userId; }
    /**
     * Performs the setUserId operation in this module.
     *
     * @param userId authenticated user identifier
     */
    public void setUserId(Long userId) { this.userId = userId; }
    /**
     * Retrieves task id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getTaskId() { return taskId; }
    /**
     * Performs the setTaskId operation in this module.
     *
     * @param taskId the taskId input value
     */
    public void setTaskId(Long taskId) { this.taskId = taskId; }
    /**
     * Retrieves task type data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getTaskType() { return taskType; }
    /**
     * Performs the setTaskType operation in this module.
     *
     * @param taskType the taskType input value
     */
    public void setTaskType(String taskType) { this.taskType = taskType; }
    /**
     * Retrieves start time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getStartTime() { return startTime; }
    /**
     * Performs the setStartTime operation in this module.
     *
     * @param startTime the startTime input value
     */
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    /**
     * Retrieves end time data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getEndTime() { return endTime; }
    /**
     * Performs the setEndTime operation in this module.
     *
     * @param endTime the endTime input value
     */
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    /**
     * Retrieves travel time seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getTravelTimeSeconds() { return travelTimeSeconds; }
    /**
     * Performs the setTravelTimeSeconds operation in this module.
     *
     * @param travelTimeSeconds the travelTimeSeconds input value
     */
    public void setTravelTimeSeconds(Integer travelTimeSeconds) { this.travelTimeSeconds = travelTimeSeconds; }
    /**
     * Retrieves idle time seconds data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Integer getIdleTimeSeconds() { return idleTimeSeconds; }
    /**
     * Performs the setIdleTimeSeconds operation in this module.
     *
     * @param idleTimeSeconds the idleTimeSeconds input value
     */
    public void setIdleTimeSeconds(Integer idleTimeSeconds) { this.idleTimeSeconds = idleTimeSeconds; }
    /**
     * Retrieves items handled data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getItemsHandled() { return itemsHandled; }
    /**
     * Performs the setItemsHandled operation in this module.
     *
     * @param itemsHandled the itemsHandled input value
     */
    public void setItemsHandled(BigDecimal itemsHandled) { this.itemsHandled = itemsHandled; }
    /**
     * Retrieves labor cost data from the database.
     *
     * @return the BigDecimal result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public BigDecimal getLaborCost() { return laborCost; }
    /**
     * Performs the setLaborCost operation in this module.
     *
     * @param laborCost the laborCost input value
     */
    public void setLaborCost(BigDecimal laborCost) { this.laborCost = laborCost; }
    /**
     * Retrieves created at data from the database.
     *
     * @return the LocalDateTime result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public LocalDateTime getCreatedAt() { return createdAt; }
}