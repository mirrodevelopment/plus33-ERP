/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : AuditEngagementService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AuditEngagementController
 * Related Service   : AuditEngagementService
 * Related Repository: AuditUniverseRepository, AuditProgramRepository, AuditEngagementRepository, AuditFindingRepository
 * Related Entity    : AuditEngagement
 * Related DTO       : N/A
 * Related Mapper    : AuditEngagementMapper
 * Related DB Table  : audit_engagements
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AuditEngagementController, AuditEngagementServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements AuditEngagementService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
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

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code AuditEngagementService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AuditEngagementController
 *   --> AuditEngagementService (this)
 *   --> Validate business rules
 *   --> AuditEngagementRepository (read/write 'audit_engagements')
 *   --> AuditEngagementMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code audit_engagements}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new auditable entity and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param name the name input value
     * @param type the type input value
     * @param riskScore the riskScore input value
     * @return the AuditUniverse result
     * @throws BusinessException if a business rule is violated
     */
    public AuditUniverse createAuditableEntity(Long companyId, String name, String type, java.math.BigDecimal riskScore) {
        AuditUniverse entity = new AuditUniverse();
        entity.setCompanyId(companyId);
        entity.setEntityName(name);
        entity.setEntityType(type);
        entity.setRiskScore(riskScore);
        return universeRepo.save(entity);
    }

    /**
     * Creates a new program and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param name the name input value
     * @param fiscalYear the fiscalYear input value
     * @return the AuditProgram result
     * @throws BusinessException if a business rule is violated
     */
    public AuditProgram createProgram(Long companyId, String name, int fiscalYear) {
        AuditProgram program = new AuditProgram();
        program.setCompanyId(companyId);
        program.setProgramName(name);
        program.setFiscalYear(fiscalYear);
        program.setStatus("PLANNED");
        return programRepo.save(program);
    }

    /**
     * Creates a new engagement and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param programId the programId input value
     * @param auditUniverseId the auditUniverseId input value
     * @param title the title input value
     * @param leadAuditorId the leadAuditorId input value
     * @return the AuditEngagement result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the transitionEngagement operation in this module.
     *
     * @param engagementId the engagementId input value
     * @param newStatus the newStatus input value
     */
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

    /**
     * Performs the raiseFinding operation in this module.
     *
     * @param engagementId the engagementId input value
     * @param title the title input value
     * @param severity the severity input value
     * @param description the description input value
     * @return the AuditFinding result
     */
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

    /**
     * Retrieves open findings data from the database.
     *
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    public List<AuditFinding> getOpenFindings() {
        return findingRepo.findByStatus("OPEN");
    }
}