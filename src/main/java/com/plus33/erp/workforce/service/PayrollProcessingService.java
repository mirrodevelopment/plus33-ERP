package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.dto.PayrollDashboardSummaryResponse;
import com.plus33.erp.workforce.dto.PayrollRunRequest;
import com.plus33.erp.workforce.dto.PayrollRunResponse;

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
