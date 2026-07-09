/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : EnterpriseRiskService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EnterpriseRiskController
 * Related Service   : EnterpriseRiskService
 * Related Repository: EnterpriseRiskRepository, RiskAssessmentRepository, RiskKriRepository, RiskAppetiteStatementRepository
 * Related Entity    : EnterpriseRisk
 * Related DTO       : N/A
 * Related Mapper    : EnterpriseRiskMapper
 * Related DB Table  : enterprise_risks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EnterpriseRiskController, EnterpriseRiskServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements EnterpriseRiskService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code EnterpriseRiskService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * EnterpriseRiskController
 *   --> EnterpriseRiskService (this)
 *   --> Validate business rules
 *   --> EnterpriseRiskRepository (read/write 'enterprise_risks')
 *   --> EnterpriseRiskMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code enterprise_risks}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class EnterpriseRiskService {

    private static final Map<String, List<String>> ALLOWED_TRANSITIONS = Map.of(
        "IDENTIFIED", List.of("ASSESSED"),
        "ASSESSED",   List.of("MITIGATED", "ACCEPTED", "ESCALATED"),
        "MITIGATED",  List.of("MONITORED", "ACCEPTED"),
        "MONITORED",  List.of("ACCEPTED", "MITIGATED", "ESCALATED"),
        "ESCALATED",  List.of("ASSESSED", "ACCEPTED"),
        "ACCEPTED",   List.of("CLOSED", "REACTIVATED"),
        "CLOSED",     List.of("REACTIVATED"),
        "REACTIVATED",List.of("ASSESSED")
    );

    private final EnterpriseRiskRepository riskRepo;
    private final RiskAssessmentRepository assessmentRepo;
    private final RiskKriRepository kriRepo;
    private final RiskAppetiteStatementRepository appetiteRepo;
    private final GrcEventBus eventBus;

    public EnterpriseRiskService(
            EnterpriseRiskRepository riskRepo,
            RiskAssessmentRepository assessmentRepo,
            RiskKriRepository kriRepo,
            RiskAppetiteStatementRepository appetiteRepo,
            GrcEventBus eventBus) {
        this.riskRepo = riskRepo;
        this.assessmentRepo = assessmentRepo;
        this.kriRepo = kriRepo;
        this.appetiteRepo = appetiteRepo;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new risk and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the EnterpriseRisk result
     * @throws BusinessException if a business rule is violated
     */
    public EnterpriseRisk createRisk(Long companyId, String category, String domain, String title,
                                     BigDecimal inherentScore) {
        EnterpriseRisk risk = new EnterpriseRisk();
        risk.setCompanyId(companyId);
        risk.setRiskNumber("RISK-" + System.nanoTime() + "-" + (int)(Math.random() * 1000));
        risk.setCategory(category);
        risk.setDomain(domain);
        risk.setTitle(title);
        risk.setInherentScore(inherentScore);
        risk.setResidualScore(inherentScore);
        risk.setStatus("IDENTIFIED");
        riskRepo.save(risk);
        eventBus.publish(companyId, "RiskCreated", Map.of("riskId", risk.getId(), "category", category));
        return risk;
    }

    /**
     * Performs the assessRisk operation in this module.
     *
     * @param riskId the riskId input value
     * @param probability the probability input value
     * @param impact the impact input value
     * @param assessorId the assessorId input value
     * @return the RiskAssessment result
     */
    public RiskAssessment assessRisk(Long riskId, BigDecimal probability, BigDecimal impact, Long assessorId) {
        EnterpriseRisk risk = riskRepo.findById(riskId)
            .orElseThrow(() -> new IllegalArgumentException("Risk not found: " + riskId));
        transitionStatus(risk, "ASSESSED");

        BigDecimal residualScore = probability.multiply(impact);
        RiskAssessment assessment = new RiskAssessment();
        assessment.setRiskId(riskId);
        assessment.setProbability(probability);
        assessment.setImpact(impact);
        assessment.setResidualScore(residualScore);
        assessment.setAssessmentDate(LocalDate.now());
        assessment.setAssessorId(assessorId);
        assessmentRepo.save(assessment);

        risk.setResidualScore(residualScore);
        riskRepo.save(risk);

        checkRiskAppetite(risk, residualScore);
        return assessment;
    }

    /**
     * Performs the transitionStatus operation in this module.
     *
     * @param risk the risk input value
     * @param newStatus the newStatus input value
     */
    public void transitionStatus(EnterpriseRisk risk, String newStatus) {
        List<String> allowed = ALLOWED_TRANSITIONS.getOrDefault(risk.getStatus(), List.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Invalid transition: " + risk.getStatus() + " → " + newStatus);
        }
        risk.setStatus(newStatus);
        riskRepo.save(risk);
        if ("ESCALATED".equals(newStatus)) {
            eventBus.publish(risk.getCompanyId(), "RiskEscalated", Map.of("riskId", risk.getId()));
        }
    }

    /**
     * Creates a new kri and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param riskId the riskId input value
     * @param name the name input value
     * @param threshold the threshold input value
     * @return the RiskKri result
     * @throws BusinessException if a business rule is violated
     */
    public RiskKri createKri(Long riskId, String name, BigDecimal threshold) {
        RiskKri kri = new RiskKri();
        kri.setRiskId(riskId);
        kri.setIndicatorName(name);
        kri.setThresholdValue(threshold);
        kri.setBreached(false);
        return kriRepo.save(kri);
    }

    /**
     * Updates an existing kri value record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param kriId the kriId input value
     * @param currentValue the currentValue input value
     * @throws BusinessException if a business rule is violated
     */
    public void updateKriValue(Long kriId, BigDecimal currentValue) {
        RiskKri kri = kriRepo.findById(kriId)
            .orElseThrow(() -> new IllegalArgumentException("KRI not found: " + kriId));
        kri.setCurrentValue(currentValue);
        boolean breached = currentValue.compareTo(kri.getThresholdValue()) > 0;
        kri.setBreached(breached);
        kriRepo.save(kri);
        if (breached) {
            EnterpriseRisk risk = riskRepo.findById(kri.getRiskId()).orElseThrow();
            eventBus.publish(risk.getCompanyId(), "KriThresholdBreached",
                Map.of("kriId", kriId, "riskId", kri.getRiskId(), "value", currentValue));
        }
    }

    /**
     * Performs the defineAppetite operation in this module.
     *
     * @return the RiskAppetiteStatement result
     */
    public RiskAppetiteStatement defineAppetite(Long companyId, String riskCategory,
                                                BigDecimal maxResidual, BigDecimal escalationThreshold,
                                                String approvalAuthority,
                                                LocalDate from, LocalDate to) {
        RiskAppetiteStatement stmt = new RiskAppetiteStatement();
        stmt.setCompanyId(companyId);
        stmt.setRiskCategory(riskCategory);
        stmt.setMaxResidualScore(maxResidual);
        stmt.setEscalationThreshold(escalationThreshold);
        stmt.setApprovalAuthority(approvalAuthority);
        stmt.setEffectiveFrom(from);
        stmt.setEffectiveTo(to);
        return appetiteRepo.save(stmt);
    }

    private void checkRiskAppetite(EnterpriseRisk risk, BigDecimal residualScore) {
        appetiteRepo.findByCompanyIdAndRiskCategoryAndEffectiveFromLessThanEqualAndEffectiveToGreaterThanEqual(
            risk.getCompanyId(), risk.getCategory(), LocalDate.now(), LocalDate.now()
        ).ifPresent(stmt -> {
            if (residualScore.compareTo(stmt.getEscalationThreshold()) > 0) {
                eventBus.publish(risk.getCompanyId(), "RiskEscalated",
                    Map.of("riskId", risk.getId(), "residualScore", residualScore,
                           "threshold", stmt.getEscalationThreshold()));
            }
        });
    }

    /**
     * Retrieves risks by status data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param status status filter for narrowing query results
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<EnterpriseRisk> getRisksByStatus(Long companyId, String status) {
        return riskRepo.findByCompanyIdAndStatus(companyId, status);
    }
}