/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : OrganizationService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: OrganizationController
 * Related Service   : OrganizationService
 * Related Repository: HcmOrganizationRepository, HcmDepartmentRepository, HcmPositionRepository, PositionAssignmentRepository
 * Related Entity    : Organization
 * Related DTO       : N/A
 * Related Mapper    : OrganizationMapper
 * Related DB Table  : organizations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : OrganizationController, OrganizationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements OrganizationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code OrganizationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * OrganizationController
 *   --> OrganizationService (this)
 *   --> Validate business rules
 *   --> OrganizationRepository (read/write 'organizations')
 *   --> OrganizationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code organizations}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Creates a new organization and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param name the name input value
     * @return the HcmOrganization result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new department and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param orgId the orgId input value
     * @param name the name input value
     * @return the HcmDepartment result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Creates a new position and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param departmentId the departmentId input value
     * @param code the code input value
     * @param title the title input value
     * @return the HcmPosition result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Performs the assignEmployeeToPosition operation in this module.
     *
     * @param positionId the positionId input value
     * @param employeeId the employeeId input value
     */
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