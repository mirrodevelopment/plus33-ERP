package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class SodEngine {

    private final SodRuleRepository sodRuleRepo;
    private final SodViolationRepository sodViolationRepo;
    private final GrcEventBus eventBus;

    public SodEngine(SodRuleRepository sodRuleRepo,
                     SodViolationRepository sodViolationRepo,
                     GrcEventBus eventBus) {
        this.sodRuleRepo = sodRuleRepo;
        this.sodViolationRepo = sodViolationRepo;
        this.eventBus = eventBus;
    }

    public SodRule createRule(Long companyId, String ruleName, String roleA, String roleB,
                               String riskLevel, String sodType) {
        SodRule rule = new SodRule();
        rule.setCompanyId(companyId);
        rule.setRuleName(ruleName);
        rule.setRoleA(roleA);
        rule.setRoleB(roleB);
        rule.setRiskLevel(riskLevel);
        rule.setSodType(sodType);
        return sodRuleRepo.save(rule);
    }

    /**
     * Simulate SoD check — returns list of conflicting rules for a proposed role assignment
     * without persisting any violation. Standard SAP GRC Access Control simulation mode.
     */
    public List<SodRule> simulateRoleAssignment(Long companyId, String proposedRole, List<String> currentRoles) {
        List<SodRule> allRules = sodRuleRepo.findByCompanyId(companyId);
        return allRules.stream()
            .filter(rule ->
                (rule.getRoleA().equals(proposedRole) && currentRoles.contains(rule.getRoleB())) ||
                (rule.getRoleB().equals(proposedRole) && currentRoles.contains(rule.getRoleA()))
            )
            .collect(Collectors.toList());
    }

    /**
     * Detective SoD — detect and record violations for an existing user role combination.
     */
    public List<SodViolation> detectViolations(Long companyId, Long userId, List<String> userRoles) {
        List<SodRule> rules = sodRuleRepo.findByCompanyId(companyId);
        List<SodViolation> violations = rules.stream()
            .filter(rule -> userRoles.contains(rule.getRoleA()) && userRoles.contains(rule.getRoleB()))
            .map(rule -> {
                SodViolation v = new SodViolation();
                v.setSodRuleId(rule.getId());
                v.setUserId(userId);
                v.setStatus("OPEN");
                return sodViolationRepo.save(v);
            })
            .collect(Collectors.toList());

        violations.forEach(v ->
            eventBus.publish(companyId, "SodViolationDetected",
                Map.of("violationId", v.getId(), "userId", userId))
        );
        return violations;
    }

    public List<SodRule> getPreventiveRules(Long companyId) {
        return sodRuleRepo.findByCompanyIdAndSodType(companyId, "PREVENTIVE");
    }

    public long countOpenViolations() {
        return sodViolationRepo.countByStatus("OPEN");
    }
}
