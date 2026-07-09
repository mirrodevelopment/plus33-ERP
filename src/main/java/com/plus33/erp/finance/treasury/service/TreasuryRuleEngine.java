/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryRuleEngine.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryRuleEngineController
 * Related Service   : TreasuryRuleEngineService, TreasuryRuleEngineServiceImpl
 * Related Repository: TreasuryRuleEngineRepository
 * Related Entity    : TreasuryRuleEngine
 * Related DTO       : N/A
 * Related Mapper    : TreasuryRuleEngineMapper
 * Related DB Table  : treasury_rule_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.shared.MoneyAmount;

import java.time.LocalDate;
import java.util.List;

/**
 * Centralized policy evaluation engine.
 *
 * Every treasury operation that involves monetary values must be validated
 * through this engine before execution. The engine evaluates matching
 * {@link com.plus33.erp.finance.treasury.entity.TreasuryRiskPolicy} rules
 * and either blocks, warns, or allows the operation.
 */
public interface TreasuryRuleEngine {

    /**
     * Evaluates a payment against all applicable limits.
     *
     * @param companyId   owning company
     * @param amount      payment amount in its currency
     * @param paymentDate the intended payment date
     * @throws PolicyBreachException if a HARD_BLOCK policy is violated
     */
    void evaluatePayment(Long companyId, MoneyAmount amount, LocalDate paymentDate);

    /**
     * Evaluates an FX trade against all applicable FX limits.
     *
     * @param companyId   owning company
     * @param buyCurrency target currency being bought
     * @param buyAmount   amount being bought
     * @param tradeDate   intended trade date
     * @throws PolicyBreachException if a HARD_BLOCK policy is violated
     */
    void evaluateFxTrade(Long companyId, String buyCurrency, MoneyAmount buyAmount, LocalDate tradeDate);

    /**
     * Evaluates an investment against concentration and investment limits.
     *
     * @param companyId      owning company
     * @param investmentType type of investment (FIXED_DEPOSIT, MONEY_MARKET_FUND, etc.)
     * @param amount         investment amount
     * @param investmentDate intended investment date
     * @throws PolicyBreachException if a HARD_BLOCK policy is violated
     */
    void evaluateInvestment(Long companyId, String investmentType,
                            MoneyAmount amount, LocalDate investmentDate);

    /**
     * Evaluates bank exposure after a proposed transfer.
     *
     * @param companyId owning company
     * @param bankId    target bank
     * @param amount    proposed transfer amount
     * @param date      transfer date
     * @throws PolicyBreachException if a HARD_BLOCK policy is violated
     */
    void evaluateBankExposure(Long companyId, Long bankId, MoneyAmount amount, LocalDate date);

    /**
     * Returns a list of all policy evaluation results for an operation
     * (useful for building warning messages when SOFT_WARN rules fire).
     */
    List<PolicyEvaluationResult> evaluateAll(Long companyId, String operationType,
                                             MoneyAmount amount, LocalDate date);

    // ── Inner Types ────────────────────────────────────────────────────────────

    record PolicyEvaluationResult(
            String policyName,
            String policyCategory,
            BreachLevel breachLevel,
            String message) {}

    enum BreachLevel {
        OK,          // within limits
        WARNING,     // approaching threshold
        BREACH       // limit exceeded
    }

    class PolicyBreachException extends RuntimeException {
        private final String policyName;
        private final String policyCategory;

        public PolicyBreachException(String policyName, String policyCategory, String message) {
            super(message);
            this.policyName = policyName;
            this.policyCategory = policyCategory;
        }

        public String getPolicyName() { return policyName; }
        public String getPolicyCategory() { return policyCategory; }
    }
}
