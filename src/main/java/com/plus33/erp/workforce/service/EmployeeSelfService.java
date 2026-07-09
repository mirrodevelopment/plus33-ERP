/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : EmployeeSelfService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeSelfController
 * Related Service   : EmployeeSelfService, EmployeeSelfServiceImpl
 * Related Repository: EmployeeSelfRepository
 * Related Entity    : EmployeeSelf
 * Related DTO       : PayslipResponse
 * Related Mapper    : EmployeeSelfMapper
 * Related DB Table  : employee_selfs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.dto.PayslipResponse;

public interface EmployeeSelfService {
    PayslipResponse getPayslipForEmployee(Long runId, Long employeeId);
    java.util.List<PayslipResponse> getPayslipsForEmployee(Long employeeId);
}
