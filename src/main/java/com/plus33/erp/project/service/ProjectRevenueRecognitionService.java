/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectRevenueRecognitionService.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectRevenueRecognitionController
 * Related Service   : ProjectRevenueRecognitionService
 * Related Repository: ProjectBillingContractRepository, PpmProjectRepository
 * Related Entity    : ProjectRevenueRecognition
 * Related DTO       : N/A
 * Related Mapper    : ProjectRevenueRecognitionMapper
 * Related DB Table  : project_revenue_recognitions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectRevenueRecognitionController, ProjectRevenueRecognitionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectRevenueRecognitionService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.PpmProject;
import com.plus33.erp.project.repository.PpmProjectRepository;
import com.plus33.erp.project.entity.ProjectBillingContract;
import com.plus33.erp.project.repository.ProjectBillingContractRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectRevenueRecognitionService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectRevenueRecognitionController
 *   --> ProjectRevenueRecognitionService (this)
 *   --> Validate business rules
 *   --> ProjectRevenueRecognitionRepository (read/write 'project_revenue_recognitions')
 *   --> ProjectRevenueRecognitionMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_revenue_recognitions}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProjectRevenueRecognitionService {

    private final ProjectBillingContractRepository contractRepository;
    private final PpmProjectRepository projectRepository;
    private final ProjectEventBus eventBus;

    public ProjectRevenueRecognitionService(ProjectBillingContractRepository contractRepository,
                                            PpmProjectRepository projectRepository,
                                            ProjectEventBus eventBus) {
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the recognizeRevenuePoC operation in this module.
     *
     * @param projectId the projectId input value
     * @param percentComplete the percentComplete input value
     */
    @Transactional
    public void recognizeRevenuePoC(Long projectId, BigDecimal percentComplete) {
        ProjectBillingContract contract = contractRepository.findByProjectId(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Contract not found"));

        BigDecimal recognizedVal = contract.getBillingAmount().multiply(percentComplete).divide(new BigDecimal("100"), BigDecimal.ROUND_HALF_UP);
        contract.setRecognizedRevenue(recognizedVal);
        contractRepository.save(contract);

        PpmProject proj = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        eventBus.publish("RevenueRecognized", proj.getCompanyId(), projectId, "Revenue recognized: " + recognizedVal);
    }
}