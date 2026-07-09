/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.entity
 * File              : RobotTask.java
 * Purpose           : JPA Entity representing a persistent database record in Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RobotTaskController
 * Related Service   : RobotTaskService, RobotTaskServiceImpl
 * Related Repository: RobotTaskRepository
 * Related Entity    : RobotTask
 * Related DTO       : N/A
 * Related Mapper    : RobotTaskMapper
 * Related DB Table  : robot_tasks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : RobotTaskRepository, RobotTaskMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'robot_tasks'. Defines persistent domain object for Wms Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.wms.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code RobotTask}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'robot_tasks'.</p>
 *
 * <p><b>Database Table   :</b> {@code robot_tasks}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Entity
@Table(name = "robot_tasks")
public class RobotTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "equipment_id")
    private EquipmentAsset equipment;

    @Column(name = "warehouse_task_id", nullable = false)
    private Long warehouseTaskId;

    @Column(name = "robot_provider_key", nullable = false, length = 50)
    private String robotProviderKey;

    @Column(name = "payload_json", columnDefinition = "TEXT")
    private String payloadJson;

    @Column(nullable = false, length = 30)
    private String status = "DISPATCHED";

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
     * Retrieves equipment data from the database.
     *
     * @return the EquipmentAsset result
     * @throws ResourceNotFoundException if the entity is not found
     */
    public EquipmentAsset getEquipment() { return equipment; }
    /**
     * Performs the setEquipment operation in this module.
     *
     * @param equipment the equipment input value
     */
    public void setEquipment(EquipmentAsset equipment) { this.equipment = equipment; }
    /**
     * Retrieves warehouse task id data from the database.
     *
     * @return the numeric result value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public Long getWarehouseTaskId() { return warehouseTaskId; }
    /**
     * Performs the setWarehouseTaskId operation in this module.
     *
     * @param warehouseTaskId the warehouseTaskId input value
     */
    public void setWarehouseTaskId(Long warehouseTaskId) { this.warehouseTaskId = warehouseTaskId; }
    /**
     * Retrieves robot provider key data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getRobotProviderKey() { return robotProviderKey; }
    /**
     * Performs the setRobotProviderKey operation in this module.
     *
     * @param robotProviderKey the robotProviderKey input value
     */
    public void setRobotProviderKey(String robotProviderKey) { this.robotProviderKey = robotProviderKey; }
    /**
     * Retrieves payload json data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getPayloadJson() { return payloadJson; }
    /**
     * Performs the setPayloadJson operation in this module.
     *
     * @param payloadJson the payloadJson input value
     */
    public void setPayloadJson(String payloadJson) { this.payloadJson = payloadJson; }
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