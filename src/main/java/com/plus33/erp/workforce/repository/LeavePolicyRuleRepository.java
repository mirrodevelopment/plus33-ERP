package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeavePolicyRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeavePolicyRuleRepository extends JpaRepository<LeavePolicyRule, Long> {

    /**
     * Find the most recent effective rule for a policy group and leave type on a given date.
     * Returns the highest version active within the date range.
     */
    @Query("""
            SELECT r FROM LeavePolicyRule r
            WHERE r.policyGroup.id = :policyGroupId
              AND r.leaveType.id   = :leaveTypeId
              AND r.effectiveFrom  <= :date
              AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date)
            ORDER BY r.version DESC
            """)
    List<LeavePolicyRule> findEffectiveRules(
            @Param("policyGroupId") Long policyGroupId,
            @Param("leaveTypeId")   Long leaveTypeId,
            @Param("date")          LocalDate date);

    /** Convenience: returns only the top (most recent version) effective rule. */
    default Optional<LeavePolicyRule> findEffectiveRule(Long policyGroupId, Long leaveTypeId, LocalDate date) {
        List<LeavePolicyRule> results = findEffectiveRules(policyGroupId, leaveTypeId, date);
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    /** All rules for a given policy group (for admin views). */
    List<LeavePolicyRule> findByPolicyGroupIdOrderByLeaveTypeIdAscVersionDesc(Long policyGroupId);

    List<LeavePolicyRule> findByPolicyGroupId(Long policyGroupId);

    List<LeavePolicyRule> findByPolicyGroupIdAndLeaveTypeId(Long policyGroupId, Long leaveTypeId);

    /** All rules for a given leave type across all groups (cross-country compare). */
    List<LeavePolicyRule> findByLeaveTypeId(Long leaveTypeId);
}
