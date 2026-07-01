package com.plus33.erp.bi.insights;

import java.math.BigDecimal;

public interface AiInsightProvider {
    String getDomain();
    String generateInsight(Long companyId, String kpiCode, BigDecimal varianceValue);
}