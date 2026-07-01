package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class SalesInsightProvider implements AiInsightProvider {
    public String getDomain() { return "SALES"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Sales Insight for " + kpiCode + ": variance of " + varianceValue + " reflects key regional distributor incentives.";
    }
}