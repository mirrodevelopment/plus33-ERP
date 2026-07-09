/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.audit
 * File              : PlatformAuditService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PlatformAuditController
 * Related Service   : PlatformAuditService
 * Related Repository: PlatformAuditRepository
 * Related Entity    : PlatformAudit
 * Related DTO       : N/A
 * Related Mapper    : PlatformAuditMapper
 * Related DB Table  : platform_audits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PlatformAuditController, PlatformAuditServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements PlatformAuditService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.audit;

import com.plus33.erp.platform.entity.PlatformAuditLog;
import com.plus33.erp.platform.repository.PlatformAuditLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code PlatformAuditService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.audit}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PlatformAuditController
 *   --> PlatformAuditService (this)
 *   --> Validate business rules
 *   --> PlatformAuditRepository (read/write 'platform_audits')
 *   --> PlatformAuditMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code platform_audits}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PlatformAuditService {
    @Autowired PlatformAuditLogRepository auditRepo;
    /**
     * Performs the logAudit operation in this module.
     *
     * @param actionName the actionName input value
     * @param userIdentity the userIdentity input value
     * @param traceContext the traceContext input value
     * @param payloadDiff the payloadDiff input value
     */
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