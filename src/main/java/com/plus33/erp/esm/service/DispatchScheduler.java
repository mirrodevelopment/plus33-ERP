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
