/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Agent Module
 * Package           : com.plus33.erp.agent.provider
 * File              : AIProvider.java
 * Purpose           : Service interface contract defining the API for Agent Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AIProviderController
 * Related Service   : AIProviderService, AIProviderServiceImpl
 * Related Repository: AIProviderRepository
 * Related Entity    : AIProvider
 * Related DTO       : generateResponse
 * Related Mapper    : AIProviderMapper
 * Related DB Table  : a_i_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Agent Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Agent Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.agent.provider;

public interface AIProvider {
    String generateResponse(String systemPrompt, String userMessage);
}