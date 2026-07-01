package com.plus33.erp.twin.decision;

import com.plus33.erp.platform.entity.PlatformAutonomousAction;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
public class RecommendationEngine {
    public void logRecommendation(PlatformAutonomousAction action, BigDecimal confidence) {
        // Recommendations logged for operators dashboard
    }
}