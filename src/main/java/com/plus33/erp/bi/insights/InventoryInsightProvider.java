package com.plus33.erp.bi.insights;

import org.springframework.stereotype.Component;
import java.math.BigDecimal;

@Component
public class InventoryInsightProvider implements AiInsightProvider {
    public String getDomain() { return "INVENTORY"; }
    public String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue) {
        return "Inventory Insight for " + kpiCode + ": variance of " + varianceValue + " matches warehouse adjustments.";
    }
}