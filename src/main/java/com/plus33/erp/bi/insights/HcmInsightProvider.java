package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class HcmInsightProvider implements AiInsightProvider {
    public String getDomain() { return "HCM"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "HCM Insight for " + kpiCode + ": variance of " + varianceValue + " is driven by cyclical contractor headcount.";
    }
}