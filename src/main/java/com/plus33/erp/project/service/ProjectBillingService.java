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
public class ProjectBillingService {

    private final ProjectBillingContractRepository contractRepository;
    private final PpmProjectRepository projectRepository;
    private final ProjectEventBus eventBus;

    public ProjectBillingService(ProjectBillingContractRepository contractRepository,
                                 PpmProjectRepository projectRepository,
                                 ProjectEventBus eventBus) {
        this.contractRepository = contractRepository;
        this.projectRepository = projectRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public ProjectBillingContract createContract(Long projectId, String contractType, BigDecimal amount) {
        ProjectBillingContract contract = new ProjectBillingContract();
        contract.setProjectId(projectId);
        contract.setContractType(contractType);
        contract.setBillingAmount(amount);
        contract.setStatus("DRAFT");
        contractRepository.save(contract);
        return contract;
    }

    @Transactional
    public void generateInvoice(Long contractId, BigDecimal amount) {
        ProjectBillingContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Billing Contract not found"));

        PpmProject proj = projectRepository.findById(contract.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        eventBus.publish("InvoiceGenerated", proj.getCompanyId(), contract.getProjectId(), "Invoice generated for amount " + amount);
    }
}
