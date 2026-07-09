/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : RecruitmentService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RecruitmentController
 * Related Service   : RecruitmentService
 * Related Repository: JobRequisitionRepository, JobRequisitionVersionRepository, HcmCandidateRepository, EmployeeRepository, CompanyRepository
 * Related Entity    : Recruitment
 * Related DTO       : N/A
 * Related Mapper    : RecruitmentMapper
 * Related DB Table  : recruitments
 * Related REST APIs : N/A
 * Depends On        : Workforce Module, Organization Module
 * Used By           : RecruitmentController, RecruitmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements RecruitmentService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code RecruitmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * RecruitmentController
 *   --> RecruitmentService (this)
 *   --> Validate business rules
 *   --> RecruitmentRepository (read/write 'recruitments')
 *   --> RecruitmentMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code recruitments}</p>
 * <p><b>Module Deps      :</b> Hcm, Workforce, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class RecruitmentService {

    private final JobRequisitionRepository requisitionRepository;
    private final JobRequisitionVersionRepository versionRepository;
    private final HcmCandidateRepository candidateRepository;
    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final HcmEventBus eventBus;

    public RecruitmentService(JobRequisitionRepository requisitionRepository,
                              JobRequisitionVersionRepository versionRepository,
                              HcmCandidateRepository candidateRepository,
                              EmployeeRepository employeeRepository,
                              CompanyRepository companyRepository,
                              HcmEventBus eventBus) {
        this.requisitionRepository = requisitionRepository;
        this.versionRepository = versionRepository;
        this.candidateRepository = candidateRepository;
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new requisition and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param requisitionNumber the requisitionNumber input value
     * @param title the title input value
     * @return the JobRequisition result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public JobRequisition createRequisition(Long companyId, String requisitionNumber, String title) {
        JobRequisition req = new JobRequisition();
        req.setCompanyId(companyId);
        req.setRequisitionNumber(requisitionNumber);
        req.setTitle(title);
        req.setStatus("DRAFT");
        requisitionRepository.save(req);

        JobRequisitionVersion version = new JobRequisitionVersion();
        version.setRequisitionId(req.getId());
        version.setVersionNumber(1);
        version.setStatus("ACTIVE");
        version.setEffectiveDate(LocalDate.now());
        versionRepository.save(version);

        return req;
    }

    /**
     * Submits the new hiring round for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param requisitionId the requisitionId input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void submitNewHiringRound(Long requisitionId) {
        if (!requisitionRepository.existsById(requisitionId)) {
            throw new IllegalArgumentException("Requisition not found");
        }

        versionRepository.findByRequisitionId(requisitionId).forEach(v -> {
            v.setStatus("DEPRECATED");
            versionRepository.save(v);
        });

        int nextRound = versionRepository.findByRequisitionId(requisitionId).size() + 1;
        JobRequisitionVersion newVer = new JobRequisitionVersion();
        newVer.setRequisitionId(requisitionId);
        newVer.setVersionNumber(nextRound);
        newVer.setStatus("ACTIVE");
        newVer.setEffectiveDate(LocalDate.now());
        versionRepository.save(newVer);
    }

    /**
     * Creates a new candidate and persists it to the database.
     *
     * @param requisitionId the requisitionId input value
     * @param firstName the firstName input value
     * @param lastName the lastName input value
     * @param email the email input value
     * @return the HcmCandidate result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public HcmCandidate registerCandidate(Long requisitionId, String firstName, String lastName, String email) {
        HcmCandidate c = new HcmCandidate();
        c.setRequisitionId(requisitionId);
        c.setFirstName(firstName);
        c.setLastName(lastName);
        c.setEmail(email);
        c.setStatus("APPLIED");
        candidateRepository.save(c);
        return c;
    }

    /**
     * Performs the hireCandidate operation in this module.
     *
     * @param candidateId the candidateId input value
     * @return the Employee result
     */
    @Transactional
    public Employee hireCandidate(Long candidateId) {
        HcmCandidate c = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new IllegalArgumentException("Candidate not found"));
        c.setStatus("HIRED");
        candidateRepository.save(c);

        JobRequisition req = requisitionRepository.findById(c.getRequisitionId()).get();
        Company company = companyRepository.findById(req.getCompanyId()).get();

        Employee emp = new Employee();
        emp.setCompany(company);
        emp.setEmployeeCode("EMP-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        emp.setFirstName(c.getFirstName());
        emp.setLastName(c.getLastName());
        emp.setEmail(c.getEmail());
        emp.setDesignation(req.getTitle());
        emp.setEmploymentType("FULL_TIME");
        emp.setHireDate(LocalDate.now());
        emp.setActive(true);
        employeeRepository.save(emp);

        eventBus.publish("EmployeeCreated", req.getCompanyId(), emp.getId(), "Employee " + emp.getEmployeeCode() + " created on hiring");
        return emp;
    }
}