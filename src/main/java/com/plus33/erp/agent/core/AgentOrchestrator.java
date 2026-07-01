package com.plus33.erp.agent.core;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.agent.provider.AIProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AgentOrchestrator {
    @Autowired PlatformAgentSessionRepository sessionRepo;
    @Autowired PlatformAgentMessageRepository messageRepo;
    @Autowired PlatformAgentReasoningRepository reasoningRepo;
    @Autowired AIProvider aiProvider;

    @Transactional
    public PlatformAgentSession startSession(String userId) {
        PlatformAgentSession session = new PlatformAgentSession();
        session.setSessionToken("session-" + UUID.randomUUID());
        session.setUserIdentity(userId);
        session.setActive(true);
        session.setCreatedAt(LocalDateTime.now());
        return sessionRepo.save(session);
    }

    @Transactional
    public void processMessage(Long sessionId, String userMsg) {
        PlatformAgentMessage userMessage = new PlatformAgentMessage();
        userMessage.setSessionId(sessionId);
        userMessage.setSenderRole("USER");
        userMessage.setContent(userMsg);
        userMessage.setSentAt(LocalDateTime.now());
        userMessage = messageRepo.save(userMessage);

        String systemPrompt = "You are a helpful ERP assistant.";
        String response = aiProvider.generateResponse(systemPrompt, userMsg);

        PlatformAgentMessage agentMsg = new PlatformAgentMessage();
        agentMsg.setSessionId(sessionId);
        agentMsg.setSenderRole("AGENT");
        agentMsg.setContent(response);
        agentMsg.setSentAt(LocalDateTime.now());
        agentMsg = messageRepo.save(agentMsg);

        PlatformAgentReasoning reasoning = new PlatformAgentReasoning();
        reasoning.setMessageId(agentMsg.getId());
        reasoning.setThoughtProcess("Parsed query -> Searched database -> Formulated response");
        reasoning.setConfidenceScore(BigDecimal.valueOf(98.50));
        reasoning.setCreatedAt(LocalDateTime.now());
        reasoningRepo.save(reasoning);
    }
}