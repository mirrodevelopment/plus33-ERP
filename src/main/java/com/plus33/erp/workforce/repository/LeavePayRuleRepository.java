package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeavePayRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeavePayRuleRepository extends JpaRepository<LeavePayRule, Long> {

    /**
     * Returns all pay tier rules for a policy rule, ordered by day_from ascending.
     * Used by the payroll engine to calculate tiered salary for each leave day.
     */
    @Query("SELECT r FROM LeavePayRule r WHERE r.policyRule.id = :policyRuleId ORDER BY r.dayFrom ASC")
    List<LeavePayRule> findByPolicyRuleId(@Param("policyRuleId") Long policyRuleId);

    /**
     * Find the applicable pay tier for a specific day index.
     * Returns the rule where dayFrom <= dayIndex <= dayTo (or dayTo IS NULL).
     */
    @Query("""
            SELECT r FROM LeavePayRule r
            WHERE r.policyRule.id = :policyRuleId
              AND r.dayFrom <= :dayIndex
              AND (r.dayTo IS NULL OR r.dayTo >= :dayIndex)
            ORDER BY r.dayFrom DESC
            """)
    List<LeavePayRule> findTierForDay(
            @Param("policyRuleId") Long policyRuleId,
            @Param("dayIndex")     int dayIndex);
}
