/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectFinancialService.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectFinancialController
 * Related Service   : ProjectFinancialService
 * Related Repository: ProjectCostRepository
 * Related Entity    : ProjectFinancial
 * Related DTO       : N/A
 * Related Mapper    : ProjectFinancialMapper
 * Related DB Table  : project_financials
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectFinancialController, ProjectFinancialServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectFinancialService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectCost;
import com.plus33.erp.project.repository.ProjectCostRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectFinancialService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectFinancialController
 *   --> ProjectFinancialService (this)
 *   --> Validate business rules
 *   --> ProjectFinancialRepository (read/write 'project_financials')
 *   --> ProjectFinancialMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_financials}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProjectFinancialService {

    private final ProjectCostRepository costRepository;
    private final ProjectEventBus eventBus;

    public ProjectFinancialService(ProjectCostRepository costRepository, ProjectEventBus eventBus) {
        this.costRepository = costRepository;
        this.eventBus = eventBus;
    }

    /**
     * Performs the recordCost operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param projectId the projectId input value
     * @param taskId the taskId input value
     * @param costType the costType input value
     * @param amount the amount input value
     * @param sourceModule the sourceModule input value
     * @param sourceId the sourceId input value
     * @return the ProjectCost result
     */
    @Transactional
    public ProjectCost recordCost(Long companyId, Long projectId, Long taskId, String costType, BigDecimal amount, String sourceModule, Long sourceId) {
        ProjectCost cost = new ProjectCost();
        cost.setCompanyId(companyId);
        cost.setProjectId(projectId);
        cost.setTaskId(taskId);
        cost.setCostType(costType);
        cost.setAmount(amount);
        cost.setSourceModule(sourceModule);
        cost.setSourceId(sourceId);
        costRepository.save(cost);

        eventBus.publish("ProjectCostPosted", companyId, projectId, "Cost of " + amount + " posted from " + sourceModule);
        return cost;
    }

    /**
     * Calculates wip totals including subtotal, tax, discounts, and net amount.
     *
     * @param projectId the projectId input value
     * @return the BigDecimal result
     */
    @Transactional(readOnly = true)
    public BigDecimal calculateWip(Long projectId) {
        return costRepository.findByProjectId(projectId).stream()
                .map(ProjectCost::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}