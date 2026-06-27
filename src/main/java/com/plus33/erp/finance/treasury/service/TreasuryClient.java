package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryAuditEvent;
import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry;
import com.plus33.erp.finance.treasury.shared.MoneyAmount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * TreasuryClient — Decoupled SDK boundary for the Treasury bounded context.
 *
 * External modules (e.g., Accounts Payable, Budgeting, Financial Reporting)
 * must interact with the Treasury module exclusively through this façade.
 * This prevents cross-module direct repository access and enforces proper
 * domain boundary separation.
 *
 * <p><b>Design contract:</b>
 * <ul>
 *   <li>All treasury read/write operations from external modules go through here.</li>
 *   <li>This class may delegate to multiple internal services.</li>
 *   <li>This class may evolve independently of the internal service layer.</li>
 * </ul>
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryClient {

    private final TreasuryJournalService journalService;
    private final TreasuryRuleEngine ruleEngine;
    private final TreasuryAuditTimelineService auditTimelineService;
    private final TreasuryAiAdvisor aiAdvisor;
    private final TreasuryConfigurationRegistry configRegistry;

    // ── Journal Posting ───────────────────────────────────────────────────────

    /**
     * Records a double-entry cash transfer journal from an external module.
     * The idempotencyKey prevents duplicate postings on retry.
     */
    public List<TreasuryJournalEntry> postCashTransferJournal(
            Long companyId, Long transferId,
            BigDecimal amount, String currencyCode, BigDecimal exchangeRate,
            LocalDate postingDate, String actor, String idempotencyKey) {
        return journalService.postCashTransfer(
                companyId, transferId, amount, currencyCode, exchangeRate,
                postingDate, actor, idempotencyKey);
    }

    /**
     * Records a bank fee journal from an external module (e.g., AP payment run).
     */
    public List<TreasuryJournalEntry> postBankFeeJournal(
            Long companyId, Long sourceId, BigDecimal feeAmount,
            String currencyCode, LocalDate postingDate,
            String actor, String idempotencyKey) {
        return journalService.postBankFee(
                companyId, sourceId, feeAmount, currencyCode,
                postingDate, actor, idempotencyKey);
    }

    // ── Policy Evaluation ─────────────────────────────────────────────────────

    /**
     * Pre-validates a payment before submission from AP or other modules.
     * Throws {@link TreasuryRuleEngine.PolicyBreachException} if a HARD_BLOCK policy fires.
     */
    public void validatePayment(Long companyId, MoneyAmount amount, LocalDate paymentDate) {
        ruleEngine.evaluatePayment(companyId, amount, paymentDate);
    }

    /**
     * Returns all policy evaluation results for an operation — useful for
     * building warning messages in calling modules.
     */
    public List<TreasuryRuleEngine.PolicyEvaluationResult> evaluateOperation(
            Long companyId, String operationType, MoneyAmount amount, LocalDate date) {
        return ruleEngine.evaluateAll(companyId, operationType, amount, date);
    }

    // ── Audit ─────────────────────────────────────────────────────────────────

    /**
     * Records an external-module audit event in the treasury audit log.
     * E.g., AP payment approval that triggered a treasury payment batch.
     */
    public TreasuryAuditEvent recordExternalAuditEvent(
            Long companyId, String aggregateType, Long aggregateId,
            String eventAction, String description, String actor) {
        return auditTimelineService.record(
                companyId, aggregateType, aggregateId, eventAction,
                null, null, description, actor);
    }

    /**
     * Returns the audit timeline for a treasury aggregate (read by external modules).
     */
    public List<TreasuryAuditEvent> getAuditTimeline(String aggregateType, Long aggregateId) {
        return auditTimelineService.getTimeline(aggregateType, aggregateId);
    }

    public Page<TreasuryAuditEvent> getCompanyAuditTimeline(
            Long companyId, LocalDateTime from, LocalDateTime to, Pageable pageable) {
        return auditTimelineService.getCompanyTimeline(companyId, from, to, pageable);
    }

    // ── AI Advisory ───────────────────────────────────────────────────────────

    public List<TreasuryAiAdvisor.CashFlowPrediction> getCashFlowForecast(
            Long companyId, LocalDate from, LocalDate to, List<String> currencies) {
        return aiAdvisor.forecastCashFlow(companyId, from, to, currencies);
    }

    public List<TreasuryAiAdvisor.AnomalyAlert> getAnomalyAlerts(
            Long companyId, LocalDate from, LocalDate to) {
        return aiAdvisor.detectAnomalies(companyId, from, to);
    }

    public List<TreasuryAiAdvisor.HedgeRecommendation> getHedgeRecommendations(
            Long companyId, Map<String, MoneyAmount> exposures, int horizonDays) {
        return aiAdvisor.recommendHedges(companyId, exposures, horizonDays);
    }

    // ── Configuration ─────────────────────────────────────────────────────────

    /** Evicts the configuration cache after a configuration update. */
    public void refreshConfiguration(Long companyId) {
        configRegistry.evictCache(companyId);
        log.info("TreasuryClient: configuration cache refreshed for company={}", companyId);
    }
}
