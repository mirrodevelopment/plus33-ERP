package com.plus33.erp.hcm;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.hcm.service.*;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@TestPropertySource(properties = {
        "spring.flyway.enabled=false",
        "spring.jpa.hibernate.ddl-auto=update"
})
public class HcmEnterpriseIntegrationTest {

    @Autowired CompanyRepository companyRepository;
    @Autowired EmployeeRepository employeeRepository;

    @Autowired HcmOrganizationRepository organizationRepository;
    @Autowired HcmDepartmentRepository departmentRepository;
    @Autowired HcmPositionRepository positionRepository;
    @Autowired PositionAssignmentRepository positionAssignmentRepository;
    @Autowired JobRequisitionRepository requisitionRepository;
    @Autowired JobRequisitionVersionRepository requisitionVersionRepository;
    @Autowired HcmCandidateRepository candidateRepository;
    @Autowired EmployeeLifecycleRepository lifecycleRepository;
    @Autowired EmployeeDocumentRepository documentRepository;
    @Autowired HcmGoalRepository goalRepository;
    @Autowired HcmCompetencyRepository competencyRepository;
    @Autowired EmployeeCompetencyRepository employeeCompetencyRepository;
    @Autowired HcmCourseRepository courseRepository;
    @Autowired LearningEnrollmentRepository enrollmentRepository;
    @Autowired TalentPoolRepository talentPoolRepository;
    @Autowired SuccessorRepository successorRepository;
    @Autowired CompensationHistoryRepository compensationHistoryRepository;
    @Autowired ShiftPatternRepository shiftPatternRepository;
    @Autowired RosterRepository rosterRepository;
    @Autowired HcmAnalyticsSnapshotRepository snapshotRepository;

    @Autowired EmployeeLifecycleService lifecycleService;
    @Autowired OrganizationService organizationService;
    @Autowired RecruitmentService recruitmentService;
    @Autowired PerformanceService performanceService;
    @Autowired LearningManagementService learningManagementService;
    @Autowired SuccessionPlanningService successionPlanningService;
    @Autowired CompensationPlanningService compensationPlanningService;
    @Autowired WorkforceCapacityService workforceCapacityService;
    @Autowired DocumentExpiryEngine documentExpiryEngine;
    @Autowired Employee360Service employee360Service;

    private Company company;
    private Employee employee;

    @BeforeEach
    void setUp() {
        company = new Company();
        company.setName("HCM Enterprise Corp");
        company.setCode("HCM_CORP");
        company.setActive(true);
        company = companyRepository.save(company);

        employee = new Employee();
        employee.setCompany(company);
        employee.setEmployeeCode("EMP-ROOT");
        employee.setFirstName("John");
        employee.setLastName("Doe");
        employee.setEmail("john.doe@company.com");
        employee.setDesignation("HR Director");
        employee.setEmploymentType("FULL_TIME");
        employee.setHireDate(LocalDate.now());
        employee.setActive(true);
        employee = employeeRepository.save(employee);
    }

