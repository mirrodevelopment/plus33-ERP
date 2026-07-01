package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.*;
import com.plus33.erp.project.repository.*;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ProjectLifecycleService {

    private final PpmProjectRepository ppmProjectRepository;
    private final ProjectWbsRepository wbsRepository;
    private final ProjectWbsVersionRepository wbsVersionRepository;
    private final ProjectChangeRequestRepository changeRequestRepository;
    private final ProjectEventBus eventBus;

    public ProjectLifecycleService(PpmProjectRepository ppmProjectRepository,
                                   ProjectWbsRepository wbsRepository,
                                   ProjectWbsVersionRepository wbsVersionRepository,
                                   ProjectChangeRequestRepository changeRequestRepository,
                                   ProjectEventBus eventBus) {
        this.ppmProjectRepository = ppmProjectRepository;
        this.wbsRepository = wbsRepository;
        this.wbsVersionRepository = wbsVersionRepository;
        this.changeRequestRepository = changeRequestRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public PpmProject createProject(Long companyId, Long customerId, String projectNumber, String name) {
        PpmProject proj = new PpmProject();
        proj.setCompanyId(companyId);
        proj.setCustomerId(customerId);
        proj.setProjectNumber(projectNumber);
        proj.setName(name);
        proj.setStatus("DRAFT");
        ppmProjectRepository.save(proj);

        // Initialize WBS
        ProjectWbs wbs = new ProjectWbs();
        wbs.setProjectId(proj.getId());
        wbs.setCurrentVersion(1);
        wbs.setStatus("DRAFT");
        wbsRepository.save(wbs);

        ProjectWbsVersion version = new ProjectWbsVersion();
        version.setWbsId(wbs.getId());
        version.setVersionNumber(1);
        version.setStatus("ACTIVE");
        version.setEffectiveDate(LocalDate.now());
        wbsVersionRepository.save(version);

        eventBus.publish("ProjectCreated", companyId, proj.getId(), "Project " + projectNumber + " created");
        return proj;
    }

    @Transactional
    public void transitionProjectStatus(Long projectId, String targetStatus) {
        PpmProject proj = ppmProjectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        proj.setStatus(targetStatus);
        proj.setUpdatedAt(LocalDateTime.now());
        ppmProjectRepository.save(proj);

        if ("BASELINE_APPROVED".equals(targetStatus)) {
            eventBus.publish("BaselineApproved", proj.getCompanyId(), projectId, "Project WBS baseline approved");
        } else if ("FINANCIAL_CLOSED".equals(targetStatus)) {
            eventBus.publish("ProjectClosed", proj.getCompanyId(), projectId, "Project closed financially");
        }
    }

    @Transactional
    public ProjectChangeRequest submitChangeRequest(Long projectId, String requestNumber, String changeType, String impact) {
        ProjectChangeRequest cr = new ProjectChangeRequest();
        cr.setProjectId(projectId);
        cr.setRequestNumber(requestNumber);
        cr.setChangeType(changeType);
        cr.setImpactAnalysis(impact);
        cr.setStatus("PENDING");
        changeRequestRepository.save(cr);
        return cr;
    }

    @Transactional
    public void approveChangeRequest(Long changeRequestId) {
        ProjectChangeRequest cr = changeRequestRepository.findById(changeRequestId)
                .orElseThrow(() -> new IllegalArgumentException("Change request not found"));
        cr.setStatus("APPROVED");
        changeRequestRepository.save(cr);

        PpmProject proj = ppmProjectRepository.findById(cr.getProjectId()).get();
        eventBus.publish("ChangeRequestApproved", proj.getCompanyId(), proj.getId(), "Change request " + cr.getRequestNumber() + " approved");

        // Increment WBS version on approval
        ProjectWbs wbs = wbsRepository.findByProjectId(proj.getId()).get();
        wbsVersionRepository.findByWbsId(wbs.getId()).forEach(v -> {
            v.setStatus("DEPRECATED");
            wbsVersionRepository.save(v);
        });

        int nextVer = wbs.getCurrentVersion() + 1;
        wbs.setCurrentVersion(nextVer);
        wbsRepository.save(wbs);

        ProjectWbsVersion newVer = new ProjectWbsVersion();
        newVer.setWbsId(wbs.getId());
        newVer.setVersionNumber(nextVer);
        newVer.setStatus("ACTIVE");
        newVer.setEffectiveDate(LocalDate.now());
        wbsVersionRepository.save(newVer);
    }
}
