package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.PpmProject;
import com.plus33.erp.project.repository.PpmProjectRepository;
import com.plus33.erp.project.entity.ProjectBillingContract;
import com.plus33.erp.project.repository.ProjectBillingContractRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

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
