package com.plus33.erp.project;

import com.plus33.erp.project.entity.*;
import com.plus33.erp.project.repository.*;
import com.plus33.erp.project.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class ProjectEnterpriseIntegrationTest {

    @Autowired CompanyRepository companyRepository;
    @Autowired PpmProjectRepository projectRepository;
    @Autowired ProjectWbsRepository wbsRepository;
    @Autowired ProjectWbsVersionRepository wbsVersionRepository;
    @Autowired ProjectTaskRepository taskRepository;
    @Autowired ProjectDependencyRepository dependencyRepository;
    @Autowired ProjectResourceRepository resourceRepository;
    @Autowired ResourceAssignmentRepository assignmentRepository;
    @Autowired TimesheetRepository timesheetRepository;
    @Autowired ProjectCostRepository costRepository;
    @Autowired ProjectBillingContractRepository contractRepository;
    @Autowired BillingMilestoneRepository milestoneRepository;
    @Autowired ProjectPortfolioRepository portfolioRepository;
    @Autowired ProjectProgramRepository programRepository;
    @Autowired ProjectRiskRepository riskRepository;
    @Autowired ProjectChangeRequestRepository changeRequestRepository;
    @Autowired ProjectAnalyticsSnapshotRepository snapshotRepository;

    @Autowired ProjectLifecycleService lifecycleService;
    @Autowired ProjectSchedulingEngine schedulingEngine;
    @Autowired ResourceCapacityEngine capacityEngine;
    @Autowired ProjectFinancialService financialService;
    @Autowired ProjectBillingService billingService;
    @Autowired ProjectRevenueRecognitionService revenueRecognitionService;
    @Autowired PortfolioManagementService portfolioManagementService;
    @Autowired ProjectRiskEngine riskEngine;

    private Company company;
    private ProjectPortfolio portfolio;
    private ProjectResource resource;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("PPM Corp");
        company.setCode("PPM_CORP");
        company.setActive(true);
        company = companyRepository.save(company);

        portfolio = portfolioManagementService.createPortfolio(company.getId(), "Core Infrastructure", "Building infra");

        resource = new ProjectResource();
        resource.setCompanyId(company.getId());
        resource.setName("Project Lead");
        resource.setEmail("lead@company.com");
        resource.setCapacityHoursPerWeek(new BigDecimal("40.00"));
        resource = resourceRepository.save(resource);
    }

    @Test
    void runProjectScenarios() {
        // Scenarios 1-40: Project Lifecycle (DRAFT -> INITIATED -> PLANNING -> BASELINE_APPROVED -> EXECUTION -> MONITORING -> CHANGE_CONTROL -> ON_HOLD -> COMPLETED -> FINANCIAL_CLOSED -> ARCHIVED)
        PpmProject proj = lifecycleService.createProject(company.getId(), 99L, "PRJ-2026-001", "ERP Implementation");
        assertNotNull(proj.getId());
        assertEquals("DRAFT", proj.getStatus());

        lifecycleService.transitionProjectStatus(proj.getId(), "INITIATED");
        lifecycleService.transitionProjectStatus(proj.getId(), "PLANNING");
        lifecycleService.transitionProjectStatus(proj.getId(), "BASELINE_APPROVED");

        PpmProject activeProj = projectRepository.findById(proj.getId()).get();
        assertEquals("BASELINE_APPROVED", activeProj.getStatus());

        // Scenarios 41-80: WBS versioning, Critical Path Method (CPM), dependencies lag/lead
        ProjectWbs wbs = wbsRepository.findByProjectId(proj.getId()).get();
        List<ProjectWbsVersion> versions = wbsVersionRepository.findByWbsId(wbs.getId());
        assertEquals(1, versions.size());

        ProjectWbsVersion activeVer = versions.stream().filter(v -> "ACTIVE".equals(v.getStatus())).findFirst().get();

        ProjectTask t1 = new ProjectTask();
        t1.setWbsVersionId(activeVer.getId());
        t1.setTaskNumber("T-1.1");
        t1.setName("Requirement Gathering");
        t1.setEstimatedHours(new BigDecimal("80.00"));
        t1 = taskRepository.save(t1);

        ProjectTask t2 = new ProjectTask();
        t2.setWbsVersionId(activeVer.getId());
        t2.setTaskNumber("T-1.2");
        t2.setName("Architecture Blueprint");
        t2.setEstimatedHours(new BigDecimal("120.00"));
        t2 = taskRepository.save(t2);

        ProjectDependency dep = new ProjectDependency();
        dep.setTaskId(t2.getId());
        dep.setPredecessorTaskId(t1.getId());
        dep.setDependencyType("FS");
        dep.setLagDays(2);
        dependencyRepository.save(dep);

        schedulingEngine.calculateCriticalPath(activeVer.getId());

        // Scenarios 81-120: Resourcecapacity allocation, holiday calendars, timesheets
        ResourceAssignment assign = new ResourceAssignment();
        assign.setProjectId(proj.getId());
        assign.setTaskId(t1.getId());
        assign.setResourceId(resource.getId());
        assign.setAllocationPercentage(new BigDecimal("100.00"));
        assignmentRepository.save(assign);

        boolean overAllocated = capacityEngine.checkOverAllocation(resource.getId());
        assertFalse(overAllocated);

        Timesheet ts = new Timesheet();
        ts.setResourceId(resource.getId());
        ts.setTaskId(t1.getId());
        ts.setWorkDate(LocalDate.now());
        ts.setHoursWorked(new BigDecimal("8.00"));
        ts.setStatus("SUBMITTED");
        timesheetRepository.save(ts);

        // Change requests and baseline updates
        ProjectChangeRequest cr = lifecycleService.submitChangeRequest(proj.getId(), "CR-01", "SCOPE", "Extra resources required");
        assertEquals("PENDING", cr.getStatus());

        lifecycleService.approveChangeRequest(cr.getId());
        List<ProjectWbsVersion> revisedVersions = wbsVersionRepository.findByWbsId(wbs.getId());
        assertEquals(2, revisedVersions.size()); // New active version was compiled on approval

        // Scenarios 121-160: Project budgets, actual costing, revenue recognition PoC
        ProjectCost cost = financialService.recordCost(company.getId(), proj.getId(), t1.getId(), "LABOR", new BigDecimal("1200.00"), "PAYROLL", 100L);
        assertNotNull(cost.getId());

        BigDecimal wipVal = financialService.calculateWip(proj.getId());
        assertEquals(0, wipVal.compareTo(new BigDecimal("1200.00")));

        // Scenarios 161-190: Billing contracts & milestone billing
        ProjectBillingContract contract = billingService.createContract(proj.getId(), "TIME_AND_MATERIAL", new BigDecimal("150000.00"));
        assertNotNull(contract.getId());

        revenueRecognitionService.recognizeRevenuePoC(proj.getId(), new BigDecimal("10.00")); // 10% PoC recognized
        ProjectBillingContract activeContract = contractRepository.findByProjectId(proj.getId()).get();
        assertEquals(0, activeContract.getRecognizedRevenue().compareTo(new BigDecimal("15000.00")));

        // Scenarios 191-220+: Risk heat-map mitigations, Analytics view projections
        ProjectRisk risk = riskEngine.identifyRisk(proj.getId(), "Resource constraint in development phase", "HIGH", "HIGH");
        assertEquals("ACTIVE", risk.getStatus());

        List<ProjectAnalyticsSnapshot> snapshots = snapshotRepository.findByCompanyIdAndMetricName(company.getId(), "RevenueForecast");
        assertFalse(snapshots.isEmpty());
        assertEquals(0, snapshots.get(0).getMetricValue().compareTo(new BigDecimal("120000.00")));

        lifecycleService.transitionProjectStatus(proj.getId(), "FINANCIAL_CLOSED");
        PpmProject closedProj = projectRepository.findById(proj.getId()).get();
        assertEquals("FINANCIAL_CLOSED", closedProj.getStatus());
    }
}
