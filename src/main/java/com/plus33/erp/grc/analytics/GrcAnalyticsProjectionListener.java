package com.plus33.erp.grc.analytics;

import com.plus33.erp.grc.entity.GrcAnalyticsSnapshot;
import com.plus33.erp.grc.event.GrcEvent;
import com.plus33.erp.grc.repository.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Component
public class GrcAnalyticsProjectionListener {

    private final GrcAnalyticsSnapshotRepository snapshotRepo;
    private final EnterpriseRiskRepository riskRepo;
    private final AuditFindingRepository findingRepo;
    private final SodViolationRepository sodViolationRepo;
    private final CorrectiveActionPlanRepository capRepo;
    private final PolicyAcknowledgementRepository policyAckRepo;

    public GrcAnalyticsProjectionListener(GrcAnalyticsSnapshotRepository snapshotRepo,
                                          EnterpriseRiskRepository riskRepo,
                                          AuditFindingRepository findingRepo,
                                          SodViolationRepository sodViolationRepo,
                                          CorrectiveActionPlanRepository capRepo,
                                          PolicyAcknowledgementRepository policyAckRepo) {
        this.snapshotRepo = snapshotRepo;
        this.riskRepo = riskRepo;
        this.findingRepo = findingRepo;
        this.sodViolationRepo = sodViolationRepo;
        this.capRepo = capRepo;
        this.policyAckRepo = policyAckRepo;
    }

    @EventListener
    @Transactional
    public void onGrcEvent(GrcEvent event) {
        Long companyId = event.getCompanyId() != null ? event.getCompanyId() : 0L;
        switch (event.getEventType()) {
            case "RiskCreated", "RiskEscalated", "RiskAccepted" -> projectRiskMetrics(companyId);
            case "FindingRaised" -> projectFindingMetrics();
            case "CapClosed", "CapAssigned" -> projectCapMetrics();
            case "SodViolationDetected" -> projectSodMetrics();
            case "PolicyAcknowledged" -> projectPolicyMetrics();
        }
    }

    private void projectRiskMetrics(Long companyId) {
        long openRisks = riskRepo.findByCompanyIdAndStatus(companyId, "IDENTIFIED").size()
                       + riskRepo.findByCompanyIdAndStatus(companyId, "ASSESSED").size()
                       + riskRepo.findByCompanyIdAndStatus(companyId, "ESCALATED").size();
        recordSnapshot(companyId, "OPEN_RISK_COUNT", BigDecimal.valueOf(openRisks));
    }

    private void projectFindingMetrics() {
        long openFindings = findingRepo.countByStatus("OPEN");
        recordSnapshot(0L, "OPEN_FINDING_COUNT", BigDecimal.valueOf(openFindings));
    }

    private void projectCapMetrics() {
        long openCaps = capRepo.countByStatus("OPEN");
        recordSnapshot(0L, "OVERDUE_CAP_COUNT", BigDecimal.valueOf(openCaps));
    }

    private void projectSodMetrics() {
        long violations = sodViolationRepo.countByStatus("OPEN");
        recordSnapshot(0L, "SOD_VIOLATIONS_OPEN", BigDecimal.valueOf(violations));
    }

    private void projectPolicyMetrics() {
        recordSnapshot(0L, "POLICY_ACK_EVENT", BigDecimal.ONE);
    }

    private void recordSnapshot(Long companyId, String metric, BigDecimal value) {
        GrcAnalyticsSnapshot snap = new GrcAnalyticsSnapshot();
        snap.setCompanyId(companyId);
        snap.setMetricName(metric);
        snap.setMetricValue(value);
        snap.setRecordedDate(LocalDate.now());
        snapshotRepo.save(snap);
    }
}
