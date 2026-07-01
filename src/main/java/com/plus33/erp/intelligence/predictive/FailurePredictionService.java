package com.plus33.erp.intelligence.predictive;

import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class FailurePredictionService {
    public BigDecimal predictFailureProbability(Long instanceId) {
        return BigDecimal.valueOf(82.40);
    }
}