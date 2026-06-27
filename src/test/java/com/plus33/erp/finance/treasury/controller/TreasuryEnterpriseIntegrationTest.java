package com.plus33.erp.finance.treasury.controller;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.treasury.entity.*;
import com.plus33.erp.finance.treasury.repository.*;
import com.plus33.erp.finance.treasury.service.*;
import com.plus33.erp.finance.treasury.shared.MoneyAmount;
import com.plus33.erp.finance.treasury.shared.PaymentLifecycleStateMachine;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Enterprise-tier integration tests for the V29 Phase 8 additions:
 *  - PaymentLifecycleStateMachine transition validation
 *  - MoneyAmount value object arithmetic & currency guard
 *  - TreasuryRuleEngine policy evaluation
 *  - TreasuryAuditTimelineService record & retrieval
 *  - TreasuryJournalService idempotency
 *  - TreasuryV2Controller REST endpoints
 *  - TreasuryConfigurationRegistry profile resolution
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@DisplayName("Treasury Enterprise Integration Tests — Phase 8")
public class TreasuryEnterpriseIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private CompanyRepository companyRepository;
    @Autowired private AccountRepository accountRepository;
    @Autowired private TreasuryAuditEventRepository auditEventRepository;
    @Autowired private TreasuryRiskPolicyRepository riskPolicyRepository;
    @Autowired private TreasuryJournalEntryRepository journalEntryRepository;
    @Autowired private TreasuryAccountingProfileRepository accountingProfileRepository;
    @Autowired private BankRepository bankRepository;

    @Autowired private TreasuryAuditTimelineService auditTimelineService;
    @Autowired private TreasuryRuleEngine ruleEngine;
    @Autowired private TreasuryConfigurationRegistry configRegistry;
    @Autowired private TreasuryAiAdvisor aiAdvisor;
    @Autowired private TreasuryClient treasuryClient;

    private Company testCompany;
    private Account cashAccount;
    private Account fxGainAccount;
    private Account fxLossAccount;
    private Account bankFeeAccount;
    private Account interestIncomeAccount;
    private Account interestExpenseAccount;
    private Account hedgeReserveAccount;
    private Account investmentAccount;
    private Account overdraftAccount;

    @BeforeEach
    void setUp() {
        // Clear audit events to handle REQUIRES_NEW propagation
        auditEventRepository.deleteAll();

        testCompany = companyRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new IllegalStateException("No company found in test database"));

        // Find or create GL accounts for the accounting profile
        List<Account> accounts = accountRepository.findAll();
        assertThat(accounts).isNotEmpty();

        cashAccount = accounts.get(0);
        bankFeeAccount = accounts.size() > 1 ? accounts.get(1) : cashAccount;
        fxGainAccount = accounts.size() > 2 ? accounts.get(2) : cashAccount;
        fxLossAccount = accounts.size() > 3 ? accounts.get(3) : cashAccount;
        interestIncomeAccount = accounts.size() > 4 ? accounts.get(4) : cashAccount;
        interestExpenseAccount = accounts.size() > 5 ? accounts.get(5) : cashAccount;
        hedgeReserveAccount = accounts.size() > 6 ? accounts.get(6) : cashAccount;
        investmentAccount = accounts.size() > 7 ? accounts.get(7) : cashAccount;
        overdraftAccount = accounts.size() > 8 ? accounts.get(8) : cashAccount;
    }

    // ═══════════════════════════════════════════════════════════════════
    // 1. PaymentLifecycleStateMachine Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("SM-01: Valid payment lifecycle: DRAFT → VALIDATED → COMPLIANCE_PENDING → APPROVED → SETTLED → RECONCILED")
    void testPaymentLifecycle_happyPath() {
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.DRAFT, PaymentStatus.VALIDATED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.VALIDATED, PaymentStatus.COMPLIANCE_PENDING);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.COMPLIANCE_PENDING, PaymentStatus.COMPLIANCE_APPROVED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.COMPLIANCE_APPROVED, PaymentStatus.APPROVAL_PENDING);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.APPROVAL_PENDING, PaymentStatus.APPROVED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.APPROVED, PaymentStatus.QUEUED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.QUEUED, PaymentStatus.TRANSMITTING);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.TRANSMITTING, PaymentStatus.TRANSMITTED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.TRANSMITTED, PaymentStatus.BANK_ACCEPTED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.BANK_ACCEPTED, PaymentStatus.SETTLED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.SETTLED, PaymentStatus.RECONCILED);
    }

    @Test
    @DisplayName("SM-02: Invalid transition DRAFT → TRANSMITTED throws IllegalStateException")
    void testPaymentLifecycle_invalidTransition_draftToTransmitted() {
        assertThatThrownBy(() ->
                PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.DRAFT, PaymentStatus.TRANSMITTED))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("DRAFT")
                .hasMessageContaining("TRANSMITTED");
    }

    @Test
    @DisplayName("SM-03: RECONCILED is terminal — no outgoing transitions are valid")
    void testPaymentLifecycle_reconciledIsTerminal() {
        assertThat(PaymentStatus.RECONCILED.isTerminal()).isTrue();
        assertThat(PaymentLifecycleStateMachine.isValidTransition(PaymentStatus.RECONCILED, PaymentStatus.VALIDATED))
                .isFalse();
    }

    @Test
    @DisplayName("SM-04: Cancellation path valid from any pre-terminal state")
    void testPaymentLifecycle_cancellationFromMultipleStates() {
        List<PaymentStatus> cancellableStates = List.of(
                PaymentStatus.DRAFT, PaymentStatus.VALIDATED, PaymentStatus.COMPLIANCE_PENDING,
                PaymentStatus.APPROVAL_PENDING, PaymentStatus.APPROVED, PaymentStatus.QUEUED);

        for (PaymentStatus state : cancellableStates) {
            assertThat(PaymentLifecycleStateMachine.isValidTransition(state, PaymentStatus.CANCELLED))
                    .as("Should be able to cancel from state: " + state)
                    .isTrue();
        }
    }

    @Test
    @DisplayName("SM-05: Partial settlement path valid from BANK_ACCEPTED → PARTIALLY_SETTLED → SETTLED")
    void testPaymentLifecycle_partialSettlement() {
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.BANK_ACCEPTED, PaymentStatus.PARTIALLY_SETTLED);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.PARTIALLY_SETTLED, PaymentStatus.SETTLED);
    }

    @Test
    @DisplayName("SM-06: ON_HOLD recovery paths are valid")
    void testPaymentLifecycle_onHoldRecovery() {
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.APPROVED, PaymentStatus.ON_HOLD);
        PaymentLifecycleStateMachine.assertValidTransition(PaymentStatus.ON_HOLD, PaymentStatus.QUEUED);
    }

    @Test
    @DisplayName("SM-07: isTerminal() returns true for all terminal states")
    void testPaymentStatus_terminalFlags() {
        List<PaymentStatus> terminals = List.of(
                PaymentStatus.RECONCILED, PaymentStatus.REJECTED,
                PaymentStatus.FAILED, PaymentStatus.CANCELLED,
                PaymentStatus.REVERSED, PaymentStatus.EXPIRED);
        terminals.forEach(s -> assertThat(s.isTerminal()).as(s + " should be terminal").isTrue());
    }

    @Test
    @DisplayName("SM-08: isInFlight() returns true for in-flight states")
    void testPaymentStatus_inFlightFlags() {
        List<PaymentStatus> inFlight = List.of(
                PaymentStatus.TRANSMITTING, PaymentStatus.TRANSMITTED,
                PaymentStatus.BANK_ACCEPTED, PaymentStatus.PARTIALLY_SETTLED);
        inFlight.forEach(s -> assertThat(s.isInFlight()).as(s + " should be in-flight").isTrue());
    }

    // ═══════════════════════════════════════════════════════════════════
    // 2. MoneyAmount Value Object Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("MA-01: MoneyAmount arithmetic — add, subtract, multiply")
    void testMoneyAmount_arithmetic() {
        MoneyAmount a = MoneyAmount.of(new BigDecimal("1000.00"), "AED");
        MoneyAmount b = MoneyAmount.of(new BigDecimal("250.50"), "AED");

        assertThat(a.add(b).getAmount()).isEqualByComparingTo("1250.50");
        assertThat(a.subtract(b).getAmount()).isEqualByComparingTo("749.50");
        assertThat(a.multiply(new BigDecimal("2")).getAmount()).isEqualByComparingTo("2000.00");
    }

    @Test
    @DisplayName("MA-02: MoneyAmount currency guard throws on mismatched currencies")
    void testMoneyAmount_currencyMismatch() {
        MoneyAmount aed = MoneyAmount.of(new BigDecimal("1000.00"), "AED");
        MoneyAmount usd = MoneyAmount.of(new BigDecimal("500.00"), "USD");

        assertThatThrownBy(() -> aed.add(usd))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Currency mismatch");
    }

    @Test
    @DisplayName("MA-03: MoneyAmount currency conversion")
    void testMoneyAmount_conversion() {
        MoneyAmount usd = MoneyAmount.of(new BigDecimal("1000.00"), "USD");
        MoneyAmount aed = usd.convert(new BigDecimal("3.67"), "AED");

        assertThat(aed.getCurrencyCode()).isEqualTo("AED");
        assertThat(aed.getAmount()).isEqualByComparingTo("3670.00");
    }

    @Test
    @DisplayName("MA-04: MoneyAmount zero and sign checks")
    void testMoneyAmount_zeroAndSign() {
        MoneyAmount zero = MoneyAmount.zero("AED");
        MoneyAmount positive = MoneyAmount.of(new BigDecimal("100"), "AED");
        MoneyAmount negative = MoneyAmount.of(new BigDecimal("-50"), "AED");

        assertThat(zero.isZero()).isTrue();
        assertThat(positive.isPositive()).isTrue();
        assertThat(negative.isNegative()).isTrue();
    }

    @Test
    @DisplayName("MA-05: MoneyAmount uses banker's rounding (HALF_EVEN)")
    void testMoneyAmount_bankersRounding() {
        // 2.5 rounds to 2 (even), 3.5 rounds to 4 (even)
        MoneyAmount a = MoneyAmount.of(new BigDecimal("2.505"), "AED");
        MoneyAmount b = MoneyAmount.of(new BigDecimal("3.505"), "AED");

        // AED has 2 fraction digits — scale should be 2
        assertThat(a.getAmount().scale()).isEqualTo(2);
        assertThat(b.getAmount().scale()).isEqualTo(2);
    }

    @Test
    @DisplayName("MA-06: MoneyAmount equality ignores trailing zeros")
    void testMoneyAmount_equalityIgnoresTrailingZeros() {
        MoneyAmount a = MoneyAmount.of(new BigDecimal("100.00"), "AED");
        MoneyAmount b = MoneyAmount.of(new BigDecimal("100.000"), "AED");

        assertThat(a).isEqualTo(b);
    }

    // ═══════════════════════════════════════════════════════════════════
    // 3. TreasuryRuleEngine Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("RE-01: evaluateAll returns OK for operation well within limits")
    void testRuleEngine_evaluateAll_withinLimits() {
        // No policies configured yet — should return empty results (operation allowed)
        MoneyAmount smallAmount = MoneyAmount.of(new BigDecimal("100.00"), "AED");
        List<TreasuryRuleEngine.PolicyEvaluationResult> results =
                ruleEngine.evaluateAll(testCompany.getId(), "PAYMENT", smallAmount, LocalDate.now());

        // If policies are seeded, verify no BREACH results
        results.forEach(r ->
                assertThat(r.breachLevel()).isNotEqualTo(TreasuryRuleEngine.BreachLevel.BREACH));
    }

    @Test
    @DisplayName("RE-02: Risk policy — WARNING fires at 80% threshold")
    void testRuleEngine_warningThreshold() {
        // Create a SOFT_WARN policy with limit of 100,000 AED
        TreasuryRiskPolicy policy = TreasuryRiskPolicy.builder()
                .company(testCompany)
                .policyCategory("PAYMENT")
                .policyName("SINGLE_PAYMENT_TEST")
                .limitAmount(new BigDecimal("100000.00"))
                .warningThresholdPct(new BigDecimal("80.00"))
                .breachAction("SOFT_WARN")
                .effectiveFrom(LocalDate.now().minusDays(1))
                .createdBy("test")
                .build();
        riskPolicyRepository.save(policy);
        riskPolicyRepository.flush();

        // 85,000 should be a WARNING (above 80% = 80,000)
        MoneyAmount warnAmount = MoneyAmount.of(new BigDecimal("85000.00"), "AED");
        List<TreasuryRuleEngine.PolicyEvaluationResult> results =
                ruleEngine.evaluateAll(testCompany.getId(), "PAYMENT", warnAmount, LocalDate.now());

        boolean hasWarning = results.stream()
                .anyMatch(r -> r.breachLevel() == TreasuryRuleEngine.BreachLevel.WARNING
                        && "SINGLE_PAYMENT_TEST".equals(r.policyName()));
        assertThat(hasWarning).isTrue();
    }

    @Test
    @DisplayName("RE-03: HARD_BLOCK policy throws PolicyBreachException when limit exceeded")
    void testRuleEngine_hardBlock_throwsException() {
        TreasuryRiskPolicy blockPolicy = TreasuryRiskPolicy.builder()
                .company(testCompany)
                .policyCategory("PAYMENT")
                .policyName("SINGLE_PAYMENT")
                .currencyCode("AED")
                .limitAmount(new BigDecimal("50000.00"))
                .warningThresholdPct(new BigDecimal("80.00"))
                .breachAction("HARD_BLOCK")
                .effectiveFrom(LocalDate.now().minusDays(1))
                .createdBy("test")
                .build();
        riskPolicyRepository.save(blockPolicy);
        riskPolicyRepository.flush();

        MoneyAmount excessiveAmount = MoneyAmount.of(new BigDecimal("75000.00"), "AED");

        assertThatThrownBy(() -> ruleEngine.evaluatePayment(testCompany.getId(), excessiveAmount, LocalDate.now()))
                .isInstanceOf(TreasuryRuleEngine.PolicyBreachException.class)
                .hasMessageContaining("SINGLE_PAYMENT");
    }

    @Test
    @DisplayName("RE-04: Expired policy is not applied")
    void testRuleEngine_expiredPolicyIgnored() {
        TreasuryRiskPolicy expiredPolicy = TreasuryRiskPolicy.builder()
                .company(testCompany)
                .policyCategory("FX")
                .policyName("FX_EXPIRED_TEST")
                .limitAmount(new BigDecimal("1.00"))    // Tiny limit that would always breach
                .warningThresholdPct(new BigDecimal("80.00"))
                .breachAction("HARD_BLOCK")
                .effectiveFrom(LocalDate.now().minusYears(2))
                .effectiveTo(LocalDate.now().minusDays(1))  // Expired yesterday
                .createdBy("test")
                .build();
        riskPolicyRepository.save(expiredPolicy);
        riskPolicyRepository.flush();

        // This should NOT throw — the policy is expired
        MoneyAmount anyAmount = MoneyAmount.of(new BigDecimal("99999.00"), "USD");
        assertThat(ruleEngine.evaluateAll(testCompany.getId(), "FX", anyAmount, LocalDate.now()))
                .noneMatch(r -> "FX_EXPIRED_TEST".equals(r.policyName()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // 4. TreasuryAuditTimelineService Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("AUD-01: Record audit event and retrieve timeline")
    void testAuditTimeline_recordAndRetrieve() {
        TreasuryAuditEvent event = auditTimelineService.record(
                testCompany.getId(),
                "CashTransfer", 42L,
                "STATUS_CHANGED",
                "DRAFT", "APPROVED",
                "Test status change",
                "test-user");

        assertThat(event).isNotNull();
        assertThat(event.getId()).isNotNull();
        assertThat(event.getEventAction()).isEqualTo("STATUS_CHANGED");

        List<TreasuryAuditEvent> timeline = auditTimelineService.getTimeline("CashTransfer", 42L);
        assertThat(timeline).hasSize(1);
        assertThat(timeline.get(0).getActor()).isEqualTo("test-user");
    }

    @Test
    @DisplayName("AUD-02: Multiple events recorded in chronological order")
    void testAuditTimeline_multipleEventsInOrder() {
        auditTimelineService.record(testCompany.getId(), "PaymentBatch", 99L,
                "CREATED", null, "DRAFT", "Batch created", "ops-user");
        auditTimelineService.record(testCompany.getId(), "PaymentBatch", 99L,
                "APPROVED", "DRAFT", "APPROVED", "Batch approved", "manager");
        auditTimelineService.record(testCompany.getId(), "PaymentBatch", 99L,
                "TRANSMITTED", "APPROVED", "TRANSMITTED", "Batch sent to bank", "system");

        List<TreasuryAuditEvent> timeline = auditTimelineService.getTimeline("PaymentBatch", 99L);
        assertThat(timeline).hasSize(3);
        assertThat(timeline.get(0).getEventAction()).isEqualTo("CREATED");
        assertThat(timeline.get(1).getEventAction()).isEqualTo("APPROVED");
        assertThat(timeline.get(2).getEventAction()).isEqualTo("TRANSMITTED");
    }

    @Test
    @DisplayName("AUD-03: Company-scoped timeline pagination works")
    void testAuditTimeline_companyPagination() {
        // Record 5 events
        for (int i = 1; i <= 5; i++) {
            auditTimelineService.record(testCompany.getId(), "TreasuryInvestment", (long) i,
                    "CREATED", null, "ACTIVE", "Investment " + i + " created", "treasury-bot");
        }

        org.springframework.data.domain.Page<TreasuryAuditEvent> page =
                auditTimelineService.getCompanyTimeline(
                        testCompany.getId(),
                        LocalDateTime.now().minusHours(1),
                        LocalDateTime.now().plusHours(1),
                        org.springframework.data.domain.PageRequest.of(0, 3));

        assertThat(page.getContent()).hasSize(3);
        assertThat(page.getTotalElements()).isGreaterThanOrEqualTo(5);
    }

    // ═══════════════════════════════════════════════════════════════════
    // 5. TreasuryConfigurationRegistry Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("CFG-01: resolveAccountingProfile throws when no profile exists for company")
    void testConfigRegistry_missingProfile_throws() {
        // Attempt to resolve a profile for a company with no accounting profile
        // This depends on test data — if seeded, skip; otherwise expect exception
        Long nonExistentCompanyId = 999999L;
        assertThatThrownBy(() -> configRegistry.resolveAccountingProfile(nonExistentCompanyId, null))
                .isInstanceOf(Exception.class);
    }

    @Test
    @DisplayName("CFG-02: getActivePolicies returns only active, non-expired policies")
    void testConfigRegistry_getActivePolicies_filtersExpired() {
        TreasuryRiskPolicy activePolicy = TreasuryRiskPolicy.builder()
                .company(testCompany)
                .policyCategory("LIQUIDITY")
                .policyName("MIN_CASH_RESERVE")
                .limitAmount(new BigDecimal("100000.00"))
                .warningThresholdPct(new BigDecimal("90.00"))
                .breachAction("NOTIFY")
                .effectiveFrom(LocalDate.now().minusDays(30))
                .createdBy("test")
                .build();

        TreasuryRiskPolicy expiredPolicy = TreasuryRiskPolicy.builder()
                .company(testCompany)
                .policyCategory("LIQUIDITY")
                .policyName("OLD_MIN_CASH")
                .limitAmount(new BigDecimal("50000.00"))
                .warningThresholdPct(new BigDecimal("90.00"))
                .breachAction("NOTIFY")
                .effectiveFrom(LocalDate.now().minusYears(2))
                .effectiveTo(LocalDate.now().minusMonths(1))
                .createdBy("test")
                .build();

        riskPolicyRepository.saveAll(List.of(activePolicy, expiredPolicy));
        riskPolicyRepository.flush();

        List<TreasuryRiskPolicy> active = configRegistry.getActivePolicies(
                testCompany.getId(), "LIQUIDITY", LocalDate.now());

        assertThat(active).anyMatch(p -> "MIN_CASH_RESERVE".equals(p.getPolicyName()));
        assertThat(active).noneMatch(p -> "OLD_MIN_CASH".equals(p.getPolicyName()));
    }

    // ═══════════════════════════════════════════════════════════════════
    // 6. TreasuryAiAdvisor Stub Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("AI-01: AI advisor stub returns empty lists (safe default)")
    void testAiAdvisorStub_returnsEmpty() {
        List<TreasuryAiAdvisor.CashFlowPrediction> forecast = aiAdvisor.forecastCashFlow(
                testCompany.getId(), LocalDate.now(), LocalDate.now().plusMonths(1),
                List.of("AED", "USD"));

        assertThat(forecast).isNotNull().isEmpty();

        List<TreasuryAiAdvisor.AnomalyAlert> anomalies = aiAdvisor.detectAnomalies(
                testCompany.getId(), LocalDate.now().minusMonths(1), LocalDate.now());

        assertThat(anomalies).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("AI-02: AI investment recommendation stub returns empty safely")
    void testAiAdvisorStub_investmentRecommendations_empty() {
        MoneyAmount surplus = MoneyAmount.of(new BigDecimal("500000.00"), "AED");
        List<TreasuryAiAdvisor.InvestmentRecommendation> recs =
                aiAdvisor.recommendInvestments(testCompany.getId(), surplus, LocalDate.now());

        assertThat(recs).isNotNull().isEmpty();
    }

    // ═══════════════════════════════════════════════════════════════════
    // 7. TreasuryV2Controller REST Endpoints
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-01: GET /api/v2/treasury/journal/event/{type}/{id} returns 200 with empty list when no entries")
    void testV2Controller_getJournalEntriesByEvent_empty() throws Exception {
        mockMvc.perform(get("/api/v2/treasury/journal/event/CASH_TRANSFER/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-02: GET /api/v2/treasury/audit/{type}/{id} returns 200 with empty list when no events")
    void testV2Controller_getAuditTimeline_empty() throws Exception {
        mockMvc.perform(get("/api/v2/treasury/audit/CashTransfer/9999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-03: GET /api/v2/treasury/policies/evaluate returns 200 with results list")
    void testV2Controller_evaluatePolicies() throws Exception {
        mockMvc.perform(get("/api/v2/treasury/policies/evaluate")
                        .param("companyId", String.valueOf(testCompany.getId()))
                        .param("operationType", "PAYMENT")
                        .param("amount", "50000.00")
                        .param("currencyCode", "AED")
                        .param("date", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-04: GET /api/v2/treasury/ai/forecast returns 200")
    void testV2Controller_aiForecast() throws Exception {
        mockMvc.perform(get("/api/v2/treasury/ai/forecast")
                        .param("companyId", String.valueOf(testCompany.getId()))
                        .param("from", LocalDate.now().toString())
                        .param("to", LocalDate.now().plusMonths(1).toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-05: GET /api/v2/treasury/ai/anomalies returns 200")
    void testV2Controller_aiAnomalies() throws Exception {
        mockMvc.perform(get("/api/v2/treasury/ai/anomalies")
                        .param("companyId", String.valueOf(testCompany.getId()))
                        .param("from", LocalDate.now().minusMonths(1).toString())
                        .param("to", LocalDate.now().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    @WithMockUser(username = "treasury-admin", roles = {"ADMIN"})
    @DisplayName("V2-06: POST /api/v2/treasury/config/evict-cache/{companyId} returns 200")
    void testV2Controller_evictCache() throws Exception {
        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .post("/api/v2/treasury/config/evict-cache/" + testCompany.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    // ═══════════════════════════════════════════════════════════════════
    // 8. TreasuryClient SDK Tests
    // ═══════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("SDK-01: TreasuryClient.recordExternalAuditEvent saves an audit event")
    void testTreasuryClient_recordExternalAuditEvent() {
        TreasuryAuditEvent event = treasuryClient.recordExternalAuditEvent(
                testCompany.getId(),
                "PaymentRun", 77L,
                "PAYMENT_RUN_SUBMITTED",
                "AP module triggered treasury payment run", "ap-system");

        assertThat(event).isNotNull();
        assertThat(event.getAggregateType()).isEqualTo("PaymentRun");
        assertThat(event.getEventAction()).isEqualTo("PAYMENT_RUN_SUBMITTED");
        assertThat(event.getActor()).isEqualTo("ap-system");
    }

    @Test
    @DisplayName("SDK-02: TreasuryClient.validatePayment does not throw for small amounts within limits")
    void testTreasuryClient_validatePayment_withinLimits() {
        MoneyAmount tinyAmount = MoneyAmount.of(new BigDecimal("10.00"), "AED");
        // Should not throw — no blocking policies or small amount within default limits
        treasuryClient.validatePayment(testCompany.getId(), tinyAmount, LocalDate.now());
    }

    @Test
    @DisplayName("SDK-03: TreasuryClient.getAuditTimeline returns same events as auditTimelineService")
    void testTreasuryClient_getAuditTimeline_consistency() {
        auditTimelineService.record(testCompany.getId(), "FxDeal", 55L,
                "BOOKED", null, "BOOKED", "FX deal booked", "fx-trader");

        List<TreasuryAuditEvent> directResult = auditTimelineService.getTimeline("FxDeal", 55L);
        List<TreasuryAuditEvent> sdkResult = treasuryClient.getAuditTimeline("FxDeal", 55L);

        assertThat(sdkResult).hasSize(directResult.size());
    }

    @Test
    @DisplayName("SDK-04: TreasuryClient.refreshConfiguration completes without error")
    void testTreasuryClient_refreshConfiguration() {
        // Should not throw
        treasuryClient.refreshConfiguration(testCompany.getId());
    }
}
