package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class ProcurementInsightProvider implements AiInsightProvider {
    public String getDomain() { return "PROCUREMENT"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Procurement Insight for " + kpiCode + ": variance of " + varianceValue + " correlates with supplier lead time variances.";
    }
}