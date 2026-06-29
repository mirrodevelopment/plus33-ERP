package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.dto.PayslipResponse;

public interface EmployeeSelfService {
    PayslipResponse getPayslipForEmployee(Long runId, Long employeeId);
}
