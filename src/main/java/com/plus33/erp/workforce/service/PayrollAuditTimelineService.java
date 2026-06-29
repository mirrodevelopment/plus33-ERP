package com.plus33.erp.workforce.service;

public interface PayrollAuditTimelineService {
    void logAuditEvent(Long companyId, Long payrollRunId, String eventType, String description, String actor);
}
