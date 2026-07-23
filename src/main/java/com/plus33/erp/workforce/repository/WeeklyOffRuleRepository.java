package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.WeeklyOffRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Repository
public interface WeeklyOffRuleRepository extends JpaRepository<WeeklyOffRule, Long> {

    /** All weekly off day names for a given policy group. */
    List<WeeklyOffRule> findByPolicyGroupId(Long policyGroupId);

    /** Resolve weekly off days for a policy group identified by code. */
    @Query("SELECT w FROM WeeklyOffRule w WHERE w.policyGroup.code = :groupCode")
    List<WeeklyOffRule> findByPolicyGroupCode(@Param("groupCode") String groupCode);

    void deleteByPolicyGroupId(Long policyGroupId);

    /**
     * Returns the set of weekly-off day name strings for quick lookup.
     * e.g. ["SUNDAY"] for India/UAE, ["SATURDAY","SUNDAY"] for EU.
     */
    default Set<String> resolveWeeklyOffDays(String groupCode) {
        return findByPolicyGroupCode(groupCode).stream()
                .map(WeeklyOffRule::getDayOfWeek)
                .collect(Collectors.toSet());
    }
}
