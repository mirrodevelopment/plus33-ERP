package com.plus33.erp.workforce.event;

public class PayrollCalculatedEvent {
    private final Long payrollRunId;
    private final Long companyId;

    public PayrollCalculatedEvent(Long payrollRunId, Long companyId) {
        this.payrollRunId = payrollRunId;
        this.companyId = companyId;
    }

    public Long getPayrollRunId() { return payrollRunId; }
    public Long getCompanyId() { return companyId; }
}
