package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class AuditEngagementService {

    private static final Map<String, List<String>> TRANSITIONS = Map.of(
        "PLANNED",             List.of("FIELDWORK", "CANCELLED"),
        "FIELDWORK",           List.of("REVIEW", "ON_HOLD"),
        "REVIEW",              List.of("DRAFT_REPORT"),
        "DRAFT_REPORT",        List.of("MANAGEMENT_RESPONSE"),
        "MANAGEMENT_RESPONSE", List.of("FINAL_REPORT"),
        "FINAL_REPORT",        List.of("CLOSED"),
        "ON_HOLD",             List.of("FIELDWORK", "CANCELLED"),
        "CLOSED",              List.of(),
        "CANCELLED",           List.of()
    );

    private final AuditUniverseRepository universeRepo;
    private final AuditProgramRepository programRepo;
    private final AuditEngagementRepository engagementRepo;
    private final AuditFindingRepository findingRepo;
    private final GrcEventBus eventBus;

    public AuditEngagementService(AuditUniverseRepository universeRepo,
                                  AuditProgramRepository programRepo,
                                  AuditEngagementRepository engagementRepo,
                                  AuditFindingRepository findingRepo,
                                  GrcEventBus eventBus) {
        this.universeRepo = universeRepo;
        this.programRepo = programRepo;
        this.engagementRepo = engagementRepo;
        this.findingRepo = findingRepo;
        this.eventBus = eventBus;
    }

    public AuditUniverse createAuditableEntity(Long companyId, String name, String type, java.math.BigDecimal riskScore) {
        AuditUniverse entity = new AuditUniverse();
        entity.setCompanyId(companyId);
        entity.setEntityName(name);
        entity.setEntityType(type);
        entity.setRiskScore(riskScore);
        return universeRepo.save(entity);
    }

    public AuditProgram createProgram(Long companyId, String name, int fiscalYear) {
        AuditProgram program = new AuditProgram();
        program.setCompanyId(companyId);
        program.setProgramName(name);
        program.setFiscalYear(fiscalYear);
        program.setStatus("PLANNED");
        return programRepo.save(program);
    }

    public AuditEngagement createEngagement(Long programId, Long auditUniverseId, String title, Long leadAuditorId) {
        AuditEngagement engagement = new AuditEngagement();
        engagement.setProgramId(programId);
        engagement.setAuditUniverseId(auditUniverseId);
        engagement.setEngagementNumber("ENG-" + System.nanoTime() + "-" + (int)(Math.random() * 1000));
        engagement.setTitle(title);
        engagement.setLeadAuditorId(leadAuditorId);
        engagement.setStatus("PLANNED");
        engagementRepo.save(engagement);
        eventBus.publish(null, "AuditStarted", Map.of("engagementId", engagement.getId()));
        return engagement;
    }

    public void transitionEngagement(Long engagementId, String newStatus) {
        AuditEngagement engagement = engagementRepo.findById(engagementId)
            .orElseThrow(() -> new IllegalArgumentException("Engagement not found: " + engagementId));
        List<String> allowed = TRANSITIONS.getOrDefault(engagement.getStatus(), List.of());
        if (!allowed.contains(newStatus)) {
            throw new IllegalStateException("Invalid engagement transition: " + engagement.getStatus() + " → " + newStatus);
        }
        engagement.setStatus(newStatus);
        engagementRepo.save(engagement);
        if ("CLOSED".equals(newStatus)) {
            eventBus.publish(null, "AuditCompleted", Map.of("engagementId", engagementId));
        }
    }

    public AuditFinding raiseFinding(Long engagementId, String title, String severity, String description) {
        AuditFinding finding = new AuditFinding();
        finding.setEngagementId(engagementId);
        finding.setFindingNumber("FND-" + System.nanoTime() + "-" + (int)(Math.random() * 1000));
        finding.setTitle(title);
        finding.setSeverity(severity);
        finding.setStatus("OPEN");
        finding.setDescription(description);
        findingRepo.save(finding);
        eventBus.publish(null, "FindingRaised",
            Map.of("findingId", finding.getId(), "severity", severity));
        return finding;
    }

    public List<AuditFinding> getOpenFindings() {
        return findingRepo.findByStatus("OPEN");
    }
}
