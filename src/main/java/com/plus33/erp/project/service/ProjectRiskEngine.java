/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectRiskEngine.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectRiskEngineController
 * Related Service   : ProjectRiskEngine
 * Related Repository: ProjectRiskRepository, PpmProjectRepository
 * Related Entity    : ProjectRiskEngine
 * Related DTO       : N/A
 * Related Mapper    : ProjectRiskEngineMapper
 * Related DB Table  : project_risk_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectRiskEngineController, ProjectRiskEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectRiskEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.PpmProject;
import com.plus33.erp.project.repository.PpmProjectRepository;
import com.plus33.erp.project.entity.ProjectRisk;
import com.plus33.erp.project.repository.ProjectRiskRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectRiskEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectRiskEngineController
 *   --> ProjectRiskEngine (this)
 *   --> Validate business rules
 *   --> ProjectRiskEngineRepository (read/write 'project_risk_engines')
 *   --> ProjectRiskEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_risk_engines}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProjectRiskEngine {

    private final ProjectRiskRepository riskRepository;
    private final PpmProjectRepository projectRepository;
    private final ProjectEventBus eventBus;

    public ProjectRiskEngine(ProjectRiskRepository riskRepository,
                             PpmProjectRepository projectRepository,
                             ProjectEventBus eventBus) {
        this.riskRepository = riskRepository;
        this.projectRepository = projectRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the identifyRisk operation in this module.
     *
     * @param projectId the projectId input value
     * @param description the description input value
     * @param prob the prob input value
     * @param imp the imp input value
     * @return the ProjectRisk result
     */
    @Transactional
    public ProjectRisk identifyRisk(Long projectId, String description, String prob, String imp) {
        ProjectRisk risk = new ProjectRisk();
        risk.setProjectId(projectId);
        risk.setDescription(description);
        risk.setProbability(prob);
        risk.setImpact(imp);
        riskRepository.save(risk);

        if ("HIGH".equals(prob) && "HIGH".equals(imp)) {
            PpmProject proj = projectRepository.findById(projectId)
                    .orElseThrow(() -> new IllegalArgumentException("Project not found"));
            eventBus.publish("RiskEscalated", proj.getCompanyId(), projectId, "High exposure risk identified: " + description);
        }
        return risk;
    }
}