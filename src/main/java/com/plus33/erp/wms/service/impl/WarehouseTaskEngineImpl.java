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

    public WarehouseTask assign(Long taskId, Long userId) {
        WarehouseTask task = getById(taskId);
        task.setAssignedTo(userId);
        task.setAssignedAt(LocalDateTime.now());
        task.setTaskStatus("ASSIGNED");
        return taskRepo.save(task);
    }

    public WarehouseTask start(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setStartedAt(LocalDateTime.now());
        task.setTaskStatus("IN_PROGRESS");
        return taskRepo.save(task);
    }

    public WarehouseTask pause(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setPausedAt(LocalDateTime.now());
        task.setTaskStatus("PAUSED");
        return taskRepo.save(task);
    }

    public WarehouseTask complete(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setCompletedAt(LocalDateTime.now());
        task.setTaskStatus("COMPLETED");
        return taskRepo.save(task);
    }

    public WarehouseTask cancel(Long taskId) {
        WarehouseTask task = getById(taskId);
        task.setCancelledAt(LocalDateTime.now());
        task.setTaskStatus("CANCELLED");
        return taskRepo.save(task);
    }

    @Transactional(readOnly = true)
    public List<WarehouseTask> getByWarehouseAndStatus(Long companyId, Long warehouseId, String status) {
        return taskRepo.findByCompanyIdAndWarehouseIdAndTaskStatus(companyId, warehouseId, status);
    }

    @Transactional(readOnly = true)
    public List<WarehouseTask> getMyTasks(Long userId, String status) {
        return taskRepo.findByAssignedToAndTaskStatus(userId, status);
    }

    private WarehouseTask getById(Long id) {
        return taskRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Warehouse task not found: " + id));
    }
}
