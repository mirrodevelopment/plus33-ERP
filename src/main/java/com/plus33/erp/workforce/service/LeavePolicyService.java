package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.*;
import com.plus33.erp.workforce.repository.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeavePolicyService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service — hierarchical leave policy resolution.</p>
 *
 * <p>Resolves the effective leave policy for any employee at any point in time
 * by walking the hierarchy:</p>
 *
 * <pre>
 *   Employee
 *     → Store
 *       → Region
 *         → Company
 *           → Policy Group (INDIA | EU | UAE)
 *             → LeaveType System Default
 * </pre>
 *
 * <p>All results are returned as a {@link ResolvedPolicy} value object that
 * contains every configurable parameter: entitlement, accrual, notice period,
 * document thresholds, approval mode, carry-forward, encashment, etc.</p>
 *
 * <p>The payroll engine calls {@link #resolvePayPercentage(Long, int)} to
 * determine salary percentage for a given leave day number, using the tiered
 * {@link LeavePayRule} records — no hardcoded country logic.</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class LeavePolicyService {

    // New normalized repositories
    private final LeavePolicyGroupRepository    policyGroupRepository;
    private final LeavePolicyRuleRepository     policyRuleRepository;
    private final LeavePayRuleRepository        payRuleRepository;

    // Legacy repositories retained for backward compatibility
    private final CountryLeavePolicyRepository      countryPolicyRepository;
    private final CompanyLeavePolicyOverrideRepository companyOverrideRepository;

    public LeavePolicyService(
            LeavePolicyGroupRepository policyGroupRepository,
            LeavePolicyRuleRepository policyRuleRepository,
            LeavePayRuleRepository payRuleRepository,
            CountryLeavePolicyRepository countryPolicyRepository,
            CompanyLeavePolicyOverrideRepository companyOverrideRepository) {
        this.policyGroupRepository    = policyGroupRepository;
        this.policyRuleRepository     = policyRuleRepository;
        this.payRuleRepository        = payRuleRepository;
        this.countryPolicyRepository  = countryPolicyRepository;
        this.companyOverrideRepository = companyOverrideRepository;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIMARY API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Resolves the full policy for an employee's leave request.
     *
     * <p>Resolution order (highest priority first):
     * <ol>
     *   <li>Employee-level override (entity_policy_group_mappings, type=EMPLOYEE)</li>
     *   <li>Store-level override</li>
     *   <li>Region-level override</li>
     *   <li>Company-level override (legacy company_leave_policy_overrides + new mappings)</li>
     *   <li>Policy Group rule (leave_policy_rules)</li>
     *   <li>LeaveType system default</li>
     * </ol></p>
     *
     * @param employeeId   the employee making the request
     * @param storeId      the store the employee belongs to
     * @param companyId    the company ID
     * @param leaveType    the leave type entity
     * @param policyGroup  the resolved {@link LeavePolicyGroup} (INDIA/EU/UAE)
     * @param date         the effective date for version resolution
     * @return a fully resolved {@link ResolvedPolicy}
     */
    public ResolvedPolicy resolvePolicy(
            Long employeeId,
            Long storeId,
            Long companyId,
            LeaveType leaveType,
            LeavePolicyGroup policyGroup,
            LocalDate date) {

        // ── Step 1: Employee-level override (future extensibility) ────────────
        // Currently reserved; employee-level overrides resolved via entity_policy_group_mappings
        // if a specific mapping exists for EMPLOYEE type.

        // ── Step 2: Legacy Company Override (backward-compat) ─────────────────
        if (companyId != null && policyGroup != null) {
            // Map policy group code to legacy country code for backward compatibility
            String legacyCountryCode = mapGroupToLegacyCountry(policyGroup.getCode());
            List<CompanyLeavePolicyOverride> overrides = companyOverrideRepository
                    .findEffectiveOverrides(companyId, leaveType.getId(), legacyCountryCode, date);
            if (!overrides.isEmpty()) {
                CompanyLeavePolicyOverride o = overrides.get(0);
                return buildFromCompanyOverride(o, leaveType);
            }
        }

        // ── Step 3: Policy Group Rule (primary new path) ───────────────────────
        if (policyGroup != null) {
            Optional<LeavePolicyRule> ruleOpt = policyRuleRepository
                    .findEffectiveRule(policyGroup.getId(), leaveType.getId(), date);
            if (ruleOpt.isPresent()) {
                return buildFromPolicyRule(ruleOpt.get());
            }
        }

        // ── Step 4: Legacy Country Policy (backward-compat fallback) ──────────
        if (policyGroup != null) {
            String legacyCountryCode = mapGroupToLegacyCountry(policyGroup.getCode());
            List<CountryLeavePolicy> countryPolicies = countryPolicyRepository
                    .findEffectivePolicies(legacyCountryCode, leaveType.getId(), date);
            if (!countryPolicies.isEmpty()) {
                CountryLeavePolicy cp = countryPolicies.get(0);
                return buildFromCountryPolicy(cp, leaveType);
            }
        }

        // ── Step 5: LeaveType system default ──────────────────────────────────
        return buildFromLeaveTypeDefault(leaveType);
    }

    /**
     * Resolves the policy group for a store using the entity hierarchy.
     * Falls back to company mapping, then system default (EU).
     *
     * @param storeId   the store entity ID
     * @param companyId the company entity ID
     * @return the resolved {@link LeavePolicyGroup}, or empty if unmapped
     */
    public Optional<LeavePolicyGroup> resolvePolicyGroup(Long storeId, Long companyId) {
        // Try store-level first
        if (storeId != null) {
            Optional<LeavePolicyGroup> storeGroup = policyGroupRepository
                    .findEffectiveForEntity("STORE", storeId);
            if (storeGroup.isPresent()) return storeGroup;
        }
        // Fall back to company-level
        if (companyId != null) {
            return policyGroupRepository.findEffectiveForEntity("COMPANY", companyId);
        }
        // Return empty — caller decides final fallback
        return Optional.empty();
    }

    /**
     * Resolves the pay percentage for a specific leave day number using tiered pay rules.
     * Used by the payroll engine.
     *
     * <p>Example: UAE Sick Leave day 20 → 50% (Half Pay tier covers days 16-45).</p>
     *
     * @param policyRuleId the ID of the resolved {@link LeavePolicyRule}
     * @param dayIndex     1-based day number from start of the leave instance
     * @return pay percentage (0.00 – 100.00), defaulting to 100.00 if no tiers defined
     */
    public BigDecimal resolvePayPercentage(Long policyRuleId, int dayIndex) {
        List<LeavePayRule> tiers = payRuleRepository.findTierForDay(policyRuleId, dayIndex);
        if (!tiers.isEmpty()) {
            return tiers.get(0).getPayPercentage();
        }
        // No tiered rules: default to 100% (caller should already know is_paid from ResolvedPolicy)
        return BigDecimal.valueOf(100);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SIMPLIFIED OVERLOAD (backward compat — uses countryCode string)
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Legacy-compatible overload for existing callers that pass companyId, leaveType, countryCode.
     * Internally resolves the policy group from the country code string.
     *
     * @deprecated Prefer {@link #resolvePolicy(Long, Long, Long, LeaveType, LeavePolicyGroup, LocalDate)}
     */
    @Deprecated
    public ResolvedPolicy resolvePolicy(Long companyId, LeaveType leaveType, String countryCode, LocalDate date) {
        LeavePolicyGroup group = policyGroupRepository
                .findByCode(mapCountryToGroupCode(countryCode))
                .orElse(null);
        return resolvePolicy(null, null, companyId, leaveType, group, date);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // BUILDERS
    // ─────────────────────────────────────────────────────────────────────────

    private ResolvedPolicy buildFromPolicyRule(LeavePolicyRule r) {
        return new ResolvedPolicy(
                r.getId(),
                r.getDefaultEntitlement(),
                r.getMonthlyAccrual(),
                r.getEntitlementUnit(),
                r.getMaxConsecutiveDays(),
                r.getMaxPerYear(),
                r.getMinNoticeDays(),
                r.getDocumentRequiredAfterDays(),
                r.getAllowHalfDay(),
                r.getMinimumLeaveUnit(),
                r.getCarryForwardAllowed(),
                r.getCarryForwardLimit(),
                r.getCarryForwardExpiryMonths(),
                r.getEncashmentAllowed(),
                r.getMaximumEncashmentDays(),
                r.getMinimumBalanceForEncashment(),
                r.getApprovalLevel(),
                r.getApprovalMode(),
                r.getIsPaid(),
                r.getIsProtected(),
                r.getLifetimeLimit()
        );
    }

    private ResolvedPolicy buildFromCompanyOverride(CompanyLeavePolicyOverride o, LeaveType lt) {
        return new ResolvedPolicy(
                null, // no policy rule ID for legacy path
                o.getAnnualEntitlement() != null ? o.getAnnualEntitlement() : toDecimal(lt.getAnnualLimit()),
                o.getMonthlyAccrual() != null ? o.getMonthlyAccrual() : lt.getMonthlyAccrual(),
                "WORKING_DAYS",
                o.getMaxConsecutiveDays() != null ? o.getMaxConsecutiveDays() : lt.getMaxConsecutiveDays(),
                null,
                o.getMinNoticeDays() != null ? o.getMinNoticeDays() : lt.getMinNoticeDays(),
                lt.getRequiresDocument() ? 2 : 0,
                true,
                new BigDecimal("0.5"),
                o.getCarryForwardMax() != null && o.getCarryForwardMax().compareTo(BigDecimal.ZERO) > 0,
                o.getCarryForwardMax(),
                null,
                false, null, null,
                lt.getApprovalLevel(),
                lt.getProtectedLeave() ? "SYSTEM_APPROVAL" : "MANAGER_APPROVAL",
                lt.getPaid(),
                lt.getProtectedLeave(),
                null
        );
    }

    private ResolvedPolicy buildFromCountryPolicy(CountryLeavePolicy cp, LeaveType lt) {
        return new ResolvedPolicy(
                null,
                cp.getAnnualEntitlement() != null ? cp.getAnnualEntitlement() : toDecimal(lt.getAnnualLimit()),
                cp.getMonthlyAccrual() != null ? cp.getMonthlyAccrual() : lt.getMonthlyAccrual(),
                "WORKING_DAYS",
                cp.getMaxConsecutiveDays() != null ? cp.getMaxConsecutiveDays() : lt.getMaxConsecutiveDays(),
                null,
                cp.getMinNoticeDays() != null ? cp.getMinNoticeDays() : lt.getMinNoticeDays(),
                cp.getRequiresDocument() != null && cp.getRequiresDocument() ? 2 : 0,
                true,
                new BigDecimal("0.5"),
                cp.getCarryForwardMax() != null && cp.getCarryForwardMax().compareTo(BigDecimal.ZERO) > 0,
                cp.getCarryForwardMax(),
                null,
                false, null, null,
                cp.getApprovalLevel() != null ? cp.getApprovalLevel() : lt.getApprovalLevel(),
                cp.getIsProtected() != null && cp.getIsProtected() ? "SYSTEM_APPROVAL" : "MANAGER_APPROVAL",
                cp.getIsPaid() != null ? cp.getIsPaid() : lt.getPaid(),
                cp.getIsProtected() != null ? cp.getIsProtected() : lt.getProtectedLeave(),
                null
        );
    }

    private ResolvedPolicy buildFromLeaveTypeDefault(LeaveType lt) {
        return new ResolvedPolicy(
                null,
                toDecimal(lt.getAnnualLimit()),
                lt.getMonthlyAccrual(),
                "WORKING_DAYS",
                lt.getMaxConsecutiveDays(),
                null,
                lt.getMinNoticeDays(),
                lt.getRequiresDocument() ? 2 : 0,
                true,
                new BigDecimal("0.5"),
                lt.getCarryForward(),
                BigDecimal.ZERO,
                null,
                false, null, null,
                lt.getApprovalLevel(),
                lt.getProtectedLeave() ? "SYSTEM_APPROVAL" : "MANAGER_APPROVAL",
                lt.getPaid(),
                lt.getProtectedLeave(),
                null
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UTILITIES
    // ─────────────────────────────────────────────────────────────────────────

    /** Maps a raw country code string to a policy group code. */
    private String mapCountryToGroupCode(String countryCode) {
        if (countryCode == null) return "EU";
        return switch (countryCode.toUpperCase()) {
            case "IN", "IND" -> "INDIA";
            case "AE", "UAE" -> "UAE";
            default -> "EU"; // France (FR) and all other EU countries
        };
    }

    /** Maps a policy group code back to a legacy country code string for old repositories. */
    private String mapGroupToLegacyCountry(String groupCode) {
        return switch (groupCode) {
            case "INDIA" -> "IN";
            case "UAE"   -> "AE";
            default      -> "FR"; // EU default
        };
    }

    private BigDecimal toDecimal(Integer value) {
        return value != null ? BigDecimal.valueOf(value) : null;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // RESOLVED POLICY VALUE OBJECT
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Immutable value object containing all resolved leave policy parameters.
     * Produced by {@link LeavePolicyService#resolvePolicy} — consumed by
     * {@link LeaveServiceImpl}, the working day calculator, and the payroll engine.
     */
    public static class ResolvedPolicy {
        /** ID of the {@link LeavePolicyRule} used (null if resolved from legacy tables). */
        public final Long    policyRuleId;
        /** Total entitlement in days. Null = unlimited (EU Sick Leave). */
        public final BigDecimal annualEntitlement;
        /** Monthly accrual. Null = lump-sum grant. */
        public final BigDecimal monthlyAccrual;
        /** WORKING_DAYS | CALENDAR_DAYS | WEEKS */
        public final String  entitlementUnit;
        public final Integer maxConsecutiveDays;
        /** Hard year cap. Null = no cap. */
        public final Integer maxPerYear;
        public final Integer minNoticeDays;
        /**
         * Document required if leave days > this value.
         * 0 = never required.
         */
        public final int     documentRequiredAfterDays;
        public final boolean allowHalfDay;
        public final BigDecimal minimumLeaveUnit;
        public final boolean carryForwardAllowed;
        public final BigDecimal carryForwardLimit;
        public final Integer carryForwardExpiryMonths;
        public final boolean encashmentAllowed;
        public final BigDecimal maximumEncashmentDays;
        public final BigDecimal minimumBalanceForEncashment;
        /** SHIFT_SUPERVISOR | STORE_ADMIN | REGIONAL_ADMIN | HR | AUTO_APPROVED */
        public final String  approvalLevel;
        /** MANAGER_APPROVAL | HR_APPROVAL | AUTO_APPROVE | SYSTEM_APPROVAL */
        public final String  approvalMode;
        public final boolean isPaid;
        public final boolean isProtected;
        /** 1 = once per employment (e.g. Marriage Leave). Null = unlimited. */
        public final Integer lifetimeLimit;

        /** @deprecated Use {@link #isDocumentRequired(int)} or {@code documentRequiredAfterDays}. */
        @Deprecated public final boolean requiresDocument;
        /** @deprecated Use {@link #carryForwardLimit}. */
        @Deprecated public final BigDecimal carryForwardMax;

        public ResolvedPolicy(
                Long policyRuleId,
                BigDecimal annualEntitlement, BigDecimal monthlyAccrual,
                String entitlementUnit,
                Integer maxConsecutiveDays, Integer maxPerYear,
                Integer minNoticeDays, int documentRequiredAfterDays,
                boolean allowHalfDay, BigDecimal minimumLeaveUnit,
                boolean carryForwardAllowed, BigDecimal carryForwardLimit,
                Integer carryForwardExpiryMonths,
                boolean encashmentAllowed, BigDecimal maximumEncashmentDays,
                BigDecimal minimumBalanceForEncashment,
                String approvalLevel, String approvalMode,
                boolean isPaid, boolean isProtected,
                Integer lifetimeLimit) {
            this.policyRuleId             = policyRuleId;
            this.annualEntitlement        = annualEntitlement;
            this.monthlyAccrual           = monthlyAccrual;
            this.entitlementUnit          = entitlementUnit;
            this.maxConsecutiveDays       = maxConsecutiveDays;
            this.maxPerYear               = maxPerYear;
            this.minNoticeDays            = minNoticeDays;
            this.documentRequiredAfterDays = documentRequiredAfterDays;
            this.allowHalfDay             = allowHalfDay;
            this.minimumLeaveUnit         = minimumLeaveUnit;
            this.carryForwardAllowed      = carryForwardAllowed;
            this.carryForwardLimit        = carryForwardLimit;
            this.carryForwardExpiryMonths = carryForwardExpiryMonths;
            this.encashmentAllowed        = encashmentAllowed;
            this.maximumEncashmentDays    = maximumEncashmentDays;
            this.minimumBalanceForEncashment = minimumBalanceForEncashment;
            this.approvalLevel            = approvalLevel;
            this.approvalMode             = approvalMode;
            this.isPaid                   = isPaid;
            this.isProtected              = isProtected;
            this.lifetimeLimit            = lifetimeLimit;
            // Backward-compat aliases
            this.requiresDocument = documentRequiredAfterDays > 0;
            this.carryForwardMax  = carryForwardLimit;
        }

        /** Returns true if auto/system approval should be triggered immediately on submit. */
        public boolean isAutoApproved() {
            return "AUTO_APPROVE".equals(approvalMode)
                    || "SYSTEM_APPROVAL".equals(approvalMode)
                    || isProtected;
        }

        /** Returns true if a document is required for the given number of leave days. */
        public boolean isDocumentRequired(int consecutiveDays) {
            return documentRequiredAfterDays > 0 && consecutiveDays > documentRequiredAfterDays;
        }
    }
}
