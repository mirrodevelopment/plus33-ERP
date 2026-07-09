/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Crm Module
 * Package           : com.plus33.erp.crm.ai
 * File              : AiPredictionProvider.java
 * Purpose           : Service interface contract defining the API for Crm Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AiPredictionProviderController
 * Related Service   : AiPredictionProviderService, AiPredictionProviderServiceImpl
 * Related Repository: AiPredictionProviderRepository
 * Related Entity    : AiPredictionProvider
 * Related DTO       : N/A
 * Related Mapper    : AiPredictionProviderMapper
 * Related DB Table  : ai_prediction_providers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Crm Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Crm Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.crm.ai;

import java.math.BigDecimal;

public interface AiPredictionProvider {
    String getProviderName();
    BigDecimal calculateScore(Long entityId);
}
