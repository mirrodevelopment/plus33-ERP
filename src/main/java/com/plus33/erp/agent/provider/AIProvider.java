package com.plus33.erp.agent.provider;

public interface AIProvider {
    String generateResponse(String systemPrompt, String userMessage);
}