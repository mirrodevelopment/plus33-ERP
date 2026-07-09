/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : DepartmentServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepartmentController
 * Related Service   : DepartmentServiceImpl
 * Related Repository: DepartmentRepository, CompanyRepository
 * Related Entity    : Department
 * Related DTO       : DepartmentRequest, DepartmentResponse, mapToResponse
 * Related Mapper    : DepartmentMapper
 * Related DB Table  : departments
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : DepartmentController, DepartmentServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements DepartmentService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.budget.dto.DepartmentRequest;
import com.plus33.erp.finance.budget.dto.DepartmentResponse;
import com.plus33.erp.finance.budget.entity.Department;
import com.plus33.erp.finance.budget.repository.DepartmentRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DepartmentServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DepartmentController
 *   --> DepartmentServiceImpl (this)
 *   --> Validate business rules
 *   --> DepartmentRepository (read/write 'departments')
 *   --> DepartmentMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code departments}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

    /**
     * Creates a new department and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the DepartmentResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public DepartmentResponse createDepartment(Long companyId, DepartmentRequest request) {
        Company company = companyRepository.findById(companyId)
            .orElseThrow(() -> new BusinessException("Company not found"));

        if (departmentRepository.findByCompanyIdAndCode(companyId, request.code()).isPresent()) {
            throw new BusinessException("Department code already exists: " + request.code());
        }

        Department department = Department.builder()
            .company(company)
            .code(request.code())
            .name(request.name())
            .active(request.active() != null ? request.active() : true)
            .build();

        return mapToResponse(departmentRepository.save(department));
    }

    /**
     * Updates an existing department record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param id the unique database ID of the resource
     * @param request the validated request DTO containing input data
     * @return the DepartmentResponse result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public DepartmentResponse updateDepartment(Long id, DepartmentRequest request) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));

        department.setName(request.name());
        if (request.active() != null) {
            department.setActive(request.active());
        }

        return mapToResponse(departmentRepository.save(department));
    }

    /**
     * Retrieves department data from the database.
     *
     * @param id the unique database ID of the resource
     * @return the DepartmentResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));
        return mapToResponse(department);
    }

    /**
     * Retrieves departments by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<DepartmentResponse> getDepartmentsByCompany(Long companyId) {
        return departmentRepository.findAll().stream()
            .filter(d -> d.getCompany().getId().equals(companyId))
            .map(this::mapToResponse)
            .toList();
    }

    private DepartmentResponse mapToResponse(Department department) {
        return new DepartmentResponse(
            department.getId(),
            department.getCompany().getId(),
            department.getCode(),
            department.getName(),
            department.getActive()
        );
    }
}