package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.shared.MoneyAmount;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Isolated AI advisory boundary for treasury intelligence features.
 *
 * All AI-generated insights must pass through this interface. Implementations
 * can swap between a local rules engine, a fine-tuned ML model, or a generative
 * AI API without impacting calling services.
 *
 * <b>Important:</b> Recommendations from this service are advisory only.
 * No treasury transaction should be blocked or executed solely based on AI output.
 * Human approval is always required before acting on AI advice.
 */
public interface TreasuryAiAdvisor {

    /**
     * Generates a cash flow forecast for the given company and date range.
     *
     * @param companyId  owning company
     * @param startDate  forecast start
     * @param endDate    forecast end
     * @param currencies list of currencies to include in the forecast
     * @return list of daily cash flow predictions per currency
     */
    List<CashFlowPrediction> forecastCashFlow(
            Long companyId, LocalDate startDate, LocalDate endDate, List<String> currencies);

    /**
     * Recommends investment allocation based on available surplus cash,
     * current risk policies, and market conditions.
     *
     * @param companyId       owning company
     * @param availableSurplus surplus cash amount in base currency
     * @param investmentDate  target investment date
     * @return prioritized list of investment recommendations
     */
    List<InvestmentRecommendation> recommendInvestments(
            Long companyId, MoneyAmount availableSurplus, LocalDate investmentDate);

    /**
     * Detects anomalies in treasury cash flows and flags suspicious patterns.
     *
     * @param companyId owning company
     * @param from      analysis start date
     * @param to        analysis end date
     * @return list of detected anomalies with risk scores
     */
    List<AnomalyAlert> detectAnomalies(Long companyId, LocalDate from, LocalDate to);

    /**
     * Suggests optimal FX hedging coverage for known currency exposures.
     *
     * @param companyId   owning company
     * @param exposures   map of currencyCode → net open exposure amount
     * @param horizonDays number of days to hedge forward
     * @return hedge recommendations per currency
     */
    List<HedgeRecommendation> recommendHedges(
            Long companyId, Map<String, MoneyAmount> exposures, int horizonDays);

    // ── Advisory DTOs ──────────────────────────────────────────────────────────

    record CashFlowPrediction(
            LocalDate date,
            String currencyCode,
            MoneyAmount projectedInflow,
            MoneyAmount projectedOutflow,
            MoneyAmount projectedNetPosition,
            double confidenceScore) {}

    record InvestmentRecommendation(
            String investmentType,
            MoneyAmount suggestedAmount,
            String currencyCode,
            int tenorDays,
            double expectedYieldPct,
            String rationale) {}

    record AnomalyAlert(
            String anomalyType,
            LocalDate detectedDate,
            String description,
            double riskScore,
            String affectedAggregateType,
            Long affectedAggregateId) {}

    record HedgeRecommendation(
            String currencyCode,
            MoneyAmount exposureAmount,
            MoneyAmount recommendedHedgeAmount,
            double coverageRatioPct,
            String hedgeInstrument,
            String rationale) {}
}
