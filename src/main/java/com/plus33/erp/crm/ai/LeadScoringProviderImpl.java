package com.plus33.erp.crm.ai;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class LeadScoringProviderImpl implements AiPredictionProvider {

    @Override
    public String getProviderName() {
        return "LEAD_SCORING";
    }

    @Override
    public BigDecimal calculateScore(Long entityId) {
        return new BigDecimal("85.00");
    }
}
