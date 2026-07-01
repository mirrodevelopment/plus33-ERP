package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import com.plus33.erp.esm.entity.WorkOrderTask;
import com.plus33.erp.esm.entity.ServiceSurvey;
import com.plus33.erp.esm.repository.EsmWorkOrderRepository;
import com.plus33.erp.esm.repository.WorkOrderTaskRepository;
import com.plus33.erp.esm.repository.ServiceSurveyRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WorkOrderService {

    private final EsmWorkOrderRepository workOrderRepository;
    private final WorkOrderTaskRepository taskRepository;
    private final ServiceSurveyRepository surveyRepository;
    private final EsmEventBus eventBus;

    public WorkOrderService(EsmWorkOrderRepository workOrderRepository,
                            WorkOrderTaskRepository taskRepository,
                            ServiceSurveyRepository surveyRepository,
                            EsmEventBus eventBus) {
        this.workOrderRepository = workOrderRepository;
        this.taskRepository = taskRepository;
        this.surveyRepository = surveyRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public EsmWorkOrder createWorkOrder(Long companyId, Long customerId, String workOrderNumber) {
        EsmWorkOrder wo = new EsmWorkOrder();
        wo.setCompanyId(companyId);
        wo.setCustomerId(customerId);
        wo.setWorkOrderNumber(workOrderNumber);
        wo.setStatus("DRAFT");
        wo.setPriority("MEDIUM");
        wo.setUpdatedAt(LocalDateTime.now());
        workOrderRepository.save(wo);

        eventBus.publish("WorkOrderCreated", companyId, wo.getId(), "Work order " + workOrderNumber + " created");
        return wo;
    }

    @Transactional
    public void transitionStatus(Long workOrderId, String targetStatus) {
        EsmWorkOrder wo = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Work Order not found"));

        // Validate state transitions
        String current = wo.getStatus();
        if ("CLOSED".equals(current)) {
            throw new IllegalStateException("Cannot transition from CLOSED state");
        }

        wo.setStatus(targetStatus);
        wo.setUpdatedAt(LocalDateTime.now());
        workOrderRepository.save(wo);

        // Map key events
        if ("SCHEDULED".equals(targetStatus)) {
            eventBus.publish("WorkOrderScheduled", wo.getCompanyId(), wo.getId(), "Scheduled");
        } else if ("IN_TRANSIT".equals(targetStatus)) {
            eventBus.publish("TechnicianArrived", wo.getCompanyId(), wo.getId(), "Technician is in transit");
        } else if ("COMPLETED".equals(targetStatus)) {
            eventBus.publish("MaintenanceCompleted", wo.getCompanyId(), wo.getId(), "Maintenance completed");
        } else if ("CUSTOMER_SIGNED".equals(targetStatus)) {
            eventBus.publish("CustomerSigned", wo.getCompanyId(), wo.getId(), "Customer signed off");
        }
    }

    @Transactional
    public void addWorkOrderTask(Long workOrderId, String description, int estimatedMinutes, String requiredSkill) {
        List<WorkOrderTask> currentTasks = taskRepository.findByWorkOrderId(workOrderId);
        WorkOrderTask task = new WorkOrderTask();
        task.setWorkOrderId(workOrderId);
        task.setTaskSequence(currentTasks.size() + 1);
        task.setTaskDescription(description);
        task.setEstimatedMinutes(estimatedMinutes);
        task.setStatus("PENDING");
        task.setRequiredSkill(requiredSkill);
        taskRepository.save(task);
    }

    @Transactional
    public void submitSurvey(Long workOrderId, int csat, int nps, int ces, String comments) {
        ServiceSurvey survey = new ServiceSurvey();
        survey.setWorkOrderId(workOrderId);
        survey.setCsatScore(csat);
        survey.setNpsScore(nps);
        survey.setCesScore(ces);
        survey.setComments(comments);
        surveyRepository.save(survey);

        EsmWorkOrder wo = workOrderRepository.findById(workOrderId)
                .orElseThrow(() -> new IllegalArgumentException("Work Order not found"));

        eventBus.publish("SurveySubmitted", wo.getCompanyId(), workOrderId, "Survey CSAT: " + csat);
    }
}
