/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.service
 * File              : DepartmentService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepartmentController
 * Related Service   : DepartmentService, DepartmentServiceImpl
 * Related Repository: DepartmentRepository
 * Related Entity    : Department
 * Related DTO       : DepartmentRequest, DepartmentResponse
 * Related Mapper    : DepartmentMapper
 * Related DB Table  : departments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.budget.service;

import com.plus33.erp.finance.budget.dto.DepartmentRequest;
import com.plus33.erp.finance.budget.dto.DepartmentResponse;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DepartmentService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface DepartmentService {
    DepartmentResponse createDepartment(Long companyId, DepartmentRequest request);
    DepartmentResponse updateDepartment(Long id, DepartmentRequest request);
    DepartmentResponse getDepartment(Long id);
    List<DepartmentResponse> getDepartmentsByCompany(Long companyId);
}
