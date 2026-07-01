package com.plus33.erp.crm.ai;

import java.math.BigDecimal;

public interface AiPredictionProvider {
    String getProviderName();
    BigDecimal calculateScore(Long entityId);
}
