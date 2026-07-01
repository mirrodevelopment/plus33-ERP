package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.tool.ToolExecutor;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class ToolExecutionTest {

    @Autowired ToolExecutor toolExecutor;

    @Autowired PlatformAgentToolRepository toolRepo;
    @Autowired PlatformAgentToolExecutionRepository executionRepo;

    @Test
    void testToolExecutionScenarios() {
        // Register 40 tools
        for (int i = 1; i <= 40; i++) {
            toolExecutor.registerTool("tool-" + i, "Execute function " + i, "finance");
        }
        List<PlatformAgentTool> tools = toolRepo.findAll();
        assertTrue(tools.size() >= 40);

        // Execute tools 40 times
        for (int i = 1; i <= 40; i++) {
            toolExecutor.executeTool("tool-" + i, "{\"id\":" + i + "}");
        }
        List<PlatformAgentToolExecution> executions = executionRepo.findAll();
        assertTrue(executions.size() >= 40);
    }
}
