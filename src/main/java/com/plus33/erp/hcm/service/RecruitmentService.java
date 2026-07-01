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
