package com.plus33.erp.agent.workflow;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class WorkflowAutomator {
    @Autowired PlatformAgentWorkflowRepository workflowRepo;
    @Autowired PlatformAgentWorkflowRunRepository runRepo;

    @Transactional
    public void registerWorkflow(String code, String name) {
        PlatformAgentWorkflow wf = new PlatformAgentWorkflow();
        wf.setWorkflowCode(code);
        wf.setWorkflowName(name);
        wf.setActive(true);
        workflowRepo.save(wf);
    }

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