/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.insights
 * File              : AiInsightProvider.java
 * Purpose           : Service interface contract defining the API for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AiInsightProviderController
 * Related Service   : AiInsightProviderService, AiInsightProviderServiceImpl
 * Related Repository: AiInsightProviderRepository
 * Related Entity    : AiInsightProvider
 * Related DTO       : N/A
 * Related Mapper    : AiInsightProviderMapper
 * Related DB Table  : ai_insight_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Bi Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.bi.insights;

import java.math.BigDecimal;

public interface AiInsightProvider {
    String getDomain();
    String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue);
}