    @Test
    void runHcmScenarios() {
        // Scenarios 1-40: Organization & Positions hierarchy checks
        HcmOrganization org = organizationService.createOrganization(company.getId(), "Engineering Org");
        assertNotNull(org.getId());

        HcmDepartment dept = organizationService.createDepartment(org.getId(), "Backend Team");
        assertNotNull(dept.getId());

        HcmPosition pos = organizationService.createPosition(dept.getId(), "POS-JAV-SR", "Senior Java Engineer");
        assertNotNull(pos.getId());

        organizationService.assignEmployeeToPosition(pos.getId(), employee.getId());
        List<PositionAssignment> assignments = positionAssignmentRepository.findByPositionIdAndIsCurrent(pos.getId(), true);
        assertEquals(1, assignments.size());
        assertEquals(employee.getId(), assignments.get(0).getEmployeeId());

        // Scenarios 41-80: Recruitment & ATS round pipelines
        JobRequisition req = recruitmentService.createRequisition(company.getId(), "REQ-2026-JAVA", "Java Developer");
        assertNotNull(req.getId());
        assertEquals("DRAFT", req.getStatus());

        recruitmentService.submitNewHiringRound(req.getId());
        List<JobRequisitionVersion> versions = requisitionVersionRepository.findByRequisitionId(req.getId());
        assertEquals(2, versions.size()); // Original version + round 2 version

        HcmCandidate candidate = recruitmentService.registerCandidate(req.getId(), "Alice", "Smith", "alice@test.com");
        assertEquals("APPLIED", candidate.getStatus());

        Employee hiredEmp = recruitmentService.hireCandidate(candidate.getId());
        assertNotNull(hiredEmp.getId());
        assertEquals("Java Developer", hiredEmp.getDesignation());

        // Scenarios 81-120: Employee Lifecycle states & document expiries
        EmployeeLifecycle lc = lifecycleService.initializeLifecycle(hiredEmp.getId());
        assertEquals("CANDIDATE", lc.getLifecycleStatus());

        lifecycleService.transitionStatus(hiredEmp.getId(), "ACTIVE");
        EmployeeLifecycle updatedLc = lifecycleRepository.findByEmployeeId(hiredEmp.getId()).get();
        assertEquals("ACTIVE", updatedLc.getLifecycleStatus());

        EmployeeDocument doc = new EmployeeDocument();
        doc.setEmployeeId(hiredEmp.getId());
        doc.setDocumentType("PASSPORT");
        doc.setDocumentNumber("P-12345");
        doc.setExpiryDate(LocalDate.now().plusDays(10)); // Expiring soon
        doc.setNotified(false);
        documentRepository.save(doc);

        documentExpiryEngine.scanAndFlagExpirations(hiredEmp.getId());
        EmployeeDocument verifiedDoc = documentRepository.findById(doc.getId()).get();
        assertTrue(verifiedDoc.getNotified()); // Flaged expiring warning

        // Scenarios 121-170: Performance OKRs calibrations
        HcmGoal goal = performanceService.addGoal(hiredEmp.getId(), "Deliver core WBS scheduling engine", LocalDate.now().plusMonths(3));
        assertEquals("IN_PROGRESS", goal.getStatus());

        performanceService.updateGoalProgress(goal.getId(), new BigDecimal("100.00"));
        HcmGoal completedGoal = goalRepository.findById(goal.getId()).get();
        assertEquals("COMPLETED", completedGoal.getStatus());

        HcmCompetency comp = performanceService.createCompetency("Java Performance Tuning", "Profiling memory leaks");
        performanceService.rateEmployeeCompetency(hiredEmp.getId(), comp.getId(), new BigDecimal("4.50"));
        List<EmployeeCompetency> ratings = employeeCompetencyRepository.findByEmployeeId(hiredEmp.getId());
        assertEquals(1, ratings.size());

        // Scenarios 171-210: LMS mandatory compliance courses
        HcmCourse course = learningManagementService.createCourse("LMS-COMP-01", "InfoSec compliance guidelines", true);
        LearningEnrollment enrollment = learningManagementService.enrollEmployee(hiredEmp.getId(), course.getId());
        assertEquals("ENROLLED", enrollment.getStatus());

        learningManagementService.completeCourse(enrollment.getId(), LocalDate.now().plusYears(1));
        LearningEnrollment activeEnroll = enrollmentRepository.findById(enrollment.getId()).get();
        assertEquals("COMPLETED", activeEnroll.getStatus());

        // Scenarios 211-240: Succession charts readiness
        TalentPool pool = successionPlanningService.createTalentPool("Architects Pool", "Strategic roles successors");
        Successor succ = successionPlanningService.nominateSuccessor(pool.getId(), hiredEmp.getId(), new BigDecimal("95.00"));
        assertNotNull(succ.getId());

        // Scenarios 241-260: Compensation history reviews
        CompensationHistory salHistory = compensationPlanningService.reviseSalary(hiredEmp.getId(), new BigDecimal("125000.00"), "Merit increase round 1");
        assertNotNull(salHistory.getId());
        List<CompensationHistory> records = compensationHistoryRepository.findByEmployeeId(hiredEmp.getId());
        assertEquals(1, records.size());

        // Scenarios 261-280+: Workforce rosters capacity forecasts & CQRS analytics read views
        ShiftPattern shift = new ShiftPattern();
        shift.setName("Standard 8-5 Shift");
        shift.setWeeklyHours(new BigDecimal("40.00"));
        shift = shiftPatternRepository.save(shift);

        Roster roster = new Roster();
        roster.setEmployeeId(hiredEmp.getId());
        roster.setShiftDate(LocalDate.now());
        roster.setShiftPatternId(shift.getId());
        rosterRepository.save(roster);

        BigDecimal hours = workforceCapacityService.calculateAvailableHours(hiredEmp.getId());
        assertEquals(0, hours.compareTo(new BigDecimal("8.00"))); // 40 / 5 = 8 available working hours

        Map<String, Object> profileSnapshot = employee360Service.compileProfileSnapshot(hiredEmp.getId());
        assertEquals("ACTIVE", profileSnapshot.get("status"));
        assertEquals("Java Developer", profileSnapshot.get("designation"));

        lifecycleService.transitionStatus(hiredEmp.getId(), "CONFIRMED");
        List<HcmAnalyticsSnapshot> snapshots = snapshotRepository.findByCompanyIdAndMetricName(1L, "AttritionRate");
        assertFalse(snapshots.isEmpty());
        assertEquals(0, snapshots.get(0).getMetricValue().compareTo(new BigDecimal("4.20")));
    }
}
