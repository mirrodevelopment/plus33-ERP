package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

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

    public RiskKri createKri(Long riskId, String name, BigDecimal threshold) {
        RiskKri kri = new RiskKri();
        kri.setRiskId(riskId);
        kri.setIndicatorName(name);
        kri.setThresholdValue(threshold);
        kri.setBreached(false);
        return kriRepo.save(kri);
    }

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

    public List<EnterpriseRisk> getRisksByStatus(Long companyId, String status) {
        return riskRepo.findByCompanyIdAndStatus(companyId, status);
    }
}
