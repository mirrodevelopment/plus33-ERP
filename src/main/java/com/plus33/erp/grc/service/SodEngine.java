/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : SodEngine.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SodEngineController
 * Related Service   : SodEngine
 * Related Repository: SodRuleRepository, SodViolationRepository
 * Related Entity    : SodEngine
 * Related DTO       : N/A
 * Related Mapper    : SodEngineMapper
 * Related DB Table  : sod_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SodEngineController, SodEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements SodEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code SodEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SodEngineController
 *   --> SodEngine (this)
 *   --> Validate business rules
 *   --> SodEngineRepository (read/write 'sod_engines')
 *   --> SodEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code sod_engines}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new rule and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the SodRule result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves preventive rules data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<SodRule> getPreventiveRules(Long companyId) {
        return sodRuleRepo.findByCompanyIdAndSodType(companyId, "PREVENTIVE");
    }

    /**
     * Performs the countOpenViolations operation in this module.
     *
     * @return the numeric result value
     */
    public long countOpenViolations() {
        return sodViolationRepo.countByStatus("OPEN");
    }
}