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

@Service
public class WorkflowEngine {
    @Autowired IntegrationWorkflowDefinitionRepository definitionRepo;
    @Autowired IntegrationWorkflowInstanceRepository instanceRepo;
    @Autowired IntegrationWorkflowTaskRepository taskRepo;

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
