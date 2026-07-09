/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : WorkOrderService.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkOrderController
 * Related Service   : WorkOrderService
 * Related Repository: EsmWorkOrderRepository, WorkOrderTaskRepository, ServiceSurveyRepository
 * Related Entity    : WorkOrder
 * Related DTO       : N/A
 * Related Mapper    : WorkOrderMapper
 * Related DB Table  : work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkOrderController, WorkOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements WorkOrderService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code WorkOrderService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WorkOrderController
 *   --> WorkOrderService (this)
 *   --> Validate business rules
 *   --> WorkOrderRepository (read/write 'work_orders')
 *   --> WorkOrderMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code work_orders}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new work order and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param workOrderNumber the workOrderNumber input value
     * @return the EsmWorkOrder result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the transitionStatus operation in this module.
     *
     * @param workOrderId the workOrderId input value
     * @param targetStatus the targetStatus input value
     */
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

    /**
     * Creates a new work order task and persists it to the database.
     *
     * @param workOrderId the workOrderId input value
     * @param description the description input value
     * @param estimatedMinutes the estimatedMinutes input value
     * @param requiredSkill the requiredSkill input value
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Submits the survey for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param workOrderId the workOrderId input value
     * @param csat the csat input value
     * @param nps the nps input value
     * @param ces the ces input value
     * @param comments the comments input value
     * @throws BusinessException if a business rule is violated
     */
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