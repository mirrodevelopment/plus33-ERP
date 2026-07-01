package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.core.AgentOrchestrator;
import com.plus33.erp.agent.prompt.PromptVersionManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AgentLifecycleTest {

    @Autowired AgentOrchestrator orchestrator;
    @Autowired PromptVersionManager promptManager;

    @Autowired PlatformAiAgentRepository agentRepo;
    @Autowired PlatformAgentSessionRepository sessionRepo;
    @Autowired PlatformAgentPromptRepository promptRepo;
    @Autowired PlatformPromptVersionRepository promptVersionRepo;

    @Test
    void testAgentLifecycleScenarios() {
        // Register 40 agents
        for (int i = 1; i <= 40; i++) {
            PlatformAiAgent agent = new PlatformAiAgent();
            agent.setAgentCode("agent-code-" + i);
            agent.setAgentName("Agent Name " + i);
            agent.setSystemInstruction("You are agent " + i);
            agent.setActive(true);
            agentRepo.save(agent);
        }
        List<PlatformAiAgent> agents = agentRepo.findAll();
        assertTrue(agents.size() >= 40);

        // Start 40 sessions
        for (int i = 1; i <= 40; i++) {
            PlatformAgentSession s = orchestrator.startSession("user-" + i);
            assertNotNull(s);
        }
        List<PlatformAgentSession> sessions = sessionRepo.findAll();
        assertTrue(sessions.size() >= 40);

        // Register 40 prompts and versions
        for (int i = 1; i <= 40; i++) {
            promptManager.registerPrompt("prompt-" + i, "Prompt description " + i);
            promptManager.addVersion("prompt-" + i, "v1.0." + i, "System instructions " + i, "User template " + i);
        }
        List<PlatformAgentPrompt> prompts = promptRepo.findAll();
        assertTrue(prompts.size() >= 40);

        List<PlatformPromptVersion> versions = promptVersionRepo.findAll();
        assertTrue(versions.size() >= 40);
    }
}
