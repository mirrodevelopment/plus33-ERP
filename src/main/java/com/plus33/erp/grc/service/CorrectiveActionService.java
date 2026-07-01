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

    public void assignIssue(Long issueId, Long ownerId) {
        EnterpriseIssue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setOwnerId(ownerId);
        issue.setStatus("ASSIGNED");
        issueRepo.save(issue);
    }

    public void progressIssue(Long issueId, String newStatus) {
        EnterpriseIssue issue = issueRepo.findById(issueId).orElseThrow();
        issue.setStatus(newStatus);
        issueRepo.save(issue);
        if ("CLOSED".equals(newStatus)) {
            eventBus.publish(issue.getCompanyId(), "IssueClosed", Map.of("issueId", issueId));
        }
    }

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

    public void closeCap(Long capId) {
        CorrectiveActionPlan cap = capRepo.findById(capId).orElseThrow();
        cap.setStatus("CLOSED");
        cap.setClosedAt(LocalDateTime.now());
        capRepo.save(cap);
        eventBus.publish(null, "CapClosed", Map.of("capId", capId));
    }

    public long countOverdueCaps(LocalDate today) {
        return capRepo.findByStatus("OPEN").stream()
            .filter(c -> c.getDueDate() != null && c.getDueDate().isBefore(today))
            .count();
    }
}
