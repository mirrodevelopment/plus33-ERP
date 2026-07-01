package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectCost;
import com.plus33.erp.project.repository.ProjectCostRepository;
import com.plus33.erp.project.event.ProjectEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
public class ProjectFinancialService {

    private final ProjectCostRepository costRepository;
    private final ProjectEventBus eventBus;

    public ProjectFinancialService(ProjectCostRepository costRepository, ProjectEventBus eventBus) {
        this.costRepository = costRepository;
        this.eventBus = eventBus;
    }

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

    @Transactional(readOnly = true)
    public BigDecimal calculateWip(Long projectId) {
        return costRepository.findByProjectId(projectId).stream()
                .map(ProjectCost::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
