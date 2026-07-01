package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class OrganizationService {

    private final HcmOrganizationRepository organizationRepository;
    private final HcmDepartmentRepository departmentRepository;
    private final HcmPositionRepository positionRepository;
    private final PositionAssignmentRepository positionAssignmentRepository;

    public OrganizationService(HcmOrganizationRepository organizationRepository,
                               HcmDepartmentRepository departmentRepository,
                               HcmPositionRepository positionRepository,
                               PositionAssignmentRepository positionAssignmentRepository) {
        this.organizationRepository = organizationRepository;
        this.departmentRepository = departmentRepository;
        this.positionRepository = positionRepository;
        this.positionAssignmentRepository = positionAssignmentRepository;
    }

    @Transactional
    public HcmOrganization createOrganization(Long companyId, String name) {
        HcmOrganization org = new HcmOrganization();
        org.setCompanyId(companyId);
        org.setName(name);
        org.setEffectiveFrom(LocalDate.now());
        org.setEffectiveTo(LocalDate.of(9999, 12, 31));
        org.setIsCurrent(true);
        org.setVersionNumber(1);
        organizationRepository.save(org);
        return org;
    }

    @Transactional
    public HcmDepartment createDepartment(Long orgId, String name) {
        HcmDepartment dept = new HcmDepartment();
        dept.setOrganizationId(orgId);
        dept.setName(name);
        dept.setEffectiveFrom(LocalDate.now());
        dept.setEffectiveTo(LocalDate.of(9999, 12, 31));
        dept.setIsCurrent(true);
        dept.setVersionNumber(1);
        departmentRepository.save(dept);
        return dept;
    }

    @Transactional
    public HcmPosition createPosition(Long departmentId, String code, String title) {
        HcmPosition pos = new HcmPosition();
        pos.setDepartmentId(departmentId);
        pos.setCode(code);
        pos.setTitle(title);
        pos.setEffectiveFrom(LocalDate.now());
        pos.setEffectiveTo(LocalDate.of(9999, 12, 31));
        pos.setIsCurrent(true);
        pos.setVersionNumber(1);
        positionRepository.save(pos);
        return pos;
    }

    @Transactional
    public void assignEmployeeToPosition(Long positionId, Long employeeId) {
        // Expire old active assignments
        positionAssignmentRepository.findByPositionIdAndIsCurrent(positionId, true).forEach(a -> {
            a.setIsCurrent(false);
            a.setEffectiveTo(LocalDate.now());
            positionAssignmentRepository.save(a);
        });

        PositionAssignment assignment = new PositionAssignment();
        assignment.setPositionId(positionId);
        assignment.setEmployeeId(employeeId);
        assignment.setEffectiveFrom(LocalDate.now());
        assignment.setEffectiveTo(LocalDate.of(9999, 12, 31));
        assignment.setIsCurrent(true);
        positionAssignmentRepository.save(assignment);
    }
}
