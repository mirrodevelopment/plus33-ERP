/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Integration Module
 * Package           : com.plus33.erp.integration.workflow
 * File              : WorkflowEngine.java
 * Purpose           : Business logic service layer for Integration Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkflowEngineController
 * Related Service   : WorkflowEngine
 * Related Repository: WorkflowEngineRepository
 * Related Entity    : WorkflowEngine
 * Related DTO       : N/A
 * Related Mapper    : WorkflowEngineMapper
 * Related DB Table  : workflow_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WorkflowEngineController, WorkflowEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Integration Module. Implements WorkflowEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.integration.workflow;

import com.plus33.erp.integration.entity.IntegrationWorkflowDefinition;
import com.plus33.erp.integration.entity.IntegrationWorkflowInstance;
import com.plus33.erp.integration.entity.IntegrationWorkflowTask;
import com.plus33.erp.integration.repository.IntegrationWorkflowDefinitionRepository;
import com.plus33.erp.integration.repository.IntegrationWorkflowInstanceRepository;
import com.plus33.erp.integration.repository.IntegrationWorkflowTaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Integration Module</b>
 *
 * <p><b>Class  :</b> {@code WorkflowEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.integration.workflow}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Integration Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WorkflowEngineController
 *   --> WorkflowEngine (this)
 *   --> Validate business rules
 *   --> WorkflowEngineRepository (read/write 'workflow_engines')
 *   --> WorkflowEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code workflow_engines}</p>
 * <p><b>Module Deps      :</b> Integration</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class WorkflowEngine {
    @Autowired IntegrationWorkflowDefinitionRepository definitionRepo;
    @Autowired IntegrationWorkflowInstanceRepository instanceRepo;
    @Autowired IntegrationWorkflowTaskRepository taskRepo;
    /**
     * Performs the startWorkflow operation in this module.
     *
     * @param definitionCode the definitionCode input value
     * @param variablesJson the variablesJson input value
     * @return the numeric result value
     */
    @Transactional
    public IntegrationWorkflowInstance startWorkflow(String definitionCode, String variablesJson) {
        IntegrationWorkflowDefinition def = definitionRepo.findAll().stream()
                .filter(d -> d.getDefinitionCode().equals(definitionCode))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Workflow definition not found"));

        IntegrationWorkflowInstance instance = new IntegrationWorkflowInstance();
        instance.setDefinitionCode(definitionCode);
        instance.setInstanceCode("WF-INST-" + System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 8));
        instance.setStatus("RUNNING");
        instance.setCurrentStep("step-1");
        instance.setVariablesJson(variablesJson);
        instance.setCreatedAt(LocalDateTime.now());
        instance.setUpdatedAt(LocalDateTime.now());
        instanceRepo.save(instance);

        executeTask(instance.getInstanceCode(), "step-1");
        return instance;
    }

    /**
     * Performs the executeTask operation in this module.
     *
     * @param instanceCode the instanceCode input value
     * @param taskName the taskName input value
     */
    @Transactional
    public void executeTask(String instanceCode, String taskName) {
        IntegrationWorkflowTask task = new IntegrationWorkflowTask();
        task.setInstanceCode(instanceCode);
        task.setTaskName(taskName);
        task.setStatus("SUCCESS");
        task.setAttempts(1);
        task.setStartedAt(LocalDateTime.now());
        task.setCompletedAt(LocalDateTime.now());
        taskRepo.save(task);
    }
}