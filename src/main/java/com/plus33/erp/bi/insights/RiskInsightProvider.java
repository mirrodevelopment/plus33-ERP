package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class RiskInsightProvider implements AiInsightProvider {
    public String getDomain() { return "RISK"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Risk Insight for " + kpiCode + ": variance of " + varianceValue + " aligns with enterprise control self-assessment results.";
    }
}