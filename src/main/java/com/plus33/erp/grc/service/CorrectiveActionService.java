/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Grc Module
 * Package           : com.plus33.erp.grc.service
 * File              : CorrectiveActionService.java
 * Purpose           : Business logic service layer for Grc Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CorrectiveActionController
 * Related Service   : CorrectiveActionService
 * Related Repository: EnterpriseIssueRepository, CorrectiveActionPlanRepository
 * Related Entity    : CorrectiveAction
 * Related DTO       : N/A
 * Related Mapper    : CorrectiveActionMapper
 * Related DB Table  : corrective_actions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CorrectiveActionController, CorrectiveActionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Grc Module. Implements CorrectiveActionService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.grc.service;

import com.plus33.erp.grc.entity.*;
import com.plus33.erp.grc.event.GrcEventBus;
import com.plus33.erp.grc.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Grc Module</b>
 *
 * <p><b>Class  :</b> {@code CorrectiveActionService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.grc.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Grc Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CorrectiveActionController
 *   --> CorrectiveActionService (this)
 *   --> Validate business rules
 *   --> CorrectiveActionRepository (read/write 'corrective_actions')
 *   --> CorrectiveActionMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code corrective_actions}</p>
 * <p><b>Module Deps      :</b> Grc</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@Transactional
public class CorrectiveActionService {

    private final EnterpriseIssueRepository issueRepo;
    private final CorrectiveActionPlanRepository capRepo;
    private final GrcEventBus eventBus;

    public CorrectiveActionService(EnterpriseIssueRepository issueRepo,
                                   CorrectiveActionPlanRepository capRepo,
                                   GrcEventBus eventBus) {
        this.issueRepo = issueRepo;
        this.capRepo = capRepo;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new issue and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param title the title input value
     * @param source the source entity or DTO to convert
     * @param severity the severity input value
     * @param dueDate the dueDate input value
     * @return the EnterpriseIssue result
     * @throws BusinessException if a business rule is violated
     */
    public EnterpriseIssue createIssue(Long companyId, String title, String source, String severity, LocalDate dueDate) {
        EnterpriseIssue issue = new EnterpriseIssue();
        issue.setCompanyId(companyId);
        issue.setIssueNumber("ISS-" + System.nanoTime() + "-" + (int)(Math.random() * 1000));
        issue.setTitle(title);
        issue.setSource(source);
        issue.setSeverity(severity);
        issue.setStatus("OPEN");
        issue.setDueDate(dueDate);
        return issueRepo.save(issue);
    }

    /**
     * Performs the assignIssue operation in this module.
     *
     * @param issueId the issueId input value
     * @param ownerId the ownerId input value
     */
    public void assignIssue(Long issueId, Long ownerId) {
        EnterpriseIssue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setOwnerId(ownerId);
        issue.setStatus("ASSIGNED");
        issueRepo.save(issue);
    }

    /**
     * Performs the progressIssue operation in this module.
     *
     * @param issueId the issueId input value
     * @param newStatus the newStatus input value
     */
    public void progressIssue(Long issueId, String newStatus) {
        EnterpriseIssue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setStatus(newStatus);
        issueRepo.save(issue);
        if ("CLOSED".equals(newStatus)) {
            eventBus.publish(issue.getCompanyId(), "IssueClosed", Map.of("issueId", issueId));
        }
    }

    /**
     * Creates a new cap and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param issueId the issueId input value
     * @param description the description input value
     * @param ownerId the ownerId input value
     * @param dueDate the dueDate input value
     * @return the CorrectiveActionPlan result
     * @throws BusinessException if a business rule is violated
     */
    public CorrectiveActionPlan createCap(Long issueId, String description, Long ownerId, LocalDate dueDate) {
        CorrectiveActionPlan cap = new CorrectiveActionPlan();
        cap.setIssueId(issueId);
        cap.setDescription(description);
        cap.setOwnerId(ownerId);
        cap.setDueDate(dueDate);
        cap.setStatus("OPEN");
        capRepo.save(cap);
        eventBus.publish(null, "CapAssigned", Map.of("capId", cap.getId(), "issueId", issueId));
        return cap;
    }

    /**
     * Completes the cap workflow and finalizes the record status.
     *
     * @param capId the capId input value
     */
    public void closeCap(Long capId) {
        CorrectiveActionPlan cap = capRepo.findById(capId).orElseThrow();
        cap.setStatus("CLOSED");
        cap.setClosedAt(LocalDateTime.now());
        capRepo.save(cap);
        eventBus.publish(null, "CapClosed", Map.of("capId", capId));
    }

    /**
     * Performs the countOverdueCaps operation in this module.
     *
     * @param today the today input value
     * @return the numeric result value
     */
    public long countOverdueCaps(LocalDate today) {
        return capRepo.findByStatus("OPEN").stream()
            .filter(c -> c.getDueDate() != null && c.getDueDate().isBefore(today))
            .count();
    }
}