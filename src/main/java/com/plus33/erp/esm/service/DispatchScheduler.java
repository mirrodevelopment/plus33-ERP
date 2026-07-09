/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : DispatchScheduler.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DispatchSchedulerController
 * Related Service   : DispatchScheduler
 * Related Repository: EsmWorkOrderRepository, WorkOrderTaskRepository
 * Related Entity    : DispatchScheduler
 * Related DTO       : N/A
 * Related Mapper    : DispatchSchedulerMapper
 * Related DB Table  : dispatch_schedulers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DispatchSchedulerController, DispatchSchedulerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements DispatchSchedulerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import com.plus33.erp.esm.entity.WorkOrderTask;
import com.plus33.erp.esm.repository.EsmWorkOrderRepository;
import com.plus33.erp.esm.repository.WorkOrderTaskRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code DispatchScheduler}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DispatchSchedulerController
 *   --> DispatchScheduler (this)
 *   --> Validate business rules
 *   --> DispatchSchedulerRepository (read/write 'dispatch_schedulers')
 *   --> DispatchSchedulerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code dispatch_schedulers}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DispatchScheduler {

    private final EsmWorkOrderRepository workOrderRepository;
    private final WorkOrderTaskRepository taskRepository;
    private final EsmEventBus eventBus;

    public DispatchScheduler(EsmWorkOrderRepository workOrderRepository, WorkOrderTaskRepository taskRepository, EsmEventBus eventBus) {
        this.workOrderRepository = workOrderRepository;
        this.taskRepository = taskRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the scheduleAndDispatch operation in this module.
     *
     * @param workOrderId the workOrderId input value
     * @param technicianId the technicianId input value
     * @param scheduledTime the scheduledTime input value
     */
    @Transactional
    public void scheduleAndDispatch(Long workOrderId, Long technicianId, LocalDateTime scheduledTime) {
        EsmWorkOrder workOrder = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Work Order not found"));

        // Match skills
        List<WorkOrderTask> tasks = taskRepository.findByWorkOrderId(workOrderId);
        for (WorkOrderTask task : tasks) {
            if (task.getRequiredSkill() != null) {
                // Rule validation: match required certifications/skills
                // (In a real system, we look up technician certification records. Here we simulate/approve skill match)
            }
        }

        workOrder.setTechnicianId(technicianId);
        workOrder.setScheduledAt(scheduledTime);
        workOrder.setStatus("SCHEDULED");
        workOrder.setUpdatedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);

        eventBus.publish("WorkOrderScheduled", workOrder.getCompanyId(), workOrderId, "Scheduled for technician: " + technicianId);

        // Auto transition to DISPATCHED
        workOrder.setStatus("DISPATCHED");
        workOrderRepository.save(workOrder);
        eventBus.publish("TechnicianAssigned", workOrder.getCompanyId(), workOrderId, "Technician " + technicianId + " dispatched");
    }
}