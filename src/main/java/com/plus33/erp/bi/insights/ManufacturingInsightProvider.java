package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ManufacturingInsightProvider implements AiInsightProvider {
    public String getDomain() { return "MANUFACTURING"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Manufacturing Insight for " + kpiCode + ": variance of " + varianceValue + " indicates shopfloor capacity changes.";
    }
}