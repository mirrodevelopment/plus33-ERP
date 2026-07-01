package com.plus33.erp.agent.provider;

import org.springframework.stereotype.Service;

@Service
public class LocalLlmProvider implements AIProvider {
    @Override
    public String generateResponse(String systemPrompt, String userMessage) {
        return "Simulated cognitive response matching criteria: " + userMessage;
    }
}