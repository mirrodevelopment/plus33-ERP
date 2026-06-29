package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.PayrollAuditEvent;
import com.plus33.erp.workforce.repository.PayrollAuditEventRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PayrollAuditTimelineServiceImpl implements PayrollAuditTimelineService {

    private final PayrollAuditEventRepository auditEventRepository;

    public PayrollAuditTimelineServiceImpl(PayrollAuditEventRepository auditEventRepository) {
        this.auditEventRepository = auditEventRepository;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logAuditEvent(Long companyId, Long payrollRunId, String eventType, String description, String actor) {
        PayrollAuditEvent event = new PayrollAuditEvent();
        event.setCompanyId(companyId);
        event.setPayrollRunId(payrollRunId);
        event.setEventType(eventType);
        event.setDescription(description);
        event.setActor(actor != null ? actor : "SYSTEM");
        auditEventRepository.save(event);
    }
}
