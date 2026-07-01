package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.core.AgentOrchestrator;
import com.plus33.erp.agent.memory.AgentMemoryManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class AgentConversationTest {

    @Autowired AgentOrchestrator orchestrator;
    @Autowired AgentMemoryManager memoryManager;

    @Autowired PlatformAgentSessionRepository sessionRepo;
    @Autowired PlatformAgentMessageRepository messageRepo;
    @Autowired PlatformAgentReasoningRepository reasoningRepo;
    @Autowired PlatformAgentMemoryRepository memoryRepo;

    @Test
    void testAgentConversationScenarios() {
        PlatformAgentSession session = orchestrator.startSession("user-conv");

        // Process 40 messages
        for (int i = 1; i <= 40; i++) {
            orchestrator.processMessage(session.getId(), "User message " + i);
        }
        List<PlatformAgentMessage> messages = messageRepo.findAll().stream()
                .filter(m -> m.getSessionId().equals(session.getId()))
                .toList();
        assertEquals(80, messages.size()); // 40 USER + 40 AGENT messages

        List<PlatformAgentReasoning> reasoning = reasoningRepo.findAll();
        assertTrue(reasoning.size() >= 40);

        // Store 40 memory elements
        for (int i = 1; i <= 40; i++) {
            memoryManager.storeMemory(session.getId(), "SESSION", "key-" + i, "val-" + i);
        }
        List<PlatformAgentMemory> memories = memoryRepo.findAll().stream()
                .filter(m -> m.getSessionId().equals(session.getId()))
                .toList();
        assertEquals(40, memories.size());
    }
}
