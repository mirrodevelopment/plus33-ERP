package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.workflow.WorkflowAutomator;
import com.plus33.erp.agent.vector.VectorStoreCoordinator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class WorkflowAutomationTest {

    @Autowired WorkflowAutomator automator;
    @Autowired VectorStoreCoordinator vectorCoordinator;

    @Autowired PlatformAgentWorkflowRepository workflowRepo;
    @Autowired PlatformAgentWorkflowRunRepository runRepo;
    @Autowired PlatformVectorStoreRepository storeRepo;
    @Autowired PlatformEmbeddingProviderRepository providerRepo;

    @Test
    void testWorkflowScenarios() {
        // Register 40 workflows
        for (int i = 1; i <= 40; i++) {
            automator.registerWorkflow("wf-" + i, "Workflow " + i);
        }
        List<PlatformAgentWorkflow> workflows = workflowRepo.findAll();
        assertTrue(workflows.size() >= 40);

        // Run workflows 40 times
        for (int i = 1; i <= 40; i++) {
            automator.triggerWorkflow("wf-" + i);
        }
        List<PlatformAgentWorkflowRun> runs = runRepo.findAll();
        assertTrue(runs.size() >= 40);

        // Register 40 vector stores and embedding providers
        for (int i = 1; i <= 40; i++) {
            vectorCoordinator.registerStore("vector-store-" + i, "CONNECTED");
            vectorCoordinator.registerEmbeddingProvider("provider-" + i, 1536);
        }
        List<PlatformVectorStore> stores = storeRepo.findAll();
        assertTrue(stores.size() >= 40);

        List<PlatformEmbeddingProvider> providers = providerRepo.findAll();
        assertTrue(providers.size() >= 40);
    }
}
