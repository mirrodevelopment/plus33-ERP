/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.analytics
 * File              : GrcAnalyticsProjectionListener.java
 * Purpose           : Component of Grc Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GrcAnalyticsProjectionListenerController
 * Related Service   : GrcAnalyticsProjectionListenerService, GrcAnalyticsProjectionListenerServiceImpl
 * Related Repository: GrcAnalyticsSnapshotRepository, EnterpriseRiskRepository, AuditFindingRepository, SodViolationRepository, CorrectiveActionPlanRepository, PolicyAcknowledgementRepository
 * Related Entity    : GrcAnalyticsProjectionListener
 * Related DTO       : N/A
 * Related Mapper    : GrcAnalyticsProjectionListenerMapper
 * Related DB Table  : grc_analytics_projection_listeners
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Grc Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Grc Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.grc.analytics;

import com.plus33.erp.grc.entity.GrcAnalyticsSnapshot;
import com.plus33.erp.grc.event.GrcEvent;
import com.plus33.erp.grc.repository.*;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code GrcAnalyticsProjectionListener}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.analytics}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Grc Module.</p>
 *
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Handles the grc event event or exception in the business workflow.
     *
     * @param event the event input value
     */
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