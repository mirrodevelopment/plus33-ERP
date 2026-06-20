package com.plus33.erp.workforce.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.dto.EmployeeSearchRequest;
import org.springframework.data.domain.Pageable;

public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    PageResponse<EmployeeResponse> searchEmployees(EmployeeSearchRequest searchRequest, Pageable pageable);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
    EmployeeResponse activateEmployee(Long id);
    EmployeeResponse deactivateEmployee(Long id);
}
