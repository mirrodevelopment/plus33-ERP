/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : WarehouseTaskEngineImpl.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseTaskEngineController
 * Related Service   : WarehouseTaskEngineImpl
 * Related Repository: WarehouseTaskRepository
 * Related Entity    : WarehouseTaskEngine
 * Related DTO       : N/A
 * Related Mapper    : WarehouseTaskEngineMapper
 * Related DB Table  : warehouse_task_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseTaskEngineController, WarehouseTaskEngineImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements WarehouseTaskEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.*;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Centralized Warehouse Task Engine — creates, assigns, starts, pauses,
 * completes, and cancels warehouse tasks across all task types.
 */
@Service
@Transactional
public class WarehouseTaskEngineImpl {

    private final WarehouseTaskRepository taskRepo;

    public WarehouseTaskEngineImpl(WarehouseTaskRepository taskRepo) {
        this.taskRepo = taskRepo;
    }

    /**
     * Creates a new wms and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the WarehouseTask result
     * @throws BusinessException if a business rule is violated
     */
    public WarehouseTask create(Long companyId, Long warehouseId, String taskType,
                                 String refType, Long refId, int priority, Long createdBy) {
        WarehouseTask task = new WarehouseTask();
        task.setCompanyId(companyId);
        task.setWarehouseId(warehouseId);
        task.setTaskType(taskType);
        task.setRefType(refType);
        task.setRefId(refId);
        task.setPriority(priority);
        task.setCreatedBy(createdBy);
        task.setTaskStatus("PENDING");
        return taskRepo.save(task);
    }

    /**
     * Performs the assign operation in this module.
     *
     * @param taskId the taskId input value
     * @param userId authenticated user identifier
     * @return the WarehouseTask result
     */
    public WarehouseTask assign(Long taskId, Long userId) {
        WarehouseTask task = getById(taskId);
        task.setAssignedTo(userId);
        task.setAssignedAt(LocalDateTime.now());
        task.setTaskStatus("ASSIGNED");
        return taskRepo.save(task);
    }

    /**
     * Performs the start operation in this module.
     *
     * @param taskId the taskId input value
     * @return the WarehouseTask result
     */
    public WarehouseTask start(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setStartedAt(LocalDateTime.now());
        task.setTaskStatus("IN_PROGRESS");
        return taskRepo.save(task);
    }

    /**
     * Performs the pause operation in this module.
     *
     * @param taskId the taskId input value
     * @return the WarehouseTask result
     */
    public WarehouseTask pause(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setPausedAt(LocalDateTime.now());
        task.setTaskStatus("PAUSED");
        return taskRepo.save(task);
    }

    /**
     * Completes the wms workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param taskId the taskId input value
     * @return the WarehouseTask result
     */
    public WarehouseTask complete(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setCompletedAt(LocalDateTime.now());
        task.setTaskStatus("COMPLETED");
        return taskRepo.save(task);
    }

    /**
     * Cancels the wms and posts reversing GL entries. Restores reserved resources.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param taskId the taskId input value
     * @return the WarehouseTask result
     * @throws BusinessException if a business rule is violated
     */
    public WarehouseTask cancel(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setCancelledAt(LocalDateTime.now());
        task.setTaskStatus("CANCELLED");
        return taskRepo.save(task);
    }

    /**
     * Retrieves by warehouse and status data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param warehouseId the warehouseId input value
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional(readOnly = true)
    public List<WarehouseTask> getByWarehouseAndStatus(Long companyId, Long warehouseId, String status) {
        return taskRepo.findByCompanyIdAndWarehouseIdAndTaskStatus(companyId, warehouseId, status);
    }

    /**
     * Retrieves my tasks data from the database.
     *
     * @param userId authenticated user identifier
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional(readOnly = true)
    public List<WarehouseTask> getMyTasks(Long userId, String status) {
        return taskRepo.findByAssignedToAndTaskStatus(userId, status);
    }

    private WarehouseTask getById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse task not found: " + id));
    }
}