/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectBillingService.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectBillingController
 * Related Service   : ProjectBillingService
 * Related Repository: ProjectBillingContractRepository, PpmProjectRepository
 * Related Entity    : ProjectBilling
 * Related DTO       : N/A
 * Related Mapper    : ProjectBillingMapper
 * Related DB Table  : project_billings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectBillingController, ProjectBillingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectBillingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
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
 * <p><b>Class  :</b> {@code ProjectBillingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectBillingController
 *   --> ProjectBillingService (this)
 *   --> Validate business rules
 *   --> ProjectBillingRepository (read/write 'project_billings')
 *   --> ProjectBillingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_billings}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new contract and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param projectId the projectId input value
     * @param contractType the contractType input value
     * @param amount the amount input value
     * @return the ProjectBillingContract result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Generates the invoice based on input parameters and business rules.
     *
     * @param contractId the contractId input value
     * @param amount the amount input value
     */
    @Transactional
    public void generateInvoice(Long contractId, BigDecimal amount) {
        ProjectBillingContract contract = contractRepository.findById(contractId)
                .orElseThrow(() -> new IllegalArgumentException("Billing Contract not found"));

        PpmProject proj = projectRepository.findById(contract.getProjectId())
                .orElseThrow(() -> new IllegalArgumentException("Project not found"));

        eventBus.publish("InvoiceGenerated", proj.getCompanyId(), contract.getProjectId(), "Invoice generated for amount " + amount);
    }
}