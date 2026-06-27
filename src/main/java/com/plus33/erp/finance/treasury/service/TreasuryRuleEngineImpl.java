package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy;
import com.plus33.erp.finance.treasury.shared.MoneyAmount;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link TreasuryRuleEngine}.
 *
 * Evaluation order:
 * 1. Find the most specific matching policy (currency-specific > generic)
 * 2. Compare the operative amount against the limit
 * 3. Apply breach action: HARD_BLOCK throws PolicyBreachException,
 *    SOFT_WARN logs and adds a WARNING result, NOTIFY logs only
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryRuleEngineImpl implements TreasuryRuleEngine {

    private final TreasuryConfigurationRegistry configRegistry;

    @Override
    @Transactional(readOnly = true)
    public void evaluatePayment(Long companyId, MoneyAmount amount, LocalDate paymentDate) {
        List<PolicyEvaluationResult> results = new ArrayList<>();

        // Check single payment limit
        checkPolicy(companyId, "PAYMENT", "SINGLE_PAYMENT", amount, paymentDate, results);

        // Check daily outflow limit (simplified — full implementation would aggregate daily total)
        checkPolicy(companyId, "PAYMENT", "DAILY_OUTFLOW", amount, paymentDate, results);

        throwIfBlocked(results);
    }

    @Override
    @Transactional(readOnly = true)
    public void evaluateFxTrade(Long companyId, String buyCurrency,
                                 MoneyAmount buyAmount, LocalDate tradeDate) {
        List<PolicyEvaluationResult> results = new ArrayList<>();
        checkPolicy(companyId, "FX", "SINGLE_TRADE", buyAmount, tradeDate, results);
        checkPolicy(companyId, "FX", "CURRENCY_EXPOSURE", buyAmount, tradeDate, results);
        throwIfBlocked(results);
    }

    @Override
    @Transactional(readOnly = true)
    public void evaluateInvestment(Long companyId, String investmentType,
                                   MoneyAmount amount, LocalDate investmentDate) {
        List<PolicyEvaluationResult> results = new ArrayList<>();
        checkPolicy(companyId, "INVESTMENT", "SINGLE_INVESTMENT", amount, investmentDate, results);
        checkPolicy(companyId, "CONCENTRATION", "INVESTMENT_TYPE_CONCENTRATION", amount, investmentDate, results);
        throwIfBlocked(results);
    }

    @Override
    @Transactional(readOnly = true)
    public void evaluateBankExposure(Long companyId, Long bankId,
                                      MoneyAmount amount, LocalDate date) {
        List<PolicyEvaluationResult> results = new ArrayList<>();
        checkPolicy(companyId, "BANK", "SINGLE_BANK_EXPOSURE", amount, date, results);
        throwIfBlocked(results);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PolicyEvaluationResult> evaluateAll(Long companyId, String operationType,
                                                     MoneyAmount amount, LocalDate date) {
        List<PolicyEvaluationResult> results = new ArrayList<>();
        List<TreasuryRiskPolicy> policies = configRegistry.getActivePolicies(
                companyId, operationType, date);

        for (TreasuryRiskPolicy policy : policies) {
            results.add(evaluate(policy, amount));
        }
        return results;
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    private void checkPolicy(Long companyId, String category, String name,
                              MoneyAmount amount, LocalDate date,
                              List<PolicyEvaluationResult> results) {
        Optional<TreasuryRiskPolicy> policyOpt = configRegistry.findBestMatchingPolicy(
                companyId, category, name, amount.getCurrencyCode(), date);

        if (policyOpt.isEmpty()) {
            log.debug("No active policy found for {}/{} — operation allowed", category, name);
            return;
        }

        PolicyEvaluationResult result = evaluate(policyOpt.get(), amount);
        results.add(result);

        if (result.breachLevel() == BreachLevel.WARNING) {
            log.warn("Treasury policy WARNING: policy={}/{} amount={} threshold={}%",
                    category, name, amount, policyOpt.get().getWarningThresholdPct());
        }
    }

    private PolicyEvaluationResult evaluate(TreasuryRiskPolicy policy, MoneyAmount amount) {
        BigDecimal limitAmount = policy.getLimitAmount();
        BigDecimal warningAmount = limitAmount.multiply(
                policy.getWarningThresholdPct().divide(BigDecimal.valueOf(100), 6, java.math.RoundingMode.HALF_EVEN));

        int cmp = amount.getAmount().compareTo(limitAmount);
        int warnCmp = amount.getAmount().compareTo(warningAmount);

        if (cmp > 0) {
            // Breach
            String msg = String.format(
                    "Policy breach: %s/%s — amount %s %s exceeds limit %s",
                    policy.getPolicyCategory(), policy.getPolicyName(),
                    amount.getAmount(), amount.getCurrencyCode(), limitAmount);

            if ("HARD_BLOCK".equals(policy.getBreachAction())) {
                throw new PolicyBreachException(policy.getPolicyName(), policy.getPolicyCategory(), msg);
            }

            return new PolicyEvaluationResult(
                    policy.getPolicyName(), policy.getPolicyCategory(), BreachLevel.BREACH, msg);
        } else if (warnCmp > 0) {
            return new PolicyEvaluationResult(
                    policy.getPolicyName(), policy.getPolicyCategory(), BreachLevel.WARNING,
                    String.format("Approaching limit: %s/%s at %.1f%%",
                            policy.getPolicyCategory(), policy.getPolicyName(),
                            amount.getAmount().multiply(BigDecimal.valueOf(100))
                                    .divide(limitAmount, 1, java.math.RoundingMode.HALF_EVEN)));
        } else {
            return new PolicyEvaluationResult(
                    policy.getPolicyName(), policy.getPolicyCategory(), BreachLevel.OK, "Within limits");
        }
    }

    private void throwIfBlocked(List<PolicyEvaluationResult> results) {
        // PolicyBreachException for HARD_BLOCK is already thrown in evaluate()
        // This method handles BREACH results from SOFT_WARN / NOTIFY policies
        results.stream()
                .filter(r -> r.breachLevel() == BreachLevel.BREACH)
                .findFirst()
                .ifPresent(r -> log.warn("Policy breach (non-blocking): {}", r.message()));
    }
}
