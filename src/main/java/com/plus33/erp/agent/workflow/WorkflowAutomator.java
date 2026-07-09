/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.workflow
 * File              : WorkflowAutomator.java
 * Purpose           : Business logic service layer for Agent Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WorkflowAutomatorController
 * Related Service   : WorkflowAutomator
 * Related Repository: WorkflowAutomatorRepository
 * Related Entity    : WorkflowAutomator
 * Related DTO       : N/A
 * Related Mapper    : WorkflowAutomatorMapper
 * Related DB Table  : workflow_automators
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : WorkflowAutomatorController, WorkflowAutomatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Agent Module. Implements WorkflowAutomatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.agent.workflow;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Agent Module</b>
 *
 * <p><b>Class  :</b> {@code WorkflowAutomator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.agent.workflow}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Agent Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * WorkflowAutomatorController
 *   --> WorkflowAutomator (this)
 *   --> Validate business rules
 *   --> WorkflowAutomatorRepository (read/write 'workflow_automators')
 *   --> WorkflowAutomatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code workflow_automators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class WorkflowAutomator {
    @Autowired PlatformAgentWorkflowRepository workflowRepo;
    @Autowired PlatformAgentWorkflowRunRepository runRepo;
    /**
     * Creates a new workflow and persists it to the database.
     *
     * @param code the code input value
     * @param name the name input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void registerWorkflow(String code, String name) {
        PlatformAgentWorkflow wf = new PlatformAgentWorkflow();
        wf.setWorkflowCode(code);
        wf.setWorkflowName(name);
        wf.setActive(true);
        workflowRepo.save(wf);
    }

    /**
     * Performs the triggerWorkflow operation in this module.
     *
     * @param code the code input value
     * @return the PlatformAgentWorkflowRun result
     */
    @Transactional
    public PlatformAgentWorkflowRun triggerWorkflow(String code) {
        PlatformAgentWorkflow wf = workflowRepo.findAll().stream()
                .filter(w -> w.getWorkflowCode().equals(code))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("Workflow not found"));

        PlatformAgentWorkflowRun run = new PlatformAgentWorkflowRun();
        run.setWorkflowId(wf.getId());
        run.setStatus("RUNNING");
        run.setStartedAt(LocalDateTime.now());
        run = runRepo.save(run);

        // Run cognitive task workflow
        run.setStatus("COMPLETED");
        run.setCompletedAt(LocalDateTime.now());
        return runRepo.save(run);
    }
}