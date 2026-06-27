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

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final CompanyRepository companyRepository;

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

    @Override
    @Transactional(readOnly = true)
    public DepartmentResponse getDepartment(Long id) {
        Department department = departmentRepository.findById(id)
            .orElseThrow(() -> new BusinessException("Department not found"));
        return mapToResponse(department);
    }

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
