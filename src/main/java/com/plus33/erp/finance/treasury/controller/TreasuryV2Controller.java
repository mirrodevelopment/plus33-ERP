package com.plus33.erp.finance.treasury.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.finance.treasury.entity.TreasuryAuditEvent;
import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry;
import com.plus33.erp.finance.treasury.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Treasury REST API — Version 2.
 *
 * V2 exposes the advanced enterprise capabilities added in the V29 blueprint:
 * <ul>
 *   <li>Journal entry ledger queries</li>
 *   <li>Audit timeline access</li>
 *   <li>AI advisory endpoints</li>
 *   <li>Risk policy evaluation</li>
 *   <li>Configuration management</li>
 * </ul>
 *
 * Stable V1 endpoints remain on {@link TreasuryController} at {@code /api/v1/treasury}.
 */
@RestController
@RequestMapping("/api/v2/treasury")
@RequiredArgsConstructor
public class TreasuryV2Controller {

    private final TreasuryJournalService journalService;
    private final TreasuryAuditTimelineService auditTimelineService;
    private final TreasuryRuleEngine ruleEngine;
    private final TreasuryConfigurationRegistry configRegistry;
    private final TreasuryAiAdvisor aiAdvisor;

    // ═══════════════════════════════════════════════════════════════════
    // Journal Ledger
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/v2/treasury/journal/event/{eventType}/{eventId}
     * Returns all journal entries for a specific source event.
     */
    @GetMapping("/journal/event/{eventType}/{eventId}")
    public ResponseEntity<ApiResponse<List<TreasuryJournalEntry>>> getJournalEntriesByEvent(
            @PathVariable String eventType,
            @PathVariable Long eventId) {
        List<TreasuryJournalEntry> entries = journalService.getEntriesForEvent(eventType, eventId);
        return ResponseEntity.ok(ApiResponse.success(
                "Journal entries retrieved for " + eventType + "#" + eventId, entries));
    }

    /**
     * POST /api/v2/treasury/journal/cash-transfer/{transferId}
     * Posts double-entry journal for a cash transfer (idempotent).
     */
    @PostMapping("/journal/cash-transfer/{transferId}")
    public ResponseEntity<ApiResponse<List<TreasuryJournalEntry>>> postCashTransferJournal(
            @PathVariable Long transferId,
            @RequestParam Long companyId,
            @RequestParam BigDecimal amount,
            @RequestParam String currencyCode,
            @RequestParam(defaultValue = "1.000000") BigDecimal exchangeRate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate postingDate,
            @RequestParam String idempotencyKey,
            Principal principal) {
        List<TreasuryJournalEntry> entries = journalService.postCashTransfer(
                companyId, transferId, amount, currencyCode, exchangeRate,
                postingDate, principal.getName(), idempotencyKey);
        return ResponseEntity.ok(ApiResponse.success("Journal entries posted", entries));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Audit Timeline
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/v2/treasury/audit/{aggregateType}/{aggregateId}
     * Returns the full chronological audit trail for any treasury aggregate.
     */
    @GetMapping("/audit/{aggregateType}/{aggregateId}")
    public ResponseEntity<ApiResponse<List<TreasuryAuditEvent>>> getAuditTimeline(
            @PathVariable String aggregateType,
            @PathVariable Long aggregateId) {
        List<TreasuryAuditEvent> timeline = auditTimelineService.getTimeline(aggregateType, aggregateId);
        return ResponseEntity.ok(ApiResponse.success(
                "Audit timeline for " + aggregateType + "#" + aggregateId, timeline));
    }

    /**
     * GET /api/v2/treasury/audit/company/{companyId}
     * Returns paginated audit events for a company within a time window.
     */
    @GetMapping("/audit/company/{companyId}")
    public ResponseEntity<ApiResponse<Page<TreasuryAuditEvent>>> getCompanyAuditTimeline(
            @PathVariable Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<TreasuryAuditEvent> events = auditTimelineService.getCompanyTimeline(
                companyId, from, to,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt")));
        return ResponseEntity.ok(ApiResponse.success("Company audit timeline", events));
    }

    /**
     * GET /api/v2/treasury/audit/company/{companyId}/actor/{actor}
     * Returns paginated audit events filtered by actor.
     */
    @GetMapping("/audit/company/{companyId}/actor/{actor}")
    public ResponseEntity<ApiResponse<Page<TreasuryAuditEvent>>> getActorAuditTimeline(
            @PathVariable Long companyId,
            @PathVariable String actor,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size) {
        Page<TreasuryAuditEvent> events = auditTimelineService.getActorTimeline(
                companyId, actor, from, to,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "occurredAt")));
        return ResponseEntity.ok(ApiResponse.success("Actor audit timeline", events));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Policy Evaluation
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/v2/treasury/policies/evaluate
     * Evaluates a proposed operation amount against all active policies.
     * Returns a list of OK / WARNING / BREACH results without executing the operation.
     */
    @GetMapping("/policies/evaluate")
    public ResponseEntity<ApiResponse<List<TreasuryRuleEngine.PolicyEvaluationResult>>> evaluatePolicies(
            @RequestParam Long companyId,
            @RequestParam String operationType,
            @RequestParam BigDecimal amount,
            @RequestParam String currencyCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        com.plus33.erp.finance.treasury.shared.MoneyAmount moneyAmount =
                com.plus33.erp.finance.treasury.shared.MoneyAmount.of(amount, currencyCode);
        List<TreasuryRuleEngine.PolicyEvaluationResult> results =
                ruleEngine.evaluateAll(companyId, operationType, moneyAmount, date);
        return ResponseEntity.ok(ApiResponse.success("Policy evaluation results", results));
    }

    // ═══════════════════════════════════════════════════════════════════
    // AI Advisory
    // ═══════════════════════════════════════════════════════════════════

    /**
     * GET /api/v2/treasury/ai/forecast
     * Returns AI-generated cash flow forecast for a company.
     */
    @GetMapping("/ai/forecast")
    public ResponseEntity<ApiResponse<List<TreasuryAiAdvisor.CashFlowPrediction>>> getCashFlowForecast(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "AED,USD,EUR") List<String> currencies) {
        List<TreasuryAiAdvisor.CashFlowPrediction> predictions =
                aiAdvisor.forecastCashFlow(companyId, from, to, currencies);
        return ResponseEntity.ok(ApiResponse.success("Cash flow forecast (AI advisory)", predictions));
    }

    /**
     * GET /api/v2/treasury/ai/anomalies
     * Returns anomaly alerts detected by the AI advisor.
     */
    @GetMapping("/ai/anomalies")
    public ResponseEntity<ApiResponse<List<TreasuryAiAdvisor.AnomalyAlert>>> detectAnomalies(
            @RequestParam Long companyId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        List<TreasuryAiAdvisor.AnomalyAlert> alerts = aiAdvisor.detectAnomalies(companyId, from, to);
        return ResponseEntity.ok(ApiResponse.success("Treasury anomaly alerts (AI advisory)", alerts));
    }

    // ═══════════════════════════════════════════════════════════════════
    // Configuration Management
    // ═══════════════════════════════════════════════════════════════════

    /**
     * POST /api/v2/treasury/config/evict-cache/{companyId}
     * Evicts the configuration cache for a company after configuration changes.
     */
    @PostMapping("/config/evict-cache/{companyId}")
    public ResponseEntity<ApiResponse<String>> evictConfigCache(@PathVariable Long companyId) {
        configRegistry.evictCache(companyId);
        return ResponseEntity.ok(ApiResponse.success(
                "Configuration cache evicted for company " + companyId, "OK"));
    }
}
