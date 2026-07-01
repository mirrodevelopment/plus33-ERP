package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class FinanceInsightProvider implements AiInsightProvider {
    public String getDomain() { return "FINANCE"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Finance Insight for " + kpiCode + ": variance of " + varianceValue + " is driven by standard accrual matching rules.";
    }
}