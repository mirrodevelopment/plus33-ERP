/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollProcessingService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollProcessingController
 * Related Service   : PayrollProcessingService, PayrollProcessingServiceImpl
 * Related Repository: PayrollProcessingRepository
 * Related Entity    : PayrollProcessing
 * Related DTO       : PayrollDashboardSummaryResponse, PayrollRunRequest, PayrollRunResponse
 * Related Mapper    : PayrollProcessingMapper
 * Related DB Table  : payroll_processings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.dto.PayrollDashboardSummaryResponse;
import com.plus33.erp.workforce.dto.PayrollRunRequest;
import com.plus33.erp.workforce.dto.PayrollRunResponse;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollProcessingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Data Transfer Object: serializes API request/response data for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PayrollProcessingService {
    PayrollRunResponse createPayrollRun(PayrollRunRequest request);
    PayrollRunResponse calculatePayrollRun(Long runId);
    PayrollRunResponse approvePayrollRun(Long runId, String approver);
    PayrollRunResponse postPayrollRun(Long runId);
    PayrollRunResponse payPayrollRun(Long runId);
    PayrollRunResponse reversePayrollRun(Long runId);
    PayrollRunResponse getPayrollRun(Long runId);
    PayrollDashboardSummaryResponse getDashboardSummary(Long companyId);
}
