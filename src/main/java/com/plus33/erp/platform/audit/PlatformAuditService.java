package com.plus33.erp.platform.audit;

import com.plus33.erp.platform.entity.PlatformAuditLog;
import com.plus33.erp.platform.repository.PlatformAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class PlatformAuditService {
    @Autowired PlatformAuditLogRepository auditRepo;

    @Transactional
    public void logAudit(String actionName, String userIdentity, String traceContext, String payloadDiff) {
        PlatformAuditLog log = new PlatformAuditLog();
        log.setActionName(actionName);
        log.setUserIdentity(userIdentity);
        log.setTraceContext(traceContext);
        log.setPayloadDiff(payloadDiff);
        log.setCreatedAt(LocalDateTime.now());
        auditRepo.save(log);
    }
}