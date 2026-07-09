package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.CompanyLeavePolicyOverride;
import com.plus33.erp.workforce.entity.CountryLeavePolicy;
import com.plus33.erp.workforce.entity.LeaveType;
import com.plus33.erp.workforce.repository.CompanyLeavePolicyOverrideRepository;
import com.plus33.erp.workforce.repository.CountryLeavePolicyRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class LeavePolicyService {

    private final CountryLeavePolicyRepository countryPolicyRepository;
    private final CompanyLeavePolicyOverrideRepository companyOverrideRepository;

    public LeavePolicyService(
            CountryLeavePolicyRepository countryPolicyRepository,
            CompanyLeavePolicyOverrideRepository companyOverrideRepository) {
        this.countryPolicyRepository = countryPolicyRepository;
        this.companyOverrideRepository = companyOverrideRepository;
    }

    public ResolvedPolicy resolvePolicy(Long companyId, LeaveType leaveType, String countryCode, LocalDate date) {
        // Priority 1: Company Override (highest version in date range)
        List<CompanyLeavePolicyOverride> overrides = companyOverrideRepository
                .findEffectiveOverrides(companyId, leaveType.getId(), countryCode, date);
        if (!overrides.isEmpty()) {
            CompanyLeavePolicyOverride o = overrides.get(0); // Query sorted by version desc
            return new ResolvedPolicy(
                    o.getAnnualEntitlement() != null ? o.getAnnualEntitlement() : leaveType.getAnnualLimit() != null ? BigDecimal.valueOf(leaveType.getAnnualLimit()) : null,
                    o.getMonthlyAccrual() != null ? o.getMonthlyAccrual() : leaveType.getMonthlyAccrual(),
                    o.getMaxConsecutiveDays() != null ? o.getMaxConsecutiveDays() : leaveType.getMaxConsecutiveDays(),
                    o.getCarryForwardMax() != null ? o.getCarryForwardMax() : BigDecimal.ZERO,
                    o.getMinNoticeDays() != null ? o.getMinNoticeDays() : leaveType.getMinNoticeDays(),
                    leaveType.getRequiresDocument(),
                    leaveType.getApprovalLevel(),
                    leaveType.getPaid(),
                    leaveType.getProtectedLeave()
            );
        }

        // Priority 2: Country Policy (highest version in date range)
        List<CountryLeavePolicy> countryPolicies = countryPolicyRepository
                .findEffectivePolicies(countryCode, leaveType.getId(), date);
        if (!countryPolicies.isEmpty()) {
            CountryLeavePolicy cp = countryPolicies.get(0);
            return new ResolvedPolicy(
                    cp.getAnnualEntitlement() != null ? cp.getAnnualEntitlement() : leaveType.getAnnualLimit() != null ? BigDecimal.valueOf(leaveType.getAnnualLimit()) : null,
                    cp.getMonthlyAccrual() != null ? cp.getMonthlyAccrual() : leaveType.getMonthlyAccrual(),
                    cp.getMaxConsecutiveDays() != null ? cp.getMaxConsecutiveDays() : leaveType.getMaxConsecutiveDays(),
                    cp.getCarryForwardMax() != null ? cp.getCarryForwardMax() : BigDecimal.ZERO,
                    cp.getMinNoticeDays() != null ? cp.getMinNoticeDays() : leaveType.getMinNoticeDays(),
                    cp.getRequiresDocument() != null ? cp.getRequiresDocument() : leaveType.getRequiresDocument(),
                    cp.getApprovalLevel() != null ? cp.getApprovalLevel() : leaveType.getApprovalLevel(),
                    cp.getIsPaid() != null ? cp.getIsPaid() : leaveType.getPaid(),
                    cp.getIsProtected() != null ? cp.getIsProtected() : leaveType.getProtectedLeave()
            );
        }

        // Priority 3: Fallback to LeaveType default fields
        return new ResolvedPolicy(
                leaveType.getAnnualLimit() != null ? BigDecimal.valueOf(leaveType.getAnnualLimit()) : null,
                leaveType.getMonthlyAccrual(),
                leaveType.getMaxConsecutiveDays(),
                BigDecimal.ZERO,
                leaveType.getMinNoticeDays(),
                leaveType.getRequiresDocument(),
                leaveType.getApprovalLevel(),
                leaveType.getPaid(),
                leaveType.getProtectedLeave()
        );
    }

    public static class ResolvedPolicy {
        public final BigDecimal annualEntitlement;
        public final BigDecimal monthlyAccrual;
        public final Integer maxConsecutiveDays;
        public final BigDecimal carryForwardMax;
        public final Integer minNoticeDays;
        public final boolean requiresDocument;
        public final String approvalLevel;
        public final boolean isPaid;
        public final boolean isProtected;

        public ResolvedPolicy(BigDecimal annualEntitlement, BigDecimal monthlyAccrual, Integer maxConsecutiveDays,
                              BigDecimal carryForwardMax, Integer minNoticeDays, boolean requiresDocument,
                              String approvalLevel, boolean isPaid, boolean isProtected) {
            this.annualEntitlement = annualEntitlement;
            this.monthlyAccrual = monthlyAccrual;
            this.maxConsecutiveDays = maxConsecutiveDays;
            this.carryForwardMax = carryForwardMax;
            this.minNoticeDays = minNoticeDays;
            this.requiresDocument = requiresDocument;
            this.approvalLevel = approvalLevel;
            this.isPaid = isPaid;
            this.isProtected = isProtected;
        }
    }
}
