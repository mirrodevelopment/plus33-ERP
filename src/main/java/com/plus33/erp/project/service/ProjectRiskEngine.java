package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.PpmProject;
import com.plus33.erp.project.repository.PpmProjectRepository;
import com.plus33.erp.project.entity.ProjectRisk;
import com.plus33.erp.project.repository.ProjectRiskRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
