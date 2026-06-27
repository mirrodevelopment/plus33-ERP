package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.DepartmentRequest;
import com.plus33.erp.finance.budget.dto.DepartmentResponse;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDepartment(Long companyId, DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    DepartmentResponse getDepartment(Long id);
    List<DepartmentResponse> getDepartmentsByCompany(Long companyId);
}
