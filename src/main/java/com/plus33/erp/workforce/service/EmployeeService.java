/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : EmployeeService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeService, EmployeeServiceImpl
 * Related Repository: EmployeeRepository
 * Related Entity    : Employee
 * Related DTO       : EmployeeRequest, EmployeeResponse, EmployeeSearchRequest, PageResponse, searchRequest
 * Related Mapper    : EmployeeMapper
 * Related DB Table  : employees
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.common.dto.PageResponse;
import com.plus33.erp.workforce.dto.EmployeeRequest;
import com.plus33.erp.workforce.dto.EmployeeResponse;
import com.plus33.erp.workforce.dto.EmployeeSearchRequest;
import org.springframework.data.domain.Pageable;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EmployeeService {
    EmployeeResponse createEmployee(EmployeeRequest request);
    EmployeeResponse getEmployeeById(Long id);
    PageResponse<EmployeeResponse> searchEmployees(EmployeeSearchRequest searchRequest, Pageable pageable);
    EmployeeResponse updateEmployee(Long id, EmployeeRequest request);
    void deleteEmployee(Long id);
    EmployeeResponse activateEmployee(Long id);
    EmployeeResponse deactivateEmployee(Long id);
}
