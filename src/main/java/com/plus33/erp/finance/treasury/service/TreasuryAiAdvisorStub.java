package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.shared.MoneyAmount;

import java.time.LocalDate;
import java.util.List;

/**
 * Default stub implementation of {@link TreasuryAiAdvisor}.
 *
 * In production, replace this stub with a real implementation backed by:
 * - A trained ML model API (e.g., Vertex AI, Azure ML)
 * - A generative AI model (e.g., Gemini Pro via Vertex)
 * - A rules-based forecasting engine
 *
 * This stub returns empty results so the module compiles and tests pass
 * without an AI backend configured.
 */
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@ConditionalOnMissingBean(name = "aiAdvisorImpl") // replaced by real impl in production
public class TreasuryAiAdvisorStub implements TreasuryAiAdvisor {

    @Override
    public List<CashFlowPrediction> forecastCashFlow(Long companyId, LocalDate startDate,
                                                      LocalDate endDate, List<String> currencies) {
        log.info("[AI Advisor Stub] forecastCashFlow called for company={} period={} to {}", companyId, startDate, endDate);
        return Collections.emptyList();
    }

    @Override
    public List<InvestmentRecommendation> recommendInvestments(Long companyId,
                                                                MoneyAmount availableSurplus,
                                                                LocalDate investmentDate) {
        log.info("[AI Advisor Stub] recommendInvestments called for company={} surplus={}", companyId, availableSurplus);
        return Collections.emptyList();
    }

    @Override
    public List<AnomalyAlert> detectAnomalies(Long companyId, LocalDate from, LocalDate to) {
        log.info("[AI Advisor Stub] detectAnomalies called for company={} period={} to {}", companyId, from, to);
        return Collections.emptyList();
    }

    @Override
    public List<HedgeRecommendation> recommendHedges(Long companyId,
                                                      Map<String, MoneyAmount> exposures,
                                                      int horizonDays) {
        log.info("[AI Advisor Stub] recommendHedges called for company={} currencies={}", companyId, exposures.keySet());
        return Collections.emptyList();
    }
}
