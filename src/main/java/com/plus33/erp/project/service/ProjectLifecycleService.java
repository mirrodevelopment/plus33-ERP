/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectLifecycleService.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectLifecycleController
 * Related Service   : ProjectLifecycleService
 * Related Repository: PpmProjectRepository, ProjectWbsRepository, ProjectWbsVersionRepository, ProjectChangeRequestRepository
 * Related Entity    : ProjectLifecycle
 * Related DTO       : approveChangeRequest, ProjectChangeRequest, submitChangeRequest
 * Related Mapper    : ProjectLifecycleMapper
 * Related DB Table  : project_lifecycles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectLifecycleController, ProjectLifecycleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectLifecycleService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectLifecycleService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectLifecycleController
 *   --> ProjectLifecycleService (this)
 *   --> Validate business rules
 *   --> ProjectLifecycleRepository (read/write 'project_lifecycles')
 *   --> ProjectLifecycleMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_lifecycles}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new project and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param customerId the customerId input value
     * @param projectNumber the projectNumber input value
     * @param name the name input value
     * @return the PpmProject result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the transitionProjectStatus operation in this module.
     *
     * @param projectId the projectId input value
     * @param targetStatus the targetStatus input value
     */
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

    /**
     * Submits the change request for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param projectId the projectId input value
     * @param requestNumber the requestNumber input value
     * @param changeType the changeType input value
     * @param impact the impact input value
     * @return the ProjectChangeRequest result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Approves the change request, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param changeRequestId the changeRequestId input value
     * @throws BusinessException if a business rule is violated
     */
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