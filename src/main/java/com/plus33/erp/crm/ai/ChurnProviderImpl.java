package com.plus33.erp.crm.ai;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class ChurnProviderImpl implements AiPredictionProvider {

    @Override
    public String getProviderName() {
        return "CHURN";
    }

    @Override
    public BigDecimal calculateScore(Long entityId) {
        return new BigDecimal("12.50");
    }
}
